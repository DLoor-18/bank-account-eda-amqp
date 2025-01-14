package ec.com.sofka.commandas.usecases.account;

import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.aggregates.account.entities.account.values.AccountId;
import ec.com.sofka.aggregates.account.entities.account.values.objects.AccountNumber;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.query.account.FindAccountByNumberUseCase;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAccountByNumberUseCaseTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @InjectMocks
    private FindAccountByNumberUseCase findAccountByNumberUseCase;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(AccountId.of("1"), AccountNumber.of("2200000000"), Amount.of(new BigDecimal(100)), StatusEnum.ACTIVE, null);
    }

    @Test
    @DisplayName("Should find account when record exist")
    public void testFindAccountByNumber_WhenAccountFound() {
        when(accountRepository.findByNumber(account.getAccountNumber().getValue()))
                .thenReturn(Mono.just(AccountMapper.mapToDTOFromModel(account)));

        Mono<Account> result = findAccountByNumberUseCase.getByNumberAccount(account.getAccountNumber().getValue());

        StepVerifier.create(result)
                .expectNext(account)
                .verifyComplete();

        verify(accountRepository).findByNumber(account.getAccountNumber().getValue());
    }

    @Test
    @DisplayName("Should no account found when record not exist")
    public void testFindAccountByNumber_WhenNoAccountFound() {
        when(accountRepository.findByNumber(account.getAccountNumber().getValue())).thenReturn(Mono.empty());

        Mono<Account> result = findAccountByNumberUseCase.getByNumberAccount(account.getAccountNumber().getValue());

        doNothing().when(errorBusMessage)
                .sendMsg(any(ErrorMessage.class));

        StepVerifier.create(result)
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(accountRepository).findByNumber(account.getAccountNumber().getValue());
    }

}
