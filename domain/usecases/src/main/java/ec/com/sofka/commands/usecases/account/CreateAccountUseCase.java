package ec.com.sofka.commands.usecases.account;

import ec.com.sofka.aggregates.account.AccountAggregate;
import ec.com.sofka.exceptions.ConflictException;
import ec.com.sofka.queries.query.account.FindAccountByNumberUseCase;
import ec.com.sofka.queries.query.customer.FindCustomerByIdUseCase;
import ec.com.sofka.commands.AccountCommand;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.queries.responses.AccountResponse;
import ec.com.sofka.queries.responses.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateAccountUseCase implements IUseCaseExecute<AccountCommand, AccountResponse> {
    private final IEventStore repository;
    private final FindCustomerByIdUseCase findCustomerByIdUseCase;
    private final FindAccountByNumberUseCase findAccountByNumberUseCase;
    private final EventBusMessage eventBusMessage;

    public CreateAccountUseCase(IEventStore repository, FindCustomerByIdUseCase findCustomerByIdUseCase, FindAccountByNumberUseCase findAccountByNumberUseCase, EventBusMessage eventBusMessage) {
        this.repository = repository;
        this.findCustomerByIdUseCase = findCustomerByIdUseCase;
        this.findAccountByNumberUseCase = findAccountByNumberUseCase;
        this.eventBusMessage = eventBusMessage;
    }

    public Mono<AccountResponse> execute(AccountCommand accountCommand) {
        AccountAggregate accountAggregate = new AccountAggregate();

        return findAccountByNumberUseCase.getByNumberAccount(accountCommand.getAccountNumber())
                .flatMap(account -> Mono.<AccountResponse>error(new ConflictException("The number account is already registered.")))
                .switchIfEmpty( Mono.defer(() -> {
                    return findCustomerByIdUseCase.getById(accountCommand.getCustomerId())
                            .flatMap(customer -> {
                                accountAggregate.createAccount(
                                        accountCommand.getAccountNumber(),
                                        accountCommand.getBalance(),
                                        accountCommand.getStatus(),
                                        customer
                                );

                                return Flux.fromIterable(accountAggregate.getUncommittedEvents())
                                        .flatMap(repository::save)
                                        .doOnNext(eventBusMessage::sendEvent)
                                        .then(Mono.fromCallable(() -> {
                                            accountAggregate.markEventsAsCommitted();
                                            return AccountMapper.mapToResponseFromModel(accountAggregate.getAccount());
                                        }));
                            });
                }));

    }

}