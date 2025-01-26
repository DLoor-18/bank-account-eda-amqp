package ec.com.sofka.handlers.customer;

import ec.com.sofka.data.CustomerResponseDTO;
import ec.com.sofka.mapper.CustomerModelMapper;
import ec.com.sofka.queries.query.customer.GetAllCustomerUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GetAllCustomersHandler {
    private final GetAllCustomerUseCase getAllAccountUseCase;

    public GetAllCustomersHandler(GetAllCustomerUseCase getAllAccountUseCase) {
        this.getAllAccountUseCase = getAllAccountUseCase;
    }

    public Flux<CustomerResponseDTO> getAll() {
        return getAllAccountUseCase.get()
                .flatMap(queryResponse -> Flux.fromIterable(queryResponse.getMultipleResults()))
                .map(CustomerModelMapper::mapToDTO);

    }
}