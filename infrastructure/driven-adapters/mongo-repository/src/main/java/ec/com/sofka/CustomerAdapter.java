package ec.com.sofka;

import ec.com.sofka.database.account.ICustomerRepository;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.dto.CustomerDTO;
import ec.com.sofka.mapper.CustomerEntityMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerAdapter implements CustomerRepository {
    private final ICustomerRepository customerRepository;

    public CustomerAdapter(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<CustomerDTO> save(CustomerDTO customer) {
        return customerRepository.save(CustomerEntityMapper.mapToEntity(customer))
                .map(CustomerEntityMapper::mapToDTO);
    }

    @Override
    public Flux<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .map(CustomerEntityMapper::mapToDTO);
    }

    @Override
    public Mono<CustomerDTO> findById(String id) {
        return customerRepository.findById(id)
                .map(CustomerEntityMapper::mapToDTO);
    }

    @Override
    public Mono<CustomerDTO> findByIdentityCard(String identityCard) {
        return customerRepository.findByIdentityCard(identityCard)
                .map(CustomerEntityMapper::mapToDTO);
    }

}