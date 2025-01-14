package ec.com.sofka.commandas.usecases.transaction;

import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.aggregates.account.entities.transaction.values.TransactionId;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.ProcessingDate;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.TransactionAccount;
import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.aggregates.account.entities.transaction.values.TransactionId;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.query.transaction.FindTransactionByIdUseCase;
import ec.com.sofka.queries.responses.TransactionResponse;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FindTransactionByIdUseCaseTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @InjectMocks
    private FindTransactionByIdUseCase findTransactionByIdUseCase;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction(TransactionId.of("u1"), TransactionAccount.of("1234567890"),"transaction made in Manabí.", Amount.of(new BigDecimal(1000)), ProcessingDate.of(new Date().toString()), null, null);
    }

    @Test
    @DisplayName("Should find transaction when record exist")
    public void testFindTransactionById_WhenTransactionFound() {
        when(transactionRepository.findById(transaction.getId().getValue()))
                .thenReturn(Mono.just(TransactionMapper.mapToDTOFromModel(transaction)));

        Mono<TransactionResponse> result = findTransactionByIdUseCase.get(new PropertyQuery(transaction.getId().getValue()))
                .flatMap(queryResponse -> Mono.justOrEmpty(queryResponse.getSingleResult().orElseThrow(() -> new RuntimeException("Transaction not found"))));

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(transactionRepository).findById(transaction.getId().getValue());
    }

    @Test
    @DisplayName("Should no transaction found when record not exist")
    public void testFindTransactionById_WhenNoTransactionFound() {
        when(transactionRepository.findById(transaction.getId().getValue())).thenReturn(Mono.empty());

        Mono<Transaction> result = findTransactionByIdUseCase.getById(transaction.getId().getValue());

        doNothing().when(errorBusMessage)
                .sendMsg(any(ErrorMessage.class));

        StepVerifier.create(result)
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(transactionRepository).findById(transaction.getId().getValue());
    }

}
