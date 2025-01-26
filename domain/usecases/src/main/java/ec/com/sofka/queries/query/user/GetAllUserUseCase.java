package ec.com.sofka.queries.query.user;

import ec.com.sofka.aggregates.auth.entities.user.User;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.UserMapper;
import ec.com.sofka.queries.responses.UserResponse;
import reactor.core.publisher.Flux;

public class GetAllUserUseCase implements IUseCaseGet<UserResponse> {
    private final UserRepository userRepository;

    public GetAllUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<QueryResponse<UserResponse>> get() {
        return getAll()
                .map(UserMapper::mapToResponseFromModel)
                .collectList()
                .flatMapMany(list -> {
                    QueryResponse<UserResponse> queryResponse = QueryResponse.ofMultiple(list);
                    return Flux.just(queryResponse);
                });
    }

    public Flux<User> getAll() {
        return userRepository.findAll()
                .map(UserMapper::mapToModelFromDTO);

    }
}