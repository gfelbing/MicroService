package de.gfelbing.microservice.service.util;

import com.google.common.collect.ImmutableList;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Collects all Elements of an java.util.Stream and builds an guava ImmutableList.
 * @param <T> The element-type of the stream and list.
 */
public final class ImmutableListCollector<T> implements Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> {

    @Override
    public Supplier<ImmutableList.Builder<T>> supplier() {
        return (() -> ImmutableList.builder());
    }

    @Override
    public BiConsumer<ImmutableList.Builder<T>, T> accumulator() {
        return (builder, t) -> builder.add(t);
    }

    @Override
    public BinaryOperator<ImmutableList.Builder<T>> combiner() {
        return (left, right) -> {
            left.addAll(right.build());
            return left;
        };
    }

    @Override
    public Function<ImmutableList.Builder<T>, ImmutableList<T>> finisher() {
        return (b -> b.build());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }

}
