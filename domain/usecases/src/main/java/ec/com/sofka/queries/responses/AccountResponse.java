package ec.com.sofka.queries.responses;

import ec.com.sofka.utils.enums.StatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountResponse implements Serializable {
    private String id;
    private String number;
    private BigDecimal balance;
    private StatusEnum status;
    private CustomerResponse customer;

    public AccountResponse(String id, String number, BigDecimal balance, StatusEnum status, CustomerResponse customer) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.status = status;
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public CustomerResponse getUser() {
        return customer;
    }

    public void setUser(CustomerResponse customer) {
        this.customer = customer;
    }

}