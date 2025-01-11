package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;

public class AccountSavedViewUseCase implements IUseCaseAccept<AccountDTO> {
    private final AccountRepository accountRepository;

    public AccountSavedViewUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void accept(AccountDTO accountDTO) {
        accountRepository.save(accountDTO)
                .subscribe();
    }

}