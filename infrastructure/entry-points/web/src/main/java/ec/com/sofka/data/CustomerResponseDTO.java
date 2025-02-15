package ec.com.sofka.data;

import java.io.Serializable;

public class CustomerResponseDTO implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String identityCard;
    private String status;

    public CustomerResponseDTO() {
    }

    public CustomerResponseDTO(String id, String firstName, String lastName, String identityCard, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}