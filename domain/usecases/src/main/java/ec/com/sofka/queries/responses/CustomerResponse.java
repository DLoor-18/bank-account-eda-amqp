package ec.com.sofka.queries.responses;

import ec.com.sofka.utils.enums.StatusEnum;

import java.io.Serializable;

public class CustomerResponse implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String identityCard;
    private StatusEnum status;

    public CustomerResponse(String id, String firstName, String lastName, String identityCard, StatusEnum status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCard = identityCard;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

}