package ec.com.sofka;

import ec.com.sofka.database.account.IAccountRepository;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.mapper.AccountEntityMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AccountAdapter implements AccountRepository {
    private final IAccountRepository accountRepository;

    public AccountAdapter(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<AccountDTO> save(AccountDTO account) {
        return accountRepository.save(AccountEntityMapper.mapToEntity(account))
                .map(AccountEntityMapper::mapToDTO);
    }

    @Override
    public Mono<AccountDTO> findByNumber(String number) {
        return accountRepository.findByAccountNumber(number)
                .map(AccountEntityMapper::mapToDTO);
    }

    @Override
    public Flux<AccountDTO> findAll() {
        return accountRepository.findAll()
                .map(AccountEntityMapper::mapToDTO);
    }

}