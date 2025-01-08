package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<UserDTO> save(UserDTO user);

    Flux<UserDTO> findAll();

    Mono<UserDTO> findById(String id);

    Mono<UserDTO> findByIdentityCard(String identityCard);

}