package ec.com.sofka.queries.query.customer;

import ec.com.sofka.aggregates.Account.AccountAggregate;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseGetElement;
import ec.com.sofka.mapper.CustomerMapper;
import ec.com.sofka.generics.shared.GetElementQuery;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.responses.CustomerResponse;
import reactor.core.publisher.Mono;

public class FindCustomerByCiUseCase implements IUseCaseGetElement<GetElementQuery, CustomerResponse> {
    private final CustomerRepository customerRepository;
    private final IEventStore eventStore;
    private final ErrorBusMessage errorBusMessage;

    public FindCustomerByCiUseCase(CustomerRepository customerRepository, IEventStore eventStore, ErrorBusMessage errorBusMessage) {
        this.customerRepository = customerRepository;
        this.eventStore = eventStore;
        this.errorBusMessage = errorBusMessage;
    }

    @Override
    public Mono<CustomerResponse> get(GetElementQuery request) {

        return eventStore.findAggregate(request.getAggregateId())
                .switchIfEmpty(Mono.defer(() -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Customer not found", "Get Customer by Id"));
                    return Mono.error(new RecordNotFoundException("Customer not found."));
                }))
                .collectList()
                .map(eventsList -> AccountAggregate.from(request.getAggregateId(), eventsList))
                .flatMap(accountAggregate ->
                        customerRepository.findByIdentityCard(accountAggregate.getCustomer().getIdentityCard().getValue())
                                .map(CustomerMapper::mapToResponseFromDTO)
                );

    }

}