package ec.com.sofka.events;

import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.enums.EventsDetailsEnum;
import ec.com.sofka.utils.enums.RoleEnum;

public class UserUpdated extends DomainEvent {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleEnum role;

    public UserUpdated(String userId, String firstName, String lastName, String email, String password, RoleEnum role) {
        super(EventsDetailsEnum.USER_UPDATED.getEventType());
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserUpdated() {
        super(EventsDetailsEnum.USER_UPDATED.getEventType());
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public RoleEnum getRole() {
        return role;
    }

}