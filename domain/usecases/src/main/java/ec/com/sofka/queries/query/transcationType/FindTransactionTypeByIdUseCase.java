package ec.com.sofka.queries.query.transcationType;

import ec.com.sofka.aggregates.Account.AccountAggregate;
import ec.com.sofka.aggregates.Account.entities.transactionType.TransactionType;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.generics.shared.GetElementQuery;
import ec.com.sofka.queries.responses.TransactionTypeResponse;
import reactor.core.publisher.Mono;

public class FindTransactionTypeByIdUseCase implements IUseCaseGetElement<GetElementQuery, TransactionTypeResponse> {
    private final IEventStore eventStore;
    private final ErrorBusMessage errorBusMessage;

    public FindTransactionTypeByIdUseCase(IEventStore eventStore, ErrorBusMessage errorBusMessage) {
        this.eventStore = eventStore;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<TransactionTypeResponse> get(GetElementQuery request) {

        return getUserByAggregate(request.getAggregateId())
                                .map(TransactionTypeMapper::mapToResponseFromModel);
    }

    public Mono<TransactionType> getUserByAggregate(String aggregateId) {
        return eventStore.findAggregate(aggregateId)
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Transaction Type not found", "Get TransactionType by Id"));
                    return Mono.error(new RecordNotFoundException("Transaction Type not found."));
                }))
                .collectList()
                .map(eventsList -> AccountAggregate.from(aggregateId, eventsList).getTransactionType());
    }

}