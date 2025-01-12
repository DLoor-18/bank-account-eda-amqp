package ec.com.sofka.aggregates.Account.handlers;

import ec.com.sofka.aggregates.Account.AccountAggregate;
import ec.com.sofka.aggregates.Account.events.TransactionTypeCreated;
import ec.com.sofka.aggregates.Account.values.objects.Amount;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.aggregates.Account.entities.transactionType.TransactionType;
import ec.com.sofka.aggregates.Account.entities.transactionType.values.TransactionTypeId;

public class TransactionTypeHandler extends DomainActionsContainer {

    public TransactionTypeHandler(AccountAggregate accountAggregate) {
        addDomainActions((TransactionTypeCreated event) -> {
            TransactionType transactionType = new TransactionType(
                    TransactionTypeId.of(event.getTransactionTypeId()),
                    event.getType(),
                    event.getDescription(),
                    Amount.of(event.getValue()),
                    event.getTransactionCost(),
                    event.getDiscount(),
                    event.getStatus());

            accountAggregate.setTransactionType(transactionType);
        });
    }

}