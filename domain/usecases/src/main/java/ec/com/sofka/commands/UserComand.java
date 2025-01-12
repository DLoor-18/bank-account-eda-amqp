package ec.com.sofka.commands;

import ec.com.sofka.generics.shared.Command;
import ec.com.sofka.utils.enums.RoleEnum;

public class UserComand extends Command {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleEnum role;

    public UserComand(String firstName, String lastName, String email, String password, RoleEnum role) {
        super(null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserComand(String aggregateId, String firstName, String lastName, String email, String password, RoleEnum role) {
        super(aggregateId);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

}