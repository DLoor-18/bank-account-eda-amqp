package ec.com.sofka.queries.query.transaction;

import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.queries.responses.TransactionResponse;
import reactor.core.publisher.Flux;

public class FindTransactionsByAccountNumberUseCase implements IUseCaseGetElement<PropertyQuery, TransactionResponse> {
    private final TransactionRepository transactionRepository;

    public FindTransactionsByAccountNumberUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Flux<QueryResponse<TransactionResponse>> get(PropertyQuery request) {

        return getByAccountNumber(request.getProperty())
                .map(TransactionMapper::mapToResponseFromModel)
                .collectList()
                .flatMapMany(list -> {
                    QueryResponse<TransactionResponse> queryResponse = QueryResponse.ofMultiple(list);
                    return Flux.just(queryResponse);
                });
    }

    public Flux<Transaction> getByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber)
                .map(TransactionMapper::mapToModelFromDTO);

    }
}
