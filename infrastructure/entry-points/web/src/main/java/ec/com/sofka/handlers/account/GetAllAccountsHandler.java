package ec.com.sofka.handlers.account;

import ec.com.sofka.data.AccountResponseDTO;
import ec.com.sofka.mapper.AccountModelMapper;
import ec.com.sofka.queries.query.account.GetAllAccountUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GetAllAccountsHandler {
    private final GetAllAccountUseCase getAllAccountUseCase;

    public GetAllAccountsHandler(GetAllAccountUseCase getAllAccountUseCase) {
        this.getAllAccountUseCase = getAllAccountUseCase;
    }


    public Flux<AccountResponseDTO> getAll() {
        return getAllAccountUseCase.get()
                .flatMap(queryResponse -> Flux.fromIterable(queryResponse.getMultipleResults()))
                .map(AccountModelMapper::mapToDTO);

    }
}