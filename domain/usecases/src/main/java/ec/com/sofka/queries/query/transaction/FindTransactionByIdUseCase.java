package ec.com.sofka.queries.query.transaction;

import ec.com.sofka.aggregates.Account.AccountAggregate;
import ec.com.sofka.aggregates.Account.entities.transaction.Transaction;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.generics.shared.GetElementQuery;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.responses.TransactionResponse;
import reactor.core.publisher.Mono;

public class FindTransactionByIdUseCase implements IUseCaseGetElement<GetElementQuery, TransactionResponse> {
    private final TransactionRepository transactionRepository;
    private final IEventStore eventStore;
    private final ErrorBusMessage errorBusMessage;

    public FindTransactionByIdUseCase(TransactionRepository transactionRepository, IEventStore eventStore, ErrorBusMessage errorBusMessage) {
        this.transactionRepository = transactionRepository;
        this.eventStore = eventStore;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<TransactionResponse> get(GetElementQuery request) {

        return getByAggregate(request.getAggregateId())
                .map(TransactionMapper::mapToResponseFromModel);
    }

    public Mono<Transaction> getByAggregate(String aggregateId) {
        return eventStore.findAggregate(aggregateId)
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Transaction not found", "Get Transaction by Id"));
                    return Mono.error(new RecordNotFoundException("Transaction not found."));
                }))
                .collectList()
                .map(eventsList -> AccountAggregate.from(aggregateId, eventsList).getTransaction());
    }

}