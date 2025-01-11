package ec.com.sofka.cases.user;

import ec.com.sofka.User;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.queries.query.customer.FindCustomerByCiUseCase;
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
public class FindCustomerByCiUseCaseTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private FindCustomerByCiUseCase findCustomerByCiUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setCi("1310000000");
        user.setEmail("diego.loor@sofka.com.co");
        user.setFirstName("Diego");
        user.setLastName("Loor");
        user.setPassword("diego.loor@sofka.com.co");
        user.setStatus("ACTIVE");
    }

    @Test
    public void testFindUserByCi_WhenUserFound() {
        when(customerRepository.findByCi(user.getCi())).thenReturn(Mono.just(user));

        Mono<User> result = findCustomerByCiUseCase.apply(user.getCi());

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        verify(customerRepository).findByCi(user.getCi());
    }

    @Test
    public void testFindUserByCi_WhenNoUserFound() {
        when(customerRepository.findByCi(user.getCi())).thenReturn(Mono.empty());

        Mono<User> result = findCustomerByCiUseCase.apply(user.getCi());

        StepVerifier.create(result)
                .expectNext()
                .verifyComplete();

        verify(customerRepository).findByCi(user.getCi());
    }

}