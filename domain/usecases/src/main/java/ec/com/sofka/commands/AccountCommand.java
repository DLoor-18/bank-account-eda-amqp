package ec.com.sofka.commands;

import ec.com.sofka.generics.shared.Command;
import ec.com.sofka.utils.enums.StatusEnum;

import java.math.BigDecimal;

public class AccountCommand extends Command {
    private String accountNumber;
    private BigDecimal balance;
    private StatusEnum status;
    private String customerAggregateId;

    public AccountCommand(String aggregateId, String accountNumber, BigDecimal balance, StatusEnum status, String customerAggregateId) {
        super(aggregateId);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customerAggregateId = customerAggregateId;
    }

    public AccountCommand(String accountNumber, BigDecimal balance, StatusEnum status, String customerAggregateId) {
        super(null);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customerAggregateId = customerAggregateId;
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

    public String getUserAggregateId() {
        return customerAggregateId;
    }

    public void setUserAggregateId(String customerAggregateId) {
        this.customerAggregateId = customerAggregateId;
    }
}