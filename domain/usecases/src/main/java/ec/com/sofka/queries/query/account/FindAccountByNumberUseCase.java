package ec.com.sofka.queries.query.account;

import ec.com.sofka.aggregate.AccountAggregate;
import ec.com.sofka.aggregate.entities.account.Account;
import ec.com.sofka.exception.RecordNotFoundException;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.generics.shared.GetElementQuery;
import ec.com.sofka.queries.responses.AccountResponse;
import reactor.core.publisher.Mono;

public class FindAccountByNumberUseCase implements IUseCaseGetElement<GetElementQuery, AccountResponse> {
    private final IEventStore eventStore;
    private final ErrorBusMessage errorBusMessage;

    public FindAccountByNumberUseCase(IEventStore eventStore, ErrorBusMessage errorBusMessage) {
        this.eventStore = eventStore;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<AccountResponse> get(GetElementQuery request) {

        return getByAggregate(request.getAggregateId())
                .map(AccountMapper::mapToResponseFromModel);
    }

    public Mono<Account> getByAggregate(String aggregateId) {
        return eventStore.findAggregate(aggregateId)
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Account not found", "Get Account by NumberAccount"));
                    return Mono.error(new RecordNotFoundException("Account not found."));
                }))
                .collectList()
                .map(eventsList -> AccountAggregate.from(aggregateId, eventsList).getAccount());
    }

}