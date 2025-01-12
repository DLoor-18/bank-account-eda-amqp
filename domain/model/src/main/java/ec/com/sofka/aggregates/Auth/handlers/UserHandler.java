package ec.com.sofka.aggregates.Auth.handlers;

import ec.com.sofka.aggregates.Auth.AuthAggregate;
import ec.com.sofka.aggregates.Auth.entities.user.User;
import ec.com.sofka.aggregates.Auth.entities.user.values.UserId;
import ec.com.sofka.aggregates.Auth.entities.user.values.objects.Email;
import ec.com.sofka.aggregates.Auth.entities.user.values.objects.Password;
import ec.com.sofka.aggregates.Auth.events.UserCreated;
import ec.com.sofka.aggregates.Auth.events.UserUpdated;
import ec.com.sofka.generics.domain.DomainActionsContainer;

public class UserHandler extends DomainActionsContainer {

    public UserHandler(AuthAggregate authAggregate) {
        addDomainActions((UserCreated event) -> {
            User user = new User(
                    UserId.of(event.getUserId()),
                    event.getFirstName(),
                    event.getLastName(),
                    Email.of(event.getEmail()),
                    Password.of(event.getPassword()),
                    event.getRole());

            authAggregate.setUser(user);
        });

        addDomainActions((UserUpdated event) -> {
            User user = new User(
                    UserId.of(event.getUserId()),
                    event.getFirstName(),
                    event.getLastName(),
                    Email.of(event.getEmail()),
                    Password.of(event.getPassword()),
                    event.getRole());

            authAggregate.setUser(user);
        });
    }
}
