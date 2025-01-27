package ec.com.sofka.aggregates.auth;

import ec.com.sofka.aggregates.auth.entities.user.User;
import ec.com.sofka.aggregates.auth.entities.user.values.UserId;
import ec.com.sofka.events.UserCreated;
import ec.com.sofka.events.UserUpdated;
import ec.com.sofka.aggregates.auth.handlers.UserHandler;
import ec.com.sofka.aggregates.auth.values.AuthAggregateId;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.shared.AggregateRoot;
import ec.com.sofka.utils.enums.RoleEnum;

import java.util.List;

public class AuthAggregate extends AggregateRoot<AuthAggregateId> {
    private User user;

    public AuthAggregate() {
        super(new AuthAggregateId());
        setSubscription(new UserHandler(this));
    }

    public AuthAggregate(final String id) {
        super(AuthAggregateId.of(id));
        setSubscription(new UserHandler(this));
    }

    public void createUser(String firstName, String lastName, String email, String password, RoleEnum role){
        UserId userId = new UserId();
        addEvent(new UserCreated(userId.getValue(), firstName, lastName, email, password, role), userId.getValue());
    }

    public void updateUser(String userId, String firstName, String lastName, String email, String password, RoleEnum role){
        UserId userUpdateId = UserId.of(userId);
        addEvent(new UserUpdated(userUpdateId.getValue(), firstName, lastName, email, password, role), userUpdateId.getValue());
    }

    public static AuthAggregate from(final String id, List<DomainEvent> events) {
        AuthAggregate authAggregate = new AuthAggregate(id);
        events.stream()
                .filter(event -> id.equals(event.getAggregateRootId()))
                .reduce((first, second) -> second)
                .ifPresent(event -> authAggregate.addEvent(event, "").apply());
        authAggregate.markEventsAsCommitted();
        return authAggregate;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}