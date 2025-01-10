package ec.com.sofka.queries.query.user;

import ec.com.sofka.aggregate.AccountAggregate;
import ec.com.sofka.exception.RecordNotFoundException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.mapper.UserMapper;
import ec.com.sofka.generics.shared.GetElementQuery;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.responses.UserResponse;
import reactor.core.publisher.Mono;

public class FindUserByCiUseCase implements IUseCaseGetElement<GetElementQuery, UserResponse> {
    private final UserRepository userRepository;
    private final IEventStore eventStore;
    private final ErrorBusMessage errorBusMessage;

    public FindUserByCiUseCase(UserRepository userRepository, IEventStore eventStore, ErrorBusMessage errorBusMessage) {
        this.userRepository = userRepository;
        this.eventStore = eventStore;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<UserResponse> get(GetElementQuery request) {

        return eventStore.findAggregate(request.getAggregateId())
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("User not found", "Get User by Id"));
                    return Mono.error(new RecordNotFoundException("User not found."));
                }))
                .collectList()
                .map(eventsList -> AccountAggregate.from(request.getAggregateId(), eventsList))
                .flatMap(accountAggregate ->
                        userRepository.findByIdentityCard(accountAggregate.getUser().getIdentityCard().getValue())
                                .map(UserMapper::mapToResponseFromDTO)
                );

    }

}