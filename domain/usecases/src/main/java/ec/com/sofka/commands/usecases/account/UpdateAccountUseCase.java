package ec.com.sofka.commands.usecases.account;

import ec.com.sofka.aggregates.account.AccountAggregate;
import ec.com.sofka.commands.AccountEntityCommand;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.queries.responses.AccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UpdateAccountUseCase implements IUseCaseExecute<AccountEntityCommand, AccountResponse> {
    private final IEventStore repository;
    private final EventBusMessage eventBusMessage;

    public UpdateAccountUseCase(IEventStore repository, EventBusMessage eventBusMessage) {
        this.repository = repository;
        this.eventBusMessage = eventBusMessage;
    }

    @Override
    public Mono<AccountResponse> execute(AccountEntityCommand request) {

        return repository.findAggregateByEntityId(request.getEntityId())
                .collectList()
                .map(domainEvent -> AccountAggregate.from(domainEvent.get(0).getAggregateRootId(), domainEvent))
                .flatMap(accountAggregate -> {
                    accountAggregate.updateAccount(
                            accountAggregate.getAccount().getId().getValue(),
                            accountAggregate.getAccount().getAccountNumber().getValue(),
                            request.getBalance(),
                            request.getStatus(),
                            accountAggregate.getAccount().getCustomer()
                    );

                    return Flux.fromIterable(accountAggregate.getUncommittedEvents())
                            .flatMap(repository::save)
                            .doOnNext(eventBusMessage::sendEvent)
                            .then(Mono.fromCallable(() -> {
                                accountAggregate.markEventsAsCommitted();
                                return AccountMapper.mapToResponseFromModel(accountAggregate.getAccount());
                            }));
                })
                .switchIfEmpty(Mono.error(new RecordNotFoundException("Account not found.")));

    }

}