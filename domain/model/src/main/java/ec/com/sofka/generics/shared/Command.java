package ec.com.sofka.generics.shared;

import ec.com.sofka.generics.interfaces.ICommandBase;

public abstract class Command implements ICommandBase {
    private final String aggregateId;

    protected Command(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateId() {
        return aggregateId;
    }
}
