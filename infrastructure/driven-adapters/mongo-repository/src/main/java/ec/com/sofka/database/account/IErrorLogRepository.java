package ec.com.sofka.database.account;

import ec.com.sofka.data.ErrorLogEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IErrorLogRepository extends ReactiveMongoRepository<ErrorLogEntity, String> {

}