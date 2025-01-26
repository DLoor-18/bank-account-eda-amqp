package ec.com.sofka.handler;

import ec.com.sofka.data.UserResponseDTO;
import ec.com.sofka.mapper.UserModelMapper;
import ec.com.sofka.queries.query.user.GetAllUserUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GetAllUserHandler  {
    private final GetAllUserUseCase getAllUserUseCase;

    public GetAllUserHandler(GetAllUserUseCase getAllUserUseCase) {
        this.getAllUserUseCase = getAllUserUseCase;
    }

    public Flux<UserResponseDTO> getAll() {
        return getAllUserUseCase.get()
                .flatMap(queryResponse -> Flux.fromIterable(queryResponse.getMultipleResults()))
                .map(UserModelMapper::mapToDTO);

    }
}