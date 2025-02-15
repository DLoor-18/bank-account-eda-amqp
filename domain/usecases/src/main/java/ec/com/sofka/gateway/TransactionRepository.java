package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.TransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository {

    Mono<TransactionDTO> save(TransactionDTO transaction);

    Mono<TransactionDTO> findById(String id);

    Flux<TransactionDTO> findByAccountNumber(String numberAccount);

    Flux<TransactionDTO> findAll();

}