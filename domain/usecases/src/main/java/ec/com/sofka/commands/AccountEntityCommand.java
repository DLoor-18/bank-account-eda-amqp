package ec.com.sofka.commands;

import ec.com.sofka.generics.shared.EntityCommand;
import ec.com.sofka.utils.enums.StatusEnum;

import java.math.BigDecimal;

public class AccountEntityCommand  extends EntityCommand {
    private String accountNumber;
    private BigDecimal balance;
    private StatusEnum status;
    private String customerId;

    public AccountEntityCommand(String entityId, String accountNumber, BigDecimal balance, StatusEnum status, String customerId) {
        super(entityId);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customerId = customerId;
    }

    public AccountEntityCommand(String accountNumber, BigDecimal balance, StatusEnum status, String customerId) {
        super(null);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerAggregateId) {
        this.customerId = customerAggregateId;
    }
}