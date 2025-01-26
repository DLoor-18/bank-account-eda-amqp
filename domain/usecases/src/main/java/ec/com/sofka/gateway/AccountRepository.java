package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository {
    Mono<AccountDTO> save(AccountDTO userDTO);
    Mono<AccountDTO> findByNumber(String email);
    Flux<AccountDTO> findAll();
}