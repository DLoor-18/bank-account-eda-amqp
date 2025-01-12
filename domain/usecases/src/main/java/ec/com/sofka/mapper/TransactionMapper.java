package ec.com.sofka.mapper;

import ec.com.sofka.aggregates.Account.events.TransactionCreated;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.aggregates.Account.entities.transaction.Transaction;
import ec.com.sofka.queries.responses.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public static TransactionResponse mapToResponseFromModel(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionResponse(
                transaction.getTransactionAccount().getValue(),
                transaction.getDetails(),
                transaction.getAmount().getValue(),
                transaction.getProcessingDate().getValue(),
                AccountMapper.mapToResponseFromModel(transaction.getAccount()),
                TransactionTypeMapper.mapToResponseFromModel(transaction.getTransactionType()));
    }

    public static TransactionResponse mapToResponseFromDTO(TransactionDTO transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionResponse(
                transaction.getAccountNumber(),
                transaction.getDetails(),
                transaction.getAmount(),
                transaction.getProcessingDate(),
                AccountMapper.mapToResponseFromDTO(transaction.getAccount()),
                TransactionTypeMapper.mapToResponseFromDTO(transaction.getTransactionType()));
    }


    public static TransactionDTO mapToDTOFromEvent(TransactionCreated transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionDTO(
                transaction.getTransactionId(),
                transaction.getAccountNumber(),
                transaction.getDetails(),
                transaction.getAmount(),
                transaction.getProcessingDate(),
                AccountMapper.mapToDTOFromModel(transaction.getAccount()),
                TransactionTypeMapper.mapToDTOFromModel(transaction.getTransactionType()));
    }

}