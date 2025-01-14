package ec.com.sofka.aggregates.account.handlers;

import ec.com.sofka.aggregates.account.AccountAggregate;
import ec.com.sofka.events.AccountCreated;
import ec.com.sofka.events.AccountUpdated;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.aggregates.account.entities.account.values.AccountId;
import ec.com.sofka.aggregates.account.entities.account.values.objects.AccountNumber;

public class AccountHandler extends DomainActionsContainer {

    public AccountHandler(AccountAggregate accountAggregate) {
        addDomainActions((AccountCreated event) -> {
                Account account = new Account(
                        AccountId.of(event.getAccountId()),
                        AccountNumber.of(event.getAccountNumber()),
                        Amount.of(event.getBalance()),
                        event.getStatus(),
                        event.getCustomer());

                accountAggregate.setAccount(account);
        });

        addDomainActions((AccountUpdated event) -> {
            Account account = new Account(
                    AccountId.of(event.getAccountId()),
                    AccountNumber.of(event.getAccountNumber()),
                    Amount.of(event.getBalance()),
                    event.getStatus(),
                    event.getCustomer());

            accountAggregate.setAccount(account);
        });
    }

}