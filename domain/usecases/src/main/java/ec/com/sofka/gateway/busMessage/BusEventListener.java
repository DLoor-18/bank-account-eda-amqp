package ec.com.sofka.gateway.busMessage;

import ec.com.sofka.generics.domain.DomainEvent;

public interface BusEventListener {

    void receiveUserCreate(DomainEvent event);
    void receiveAccountCreate(DomainEvent event);
    void receiveAccountUpdate(DomainEvent event);
    void receiveTransactionTypeCreate(DomainEvent event);
    void receiveTransactionCreate(DomainEvent event);

}