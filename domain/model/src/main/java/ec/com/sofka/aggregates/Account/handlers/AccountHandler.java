package ec.com.sofka.aggregates.Account.handlers;

import ec.com.sofka.aggregates.Account.AccountAggregate;
import ec.com.sofka.aggregates.Account.events.AccountCreated;
import ec.com.sofka.aggregates.Account.events.AccountUpdated;
import ec.com.sofka.aggregates.Account.values.objects.Amount;
import ec.com.sofka.exceptions.InvalidFieldException;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.aggregates.Account.entities.account.Account;
import ec.com.sofka.aggregates.Account.entities.account.values.AccountId;
import ec.com.sofka.aggregates.Account.entities.account.values.objects.AccountNumber;

public class AccountHandler extends DomainActionsContainer {

    public AccountHandler(AccountAggregate accountAggregate) {
        addDomainActions((AccountCreated event) -> {
                Account account = new Account(
                        AccountId.of(event.getAccountId()),
                        AccountNumber.of(event.getAccountNumber()),
                        Amount.of(event.getBalance()),
                        event.getStatus(),
                        event.getUser());

                accountAggregate.setAccount(account);
        });

        addDomainActions((AccountUpdated event) -> {
            Account account = new Account(
                    AccountId.of(event.getAccountId()),
                    AccountNumber.of(event.getAccountNumber()),
                    Amount.of(event.getBalance()),
                    event.getStatus(),
                    event.getUser());

            accountAggregate.setAccount(account);
        });
    }

}