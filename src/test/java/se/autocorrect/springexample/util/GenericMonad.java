package se.autocorrect.springexample.util;

import java.util.Objects;
import java.util.function.Function;

import jdk.jfr.Experimental;

@Experimental
public final class GenericMonad<T> implements Monad<T> {

    private final T value;

    private GenericMonad(T value) {
        this.value = value;
    }

    public static <T> GenericMonad<T> of(T value) {
        return new GenericMonad<>(value);
    }

    @Override
    public <U> Monad<U> flatMap(Function<T, Monad<U>> function) {
        return function.apply(value);
    }

    @Override
    public boolean equals(Object other) {

        if (this == other){
            return true;
        }

        if (other == null || getClass() != other.getClass()){
            return false;
        }

        GenericMonad<?> that = (GenericMonad<?>) other;

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
