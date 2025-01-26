package ec.com.sofka.mapper;

import ec.com.sofka.events.AccountCreated;
import ec.com.sofka.events.AccountUpdated;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.aggregates.account.entities.account.values.AccountId;
import ec.com.sofka.aggregates.account.entities.account.values.objects.AccountNumber;
import ec.com.sofka.queries.responses.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public static Account mapToModelFromDTO(AccountDTO account) {
        if (account == null) {
            return null;
        }

        return new Account(
                AccountId.of(account.getId()),
                AccountNumber.of(account.getAccountNumber()),
                Amount.of(account.getBalance()),
                account.getStatus(),
                CustomerMapper.mapToModelFromDTO(account.getCustomer()));
    }

    public static AccountResponse mapToResponseFromModel(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountResponse(
                account.getId().getValue(),
                account.getAccountNumber().getValue(),
                account.getBalance().getValue(),
                account.getStatus(),
                CustomerMapper.mapToResponseFromModel(account.getCustomer()));
    }

    public static AccountResponse mapToResponseFromDTO(AccountDTO account) {
        if (account == null) {
            return null;
        }

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                CustomerMapper.mapToResponseFromDTO(account.getCustomer()));
    }

    public static AccountDTO mapToDTOFromModel(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountDTO(
                account.getId().getValue(),
                account.getAccountNumber().getValue(),
                account.getBalance().getValue(),
                account.getStatus(),
                account.getCustomer() != null ? CustomerMapper.mapToDTOFromModel(account.getCustomer()) : null);
    }

    public static AccountDTO mapToDTOFromCreatedEvent(AccountCreated account) {
        if (account == null) {
            return null;
        }

        return new AccountDTO(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                CustomerMapper.mapToDTOFromModel(account.getCustomer()));
    }

    public static AccountDTO mapToDTOFromUpdatesEvent(AccountUpdated account) {
        if (account == null) {
            return null;
        }

        return new AccountDTO(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                CustomerMapper.mapToDTOFromModel(account.getCustomer()));
    }

}