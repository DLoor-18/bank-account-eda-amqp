package ec.com.sofka.commands.usecases.user;

import ec.com.sofka.aggregate.AccountAggregate;
import ec.com.sofka.exception.ConflictException;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.mapper.UserMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.commands.UserCommand;
import ec.com.sofka.queries.responses.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateUserUseCase implements IUseCaseExecute<UserCommand, UserResponse> {
    private final IEventStore repository;
    private final UserRepository userRepository;
    private final ErrorBusMessage errorBusMessage;
    private final EventBusMessage eventBusMessage;

    public CreateUserUseCase(IEventStore repository, UserRepository userRepository, ErrorBusMessage errorBusMessage, EventBusMessage eventBusMessage) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.errorBusMessage = errorBusMessage;
        this.eventBusMessage = eventBusMessage;
    }

    public Mono<UserResponse> execute(UserCommand userCommand) {
        AccountAggregate accountAggregate = new AccountAggregate();

        return userRepository.findByIdentityCard(userCommand.getIdentityCard())
                .flatMap(userFound -> {
                    errorBusMessage.sendMsg(new ErrorMessage("User is already registered (" + userCommand.getIdentityCard() + ")",
                            "Create User"));
                    return Mono.<UserResponse>error(new ConflictException("The user is already registered."));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    accountAggregate.createUser(
                            userCommand.getFirstName(),
                            userCommand.getLastName(),
                            userCommand.getIdentityCard(),
                            userCommand.getEmail(),
                            userCommand.getPassword(),
                            userCommand.getStatus()
                    );

                    return Flux.fromIterable(accountAggregate.getUncommittedEvents())
                            .flatMap(repository::save)
                            .doOnNext(eventBusMessage::sendEvent)
                            .then(Mono.fromCallable(() -> {
                                accountAggregate.markEventsAsCommitted();
                                return UserMapper.mapToResponseFromModel(accountAggregate.getUser());
                            }));
                }));
    }

}