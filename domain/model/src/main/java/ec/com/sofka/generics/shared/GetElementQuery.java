package ec.com.sofka.generics.shared;

public class GetElementQuery extends Request {
    private final String element;

    public GetElementQuery(final String aggregateId, final String element) {
        super(aggregateId);
        this.element = element;
    }

    public String getElement() {
        return element;
    }

}