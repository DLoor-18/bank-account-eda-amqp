package ec.com.sofka.handlers.transcationType;

import ec.com.sofka.data.TransactionTypeResponseDTO;
import ec.com.sofka.mapper.TransactionTypeModelMapper;
import ec.com.sofka.queries.query.transcationType.GetAllTransactionTypeUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GetAllTransactionTypesHandler {
    private final GetAllTransactionTypeUseCase getAllTransactionTypeUseCase;

    public GetAllTransactionTypesHandler(GetAllTransactionTypeUseCase getAllTransactionTypeUseCase) {
        this.getAllTransactionTypeUseCase = getAllTransactionTypeUseCase;
    }

    public Flux<TransactionTypeResponseDTO> getAll() {
        return getAllTransactionTypeUseCase.get()
                .flatMap(queryResponse -> Flux.fromIterable(queryResponse.getMultipleResults()))
                .map(TransactionTypeModelMapper::mapToDTO);

    }
}