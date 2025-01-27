package ec.com.sofka.repository.account;

import ec.com.sofka.data.TransactionEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ITransactionRepository extends ReactiveMongoRepository<TransactionEntity, String> {

    Flux<TransactionEntity> findByAccountNumber(String accountNumber);
}