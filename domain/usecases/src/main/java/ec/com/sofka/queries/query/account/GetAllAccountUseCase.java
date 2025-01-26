package ec.com.sofka.queries.query.account;

import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.queries.responses.AccountResponse;
import reactor.core.publisher.Flux;

public class GetAllAccountUseCase implements IUseCaseGet<AccountResponse> {
    private final AccountRepository accountRepository;

    public GetAllAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Flux<QueryResponse<AccountResponse>> get() {
        return getAll()
                .map(AccountMapper::mapToResponseFromModel)
                .collectList()
                .flatMapMany(list -> {
                    QueryResponse<AccountResponse> queryResponse = QueryResponse.ofMultiple(list);
                    return Flux.just(queryResponse);
                });
    }

    public Flux<Account> getAll() {
        return accountRepository.findAll()
                .map(AccountMapper::mapToModelFromDTO);

    }
}