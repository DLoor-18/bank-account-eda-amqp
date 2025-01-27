package ec.com.sofka.mapper;

import ec.com.sofka.data.TransactionTypeEntity;
import ec.com.sofka.gateway.dto.TransactionTypeDTO;
import org.springframework.stereotype.Component;

@Component
public class TransactionTypeEntityMapper {

    public static TransactionTypeEntity mapToEntity(TransactionTypeDTO transactionType) {
        if (transactionType == null) {
            return null;
        }
        return new TransactionTypeEntity(
                transactionType.getId(),
                transactionType.getType(),
                transactionType.getDescription(),
                transactionType.getValue(),
                transactionType.getTransactionCost(),
                transactionType.getDiscount(),
                transactionType.getStatus());
    }

    public static TransactionTypeDTO mapToDTO(TransactionTypeEntity transactionTypeEntity) {
        if (transactionTypeEntity == null) {
            return null;
        }
        return new TransactionTypeDTO(
                transactionTypeEntity.getId(),
                transactionTypeEntity.getType(),
                transactionTypeEntity.getDescription(),
                transactionTypeEntity.getValue(),
                transactionTypeEntity.getTransactionCost(),
                transactionTypeEntity.getDiscount(),
                transactionTypeEntity.getStatus()
        );
    }
}