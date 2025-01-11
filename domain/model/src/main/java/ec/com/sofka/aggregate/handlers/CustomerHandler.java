package ec.com.sofka.aggregate.handlers;

import ec.com.sofka.aggregate.AccountAggregate;
import ec.com.sofka.aggregate.events.CustomerCreated;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.aggregate.entities.customer.Customer;
import ec.com.sofka.aggregate.entities.customer.values.CustomerId;
import ec.com.sofka.aggregate.entities.customer.values.objects.IdentityCard;

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