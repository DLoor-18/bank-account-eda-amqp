package ec.com.sofka.queries.query.customer;

import ec.com.sofka.aggregate.AccountAggregate;
import ec.com.sofka.aggregate.entities.customer.Customer;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.mapper.CustomerMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.generics.shared.GetElementQuery;
import ec.com.sofka.queries.responses.CustomerResponse;
import reactor.core.publisher.Mono;

public class FindCustomerByIdUseCase implements IUseCaseGetElement<GetElementQuery, CustomerResponse> {
    private final CustomerRepository customerRepository;
    private final IEventStore eventStore;
    private final ErrorBusMessage errorBusMessage;

    public FindCustomerByIdUseCase(CustomerRepository customerRepository, IEventStore eventStore, ErrorBusMessage errorBusMessage) {
        this.customerRepository = customerRepository;
        this.eventStore = eventStore;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<CustomerResponse> get(GetElementQuery request) {
        return getUserByAggregate(request.getAggregateId())
                .map(CustomerMapper::mapToResponseFromModel);
    }

    public Mono<Customer> getUserByAggregate(String aggregateId) {
        return eventStore.findAggregate(aggregateId)
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("User not found", "Get User by Id"));
                    return Mono.error(new RecordNotFoundException("User not found."));
                }))
                .collectList()
                .map(eventsList -> AccountAggregate.from(aggregateId, eventsList).getCustomer());
    }

}