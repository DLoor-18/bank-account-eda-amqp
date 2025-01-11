package ec.com.sofka.cases.user;

import ec.com.sofka.User;
import ec.com.sofka.gateway.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllUsersUseCaseTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private GetAllUsersUseCase getAllUsersUseCase;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setCi("1310000000");
        user.setEmail("diego.loor@sofka.com.co");
        user.setFirstName("Diego");
        user.setLastName("Loor");
        user.setPassword("diego.loor@sofka.com.co");
        user.setStatus("ACTIVE");
    }

    @Test
    public void testGetAllUsers_WhenUsersExist() {
        when(customerRepository.findAll()).thenReturn(Flux.just(user));

        Flux<User> result = getAllUsersUseCase.apply();

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        verify(customerRepository).findAll();
    }

    @Test
    public void testGetAllUsers_WhenNoUsersExist() {
        when(customerRepository.findAll()).thenReturn(Flux.empty());

        Flux<User> result = getAllUsersUseCase.apply();

        StepVerifier.create(result)
                .expectNext()
                .verifyComplete();

        verify(customerRepository).findAll();
    }

}