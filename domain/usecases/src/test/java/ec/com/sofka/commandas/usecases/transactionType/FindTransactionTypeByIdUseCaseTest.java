package ec.com.sofka.commandas.usecases.transactionType;

import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;
import ec.com.sofka.aggregates.account.entities.transactionType.values.TransactionTypeId;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.query.transcationType.FindTransactionTypeByIdUseCase;
import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.queries.responses.TransactionTypeResponse;
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

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindTransactionTypeByIdUseCaseTest {
    @Mock
    private TransactionTypeRepository transactionTypeRepository;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @InjectMocks
    private FindTransactionTypeByIdUseCase findTransactionTypeByIdUseCase;

    private TransactionType transactionType;

    @BeforeEach
    public void setUp() {
        transactionType = new TransactionType(TransactionTypeId.of("u1"),"Deposit from branch", "Deposit from branch.", Amount.of(new BigDecimal(10)), true, true, StatusEnum.ACTIVE);
    }

    @Test
    @DisplayName("Should find transactionType when record exist")
    public void testFindTransactionTypeById_WhenTransactionTypeFound() {
        when(transactionTypeRepository.findById(transactionType.getId().getValue()))
                .thenReturn(Mono.just(TransactionTypeMapper.mapToDTOFromModel(transactionType)));

        Mono<TransactionTypeResponse> result = findTransactionTypeByIdUseCase.get(new PropertyQuery(transactionType.getId().getValue()))
                .flatMap(queryResponse -> Mono.justOrEmpty(queryResponse.getSingleResult().orElseThrow(() -> new RuntimeException("TransactionType not found"))));

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(transactionTypeRepository).findById(transactionType.getId().getValue());
    }

    @Test
    @DisplayName("Should no transactionType found when record not exist")
    public void testFindTransactionTypeById_WhenNoTransactionTypeFound() {
        when(transactionTypeRepository.findById(transactionType.getId().getValue())).thenReturn(Mono.empty());

        Mono<TransactionType> result = findTransactionTypeByIdUseCase.getById(transactionType.getId().getValue());

        doNothing().when(errorBusMessage)
                .sendMsg(any(ErrorMessage.class));

        StepVerifier.create(result)
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(transactionTypeRepository).findById(transactionType.getId().getValue());
    }

}