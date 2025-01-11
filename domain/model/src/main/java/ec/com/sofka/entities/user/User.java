package ec.com.sofka.entities.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.com.sofka.entities.user.values.UserId;
import ec.com.sofka.entities.user.values.objects.Email;
import ec.com.sofka.entities.user.values.objects.Password;
import ec.com.sofka.generics.shared.Entity;
import ec.com.sofka.utils.enums.RoleEnum;

public class User extends Entity<UserId> {

    private String firstName;

    private String lastName;

    private Email email;

    private Password password;

    private RoleEnum role;

    @JsonCreator
    public User(
            @JsonProperty("id") UserId userId,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") Email email,
            @JsonProperty("password") Password password,
            @JsonProperty("role") RoleEnum role
    ) {
        super(userId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

}