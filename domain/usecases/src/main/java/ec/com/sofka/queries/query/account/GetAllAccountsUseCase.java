package ec.com.sofka.queries.query.account;

import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.queries.responses.AccountResponse;
import reactor.core.publisher.Flux;

public class GetAllAccountsUseCase {
    private final AccountRepository accountRepository;

    public GetAllAccountsUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Flux<AccountResponse> apply() {
        return accountRepository.findAll()
                .map(AccountMapper::mapToResponseFromDTO);
    }

}