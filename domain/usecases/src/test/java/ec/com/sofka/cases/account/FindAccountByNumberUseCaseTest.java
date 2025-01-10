package ec.com.sofka.cases.account;

import ec.com.sofka.Account;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.queries.query.account.FindAccountByNumberUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAccountByNumberUseCaseTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private FindAccountByNumberUseCase findAccountByNumberUseCase;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setNumber("2200000000");
        account.setAvailableBalance(new BigDecimal(100));
        account.setRetainedBalance(new BigDecimal(0));
        account.setStatus("ACTIVE");
    }

    @Test
    public void testFindAccountByNumber_WhenUserFound() {
        when(accountRepository.findByNumber(account.getNumber())).thenReturn(Mono.just(account));

        Mono<Account> result = findAccountByNumberUseCase.apply(account.getNumber());

        StepVerifier.create(result)
                .expectNext(account)
                .verifyComplete();

        verify(accountRepository).findByNumber(account.getNumber());
    }

    @Test
    public void testFindAccountByNumber_WhenNoUserFound() {
        when(accountRepository.findByNumber(account.getNumber())).thenReturn(Mono.empty());

        Mono<Account> result = findAccountByNumberUseCase.apply(account.getNumber());

        StepVerifier.create(result)
                .expectNext()
                .verifyComplete();

        verify(accountRepository).findByNumber(account.getNumber());
    }

}