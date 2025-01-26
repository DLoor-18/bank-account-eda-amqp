package ec.com.sofka.queries.query.transcationType;

import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;
import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.queries.responses.TransactionTypeResponse;
import reactor.core.publisher.Flux;

public class GetAllTransactionTypeUseCase implements IUseCaseGet<TransactionTypeResponse> {
    private final TransactionTypeRepository transactionTypeRepository;

    public GetAllTransactionTypeUseCase(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Override
    public Flux<QueryResponse<TransactionTypeResponse>> get() {
        return getAll()
                .map(TransactionTypeMapper::mapToResponseFromModel)
                .collectList()
                .flatMapMany(list -> {
                    QueryResponse<TransactionTypeResponse> queryResponse = QueryResponse.ofMultiple(list);
                    return Flux.just(queryResponse);
                });
    }

    public Flux<TransactionType> getAll() {
        return transactionTypeRepository.findAll()
                .map(TransactionTypeMapper::mapToModelFromDTO);

    }
}