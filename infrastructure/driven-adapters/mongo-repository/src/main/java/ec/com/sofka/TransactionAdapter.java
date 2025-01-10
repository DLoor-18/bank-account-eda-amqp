package ec.com.sofka;

import ec.com.sofka.database.account.ITransactionRepository;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.mapper.TransactionEntityMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TransactionAdapter implements TransactionRepository {
    private final ITransactionRepository transactionRepository;

    public TransactionAdapter(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Mono<TransactionDTO> save(TransactionDTO transaction) {
        return transactionRepository.save(TransactionEntityMapper.mapToEntity(transaction))
                .map(TransactionEntityMapper::mapToDTO);
    }

    @Override
    public Flux<TransactionDTO> findAll() {
        return transactionRepository.findAll()
                .map(TransactionEntityMapper::mapToDTO);
    }

}