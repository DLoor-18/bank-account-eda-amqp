package ec.com.sofka.commandas.usecases.customer;

import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.aggregates.account.entities.customer.values.CustomerId;
import ec.com.sofka.aggregates.account.entities.customer.values.objects.IdentityCard;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.mapper.CustomerMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.query.customer.FindCustomerByIdUseCase;
import ec.com.sofka.queries.responses.CustomerResponse;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindCustomerByIdUseCaseTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @InjectMocks
    private FindCustomerByIdUseCase findCustomerByIdUseCase;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(CustomerId.of("u1"), "Jhon","Doe", IdentityCard.of("1234567890"), StatusEnum.ACTIVE);
    }

    @Test
    @DisplayName("Should find customer when record exist")
    public void testFindCustomerById_WhenCustomerFound() {
        when(customerRepository.findById(customer.getId().getValue()))
                .thenReturn(Mono.just(CustomerMapper.mapToDTOFromModel(customer)));

        Mono<CustomerResponse> result = findCustomerByIdUseCase.get(new PropertyQuery(customer.getId().getValue()))
                .flatMap(queryResponse -> Mono.justOrEmpty(queryResponse.getSingleResult().orElseThrow(() -> new RuntimeException("TransactionType not found"))));

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(customerRepository).findById(customer.getId().getValue());
    }

    @Test
    @DisplayName("Should no customer found when record not exist")
    public void testFindCustomerById_WhenNoCustomerFound() {
        when(customerRepository.findById(customer.getId().getValue())).thenReturn(Mono.empty());

        Mono<Customer> result = findCustomerByIdUseCase.getById(customer.getId().getValue());

        doNothing().when(errorBusMessage)
                .sendMsg(any(ErrorMessage.class));

        StepVerifier.create(result)
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(customerRepository).findById(customer.getId().getValue());
    }

}
