package ec.com.sofka;

import ec.com.sofka.database.account.IErrorLogRepository;
import ec.com.sofka.gateway.ErrorLogRepository;
import ec.com.sofka.mapper.ErrorLogEntityMapper;
import ec.com.sofka.model.ErrorMessage;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ErrorLogAdapter implements ErrorLogRepository {
    private final IErrorLogRepository repository;

    public ErrorLogAdapter(IErrorLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<ErrorMessage> save(ErrorMessage errorMessage) {
        return repository.save(ErrorLogEntityMapper.mapToEntity(errorMessage))
                .map(ErrorLogEntityMapper::mapToModel);
    }
}