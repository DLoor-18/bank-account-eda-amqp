package ec.com.sofka.handlers.transaction;

import ec.com.sofka.data.PropertyRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.mapper.PropertyModelMapper;
import ec.com.sofka.mapper.TransactionModelMapper;
import ec.com.sofka.queries.query.transaction.FindTransactionsByAccountNumberUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class FindTransactionsByAccountNumberHandler {
    private final FindTransactionsByAccountNumberUseCase findTransactionsByAccountNumberUseCase;

    public FindTransactionsByAccountNumberHandler(FindTransactionsByAccountNumberUseCase findTransactionsByAccountNumberUseCase) {
        this.findTransactionsByAccountNumberUseCase = findTransactionsByAccountNumberUseCase;
    }

    public Flux<TransactionResponseDTO> findByAccountNumber(PropertyRequestDTO propertyRequestDTO) {
        return findTransactionsByAccountNumberUseCase.get(PropertyModelMapper.mapToRequest(propertyRequestDTO))
                .flatMap(queryResponse -> Flux.fromIterable(queryResponse.getMultipleResults()))
                .map(TransactionModelMapper::mapToDTO);

    }
}