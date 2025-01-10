package ec.com.sofka;

import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.aggregate.events.TransactionTypeCreated;
import ec.com.sofka.aggregate.events.UserCreated;
import ec.com.sofka.gateway.busMessage.BusEventListener;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.mapper.UserMapper;
import ec.com.sofka.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.queries.usecases.TransactionSavedViewUseCase;
import ec.com.sofka.queries.usecases.TransactionTypeSavedViewUseCase;
import ec.com.sofka.queries.usecases.UserSavedViewUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EventBusListener implements BusEventListener {
    private final UserSavedViewUseCase userSavedViewUseCase;
    private final AccountSavedViewUseCase accountSavedViewUseCase;
    private final TransactionTypeSavedViewUseCase transactionTypeSavedViewUseCase;
    private final TransactionSavedViewUseCase transactionSavedViewUseCase;

    public EventBusListener(UserSavedViewUseCase userSavedViewUseCase, AccountSavedViewUseCase accountSavedViewUseCase, TransactionTypeSavedViewUseCase transactionTypeSavedViewUseCase, TransactionSavedViewUseCase transactionSavedViewUseCase) {
        this.userSavedViewUseCase = userSavedViewUseCase;
        this.accountSavedViewUseCase = accountSavedViewUseCase;
        this.transactionTypeSavedViewUseCase = transactionTypeSavedViewUseCase;
        this.transactionSavedViewUseCase = transactionSavedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "${user.created.queue}")
    public void receiveUserCreate(DomainEvent event) {
        UserCreated userEvent = (UserCreated) event;
        userSavedViewUseCase.accept(UserMapper.mapToDTOFromEvent(userEvent));

    }

    @Override
    @RabbitListener(queues = "${account.created.queue}")
    public void receiveAccountCreate(DomainEvent event) {
        AccountCreated accountEvent = (AccountCreated) event;
        accountSavedViewUseCase.accept(AccountMapper.mapToDTOFromCreatedEvent(accountEvent));

    }

    @Override
    @RabbitListener(queues = "${account.updated.queue}")
    public void receiveAccountUpdate(DomainEvent event) {
        AccountUpdated accountEvent = (AccountUpdated) event;
        accountSavedViewUseCase.accept(AccountMapper.mapToDTOFromUpdatesEvent(accountEvent));

    }

    @Override
    @RabbitListener(queues = "${transactionType.created.queue}")
    public void receiveTransactionTypeCreate(DomainEvent event) {
        TransactionTypeCreated transactionTypeEvent = (TransactionTypeCreated) event;
        transactionTypeSavedViewUseCase.accept(TransactionTypeMapper.mapToDTOFromEvent(transactionTypeEvent));

    }

    @Override
    @RabbitListener(queues = "${transaction.created.queue}")
    public void receiveTransactionCreate(DomainEvent event) {
        TransactionCreated transactionEvent = (TransactionCreated) event;
        transactionSavedViewUseCase.accept(TransactionMapper.mapToDTOFromEvent(transactionEvent));

    }

}