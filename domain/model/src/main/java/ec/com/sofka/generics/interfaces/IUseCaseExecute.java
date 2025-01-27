package ec.com.sofka.generics.interfaces;

import org.reactivestreams.Publisher;

public interface IUseCaseExecute<T extends ICommandBase, R> {
    Publisher<R> execute(T request);
}