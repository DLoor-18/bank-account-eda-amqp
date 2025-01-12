package ec.com.sofka.aggregates.Account.entities.account.values;

import ec.com.sofka.generics.shared.Identity;

public class AccountId extends Identity {
    public AccountId() {
        super();
    }

    private AccountId(final String id) {
        super(id);
    }

    public static AccountId of(final String id) {
        return new AccountId(id);
    }

}