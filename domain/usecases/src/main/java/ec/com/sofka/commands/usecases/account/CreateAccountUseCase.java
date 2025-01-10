package ec.com.sofka.commands.usecases.account;

import ec.com.sofka.aggregate.AccountAggregate;
import ec.com.sofka.queries.query.user.FindUserByIdUseCase;
import ec.com.sofka.commands.AccountCommand;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.queries.responses.AccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateAccountUseCase implements IUseCaseExecute<AccountCommand, AccountResponse> {
    private final IEventStore repository;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final EventBusMessage eventBusMessage;

    public CreateAccountUseCase(IEventStore repository, FindUserByIdUseCase findUserByIdUseCase, EventBusMessage eventBusMessage) {
        this.repository = repository;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.eventBusMessage = eventBusMessage;
    }

    public Mono<AccountResponse> execute(AccountCommand accountCommand) {
        AccountAggregate accountAggregate = new AccountAggregate();

        return findUserByIdUseCase.getUserByAggregate(accountCommand.getUserAggregateId())
                .flatMap(user -> {
                    accountAggregate.createAccount(
                            accountCommand.getAccountNumber(),
                            accountCommand.getBalance(),
                            accountCommand.getStatus(),
                            user
                    );

                    return Flux.fromIterable(accountAggregate.getUncommittedEvents())
                            .flatMap(repository::save)
                            .doOnNext(eventBusMessage::sendEvent)
                            .then(Mono.fromCallable(() -> {
                                accountAggregate.markEventsAsCommitted();
                                return AccountMapper.mapToResponseFromModel(accountAggregate.getAccount());
                            }));
                });
    }

}