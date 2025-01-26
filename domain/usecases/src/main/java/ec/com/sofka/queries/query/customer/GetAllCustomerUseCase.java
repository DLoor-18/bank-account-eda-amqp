package ec.com.sofka.queries.query.customer;

import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.generics.interfaces.IUseCaseGet;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.CustomerMapper;
import ec.com.sofka.queries.responses.CustomerResponse;
import reactor.core.publisher.Flux;

public class GetAllCustomerUseCase implements IUseCaseGet<CustomerResponse> {
    private final CustomerRepository customerRepository;

    public GetAllCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Flux<QueryResponse<CustomerResponse>> get() {
        return getAll()
                .map(CustomerMapper::mapToResponseFromModel)
                .collectList()
                .flatMapMany(list -> {
                    QueryResponse<CustomerResponse> queryResponse = QueryResponse.ofMultiple(list);
                    return Flux.just(queryResponse);
                });
    }

    public Flux<Customer> getAll() {
        return customerRepository.findAll()
                .map(CustomerMapper::mapToModelFromDTO);

    }
}