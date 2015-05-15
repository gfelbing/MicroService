package de.gfelbing.microservice.core.util;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Utility class for collecting Java 8 - Streams into Guava Collections.
 */
public final class GuavaCollect {

    private GuavaCollect() {
    }

    /**
     * @param <T> Will be set by the type of the Stream you are using with this Collector.
     * @return A collector for converting a Stream\<T\> into a ImmutableList\<T\>
     */
    public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> immutableList() {
        return new ImmutableCollector<T, ImmutableList<T>, ImmutableList.Builder<T>>(ImmutableList.Builder::new);
    }

    /**
     * @param <T> Will be set by the type of the Stream you are using with this Collector.
     * @return A collector for converting a Stream\<T\> into a ImmutableList\<T\>
     */
    public static <T> Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> immutableSet() {
        return new ImmutableCollector<T, ImmutableSet<T>, ImmutableSet.Builder<T>>(ImmutableSet.Builder::new);
    }

    /**
     * Collects all Elements of an java.util.Stream and builds an guava ImmutableCollection.
     *
     * @param <T> The element-type of the stream and list.
     */
    static final class ImmutableCollector
            <T, C extends ImmutableCollection<T>, B extends ImmutableCollection.Builder<T>>
            implements Collector<T, B, C> {

        private final Supplier<B> supplier;

        public ImmutableCollector(final Supplier<B> supplier) {
            this.supplier = supplier;
        }

        @Override
        public Supplier<B> supplier() {
            return supplier;
        }

        @Override
        public BiConsumer<B, T> accumulator() {
            return (builder, t) -> builder.add(t);
        }

        @Override
        public BinaryOperator<B> combiner() {
            return (left, right) -> {
                left.addAll(right.build());
                return left;
            };
        }

        @Override
        public Function<B, C> finisher() {
            return (b -> (C) b.build());
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.UNORDERED);
        }

    }

}
