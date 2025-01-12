package ec.com.sofka.aggregates.Auth.values;

import ec.com.sofka.generics.shared.Identity;

public class AuthAggergateId  extends Identity {

    public AuthAggergateId() {
        super();
    }

    public AuthAggergateId(String id) {
        super(id);
    }

    public static AuthAggergateId of(String id) {
        return new AuthAggergateId(id);
    }

}