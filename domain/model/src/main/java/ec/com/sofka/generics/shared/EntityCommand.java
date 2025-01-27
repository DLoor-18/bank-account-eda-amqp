package ec.com.sofka.generics.shared;

import ec.com.sofka.generics.interfaces.ICommandBase;

public abstract class EntityCommand implements ICommandBase {
    private final String entityId;

    protected EntityCommand(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }
}