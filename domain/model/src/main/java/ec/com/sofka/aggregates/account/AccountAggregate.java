package ec.com.sofka.aggregates.account;

import ec.com.sofka.aggregates.account.entities.account.values.AccountId;
import ec.com.sofka.aggregates.account.handlers.AccountHandler;
import ec.com.sofka.aggregates.account.handlers.TransactionHandler;
import ec.com.sofka.aggregates.account.handlers.TransactionTypeHandler;
import ec.com.sofka.aggregates.account.handlers.CustomerHandler;
import ec.com.sofka.aggregates.account.values.AccountAggregateId;
import ec.com.sofka.events.*;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.shared.AggregateRoot;
import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.aggregates.account.entities.transaction.values.TransactionId;
import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;
import ec.com.sofka.aggregates.account.entities.transactionType.values.TransactionTypeId;
import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.aggregates.account.entities.customer.values.CustomerId;
import ec.com.sofka.utils.enums.StatusEnum;

import java.math.BigDecimal;
import java.util.List;

public class AccountAggregate extends AggregateRoot<AccountAggregateId> {
    private Customer customer;
    private TransactionType transactionType;
    private Account account;
    private Transaction transaction;

    public AccountAggregate() {
        super(new AccountAggregateId());
        setSubscription(new CustomerHandler(this));
        setSubscription(new TransactionTypeHandler(this));
        setSubscription(new AccountHandler(this));
        setSubscription(new TransactionHandler(this));
    }

    public AccountAggregate(final String id) {
        super(AccountAggregateId.of(id));
        setSubscription(new CustomerHandler(this));
        setSubscription(new TransactionTypeHandler(this));
        setSubscription(new AccountHandler(this));
        setSubscription(new TransactionHandler(this));
    }

    public void createCustomer(String firstName, String lastName, String identityCard, StatusEnum statusEnum) {
        CustomerId customerId = new CustomerId();
        addEvent(new CustomerCreated(customerId.getValue(), firstName, lastName, identityCard, statusEnum), customerId.getValue()).apply();
    }

    public void createTransactionType(String type, String description, BigDecimal value, Boolean transactionCost, Boolean discount, StatusEnum statusEnum) {
        TransactionTypeId transactionTypeId = new TransactionTypeId();
        addEvent(new TransactionTypeCreated(transactionTypeId.getValue(), type, description, value, transactionCost, discount, statusEnum), transactionTypeId.getValue()).apply();
    }

    public void createAccount(String accountNumber, BigDecimal balance, StatusEnum statusEnum, Customer customer) {
        AccountId accountId = new AccountId();
        addEvent(new AccountCreated(accountId.getValue(), accountNumber, balance, statusEnum, customer), accountId.getValue()).apply();
    }

    public void updateAccount(String accountId, String accountNumber, BigDecimal balance, StatusEnum statusEnum, Customer customer) {
        AccountId accountUpdateId = AccountId.of(accountId);
        addEvent(new AccountUpdated(accountUpdateId.getValue(), accountNumber, balance, statusEnum, customer), accountUpdateId.getValue()).apply();
    }

    public void createTransaction(String transactionAccount, String details, BigDecimal amount, String processingDate, Account account, TransactionType transactionType) {
        TransactionId transactionId = new TransactionId();
        addEvent(new TransactionCreated(transactionId.getValue(), transactionAccount, details, amount, processingDate, account, transactionType), transactionId.getValue()).apply();
    }

    public static AccountAggregate from(final String id, List<DomainEvent> events) {
        AccountAggregate accountAggregate = new AccountAggregate(id);
        events.stream()
                .filter(event -> id.equals(event.getAggregateRootId()))
                .reduce((first, second) -> second)
                .ifPresent(event -> accountAggregate.addEvent(event, null).apply());
        accountAggregate.markEventsAsCommitted();
        return accountAggregate;
    }

    public static AccountAggregate fromByEntityId(final String id, final String entityId, List<DomainEvent> events) {
        AccountAggregate accountAggregate = new AccountAggregate(id);
        events.stream()
                .filter(event -> entityId.equals(event.getEntityId()))
                .reduce((first, second) -> second)
                .ifPresent(event -> accountAggregate.addEvent(event, entityId).apply());
        accountAggregate.markEventsAsCommitted();
        return accountAggregate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}