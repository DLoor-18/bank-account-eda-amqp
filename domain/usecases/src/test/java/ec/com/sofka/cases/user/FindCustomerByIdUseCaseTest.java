package ec.com.sofka.cases.user;

import ec.com.sofka.User;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.queries.query.customer.FindCustomerByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindCustomerByIdUseCaseTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private FindCustomerByIdUseCase findCustomerByIdUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("u1");
        user.setCi("1310000000");
        user.setEmail("diego.loor@sofka.com.co");
        user.setFirstName("Diego");
        user.setLastName("Loor");
        user.setPassword("diego.loor@sofka.com.co");
        user.setStatus("ACTIVE");
    }

    @Test
    public void testFindUserById_WhenUserFound() {
        when(customerRepository.findById(user.getId())).thenReturn(Mono.just(user));

        Mono<User> result = findCustomerByIdUseCase.apply(user.getId());

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        verify(customerRepository).findById(user.getId());
    }

    @Test
    public void testFindUserById_WhenNoUserFound() {
        when(customerRepository.findById(user.getId())).thenReturn(Mono.empty());

        Mono<User> result = findCustomerByIdUseCase.apply(user.getId());

        StepVerifier.create(result)
                .expectNext()
                .verifyComplete();

        verify(customerRepository).findById(user.getId());
    }

}