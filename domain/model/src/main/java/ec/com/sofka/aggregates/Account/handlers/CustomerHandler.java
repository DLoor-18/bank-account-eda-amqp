package ec.com.sofka.aggregates.Account.handlers;

import ec.com.sofka.aggregates.Account.AccountAggregate;
import ec.com.sofka.aggregates.Account.events.CustomerCreated;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.aggregates.Account.entities.customer.Customer;
import ec.com.sofka.aggregates.Account.entities.customer.values.CustomerId;
import ec.com.sofka.aggregates.Account.entities.customer.values.objects.IdentityCard;

public class CustomerHandler extends DomainActionsContainer {

    public CustomerHandler(AccountAggregate accountAggregate) {
        addDomainActions((CustomerCreated event) -> {
            Customer customer = new Customer(
                    CustomerId.of(event.getCustomerId()),
                    event.getFirstName(),
                    event.getLastName(),
                    IdentityCard.of(event.getIdentityCard()),
                    event.getStatus());

            accountAggregate.setCustomer(customer);
        });
    }

}