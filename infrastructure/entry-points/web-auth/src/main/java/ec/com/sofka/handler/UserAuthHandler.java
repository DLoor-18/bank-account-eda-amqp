package ec.com.sofka.handler;

import ec.com.sofka.commands.usecases.user.CreateUserUseCase;
import ec.com.sofka.commands.usecases.user.UpdateUserUseCase;
import ec.com.sofka.data.UserRequestDTO;
import ec.com.sofka.data.UserResponseDTO;
import ec.com.sofka.data.UserUpdateRequestDTO;
import ec.com.sofka.mapper.UserModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserAuthHandler {
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final PasswordEncoder passwordEncoder;

    public UserAuthHandler(CreateUserUseCase createUserUseCase, UpdateUserUseCase updateUserUseCase, PasswordEncoder passwordEncoder) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<UserResponseDTO> save(UserRequestDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return createUserUseCase.execute(UserModelMapper.mapToRequest(user))
                .map(UserModelMapper::mapToDTO);
    }

    public Mono<UserResponseDTO> update(UserUpdateRequestDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return updateUserUseCase.execute(UserModelMapper.mapToUpdateRequest(user))
                .map(UserModelMapper::mapToDTO);
    }

}