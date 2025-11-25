package me.lime.lightCore.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Fluent wrapper for Stream operations
 * @param <T> The element type
 */
public final class StreamUtil<T> {

    private Stream<T> stream;

    private StreamUtil(@Nullable Collection<T> source) {
        this.stream = source == null ? Stream.empty() : source.stream();
    }

    @NotNull
    public static <T> StreamUtil<T> from(@Nullable Collection<T> source) {
        return new StreamUtil<>(source);
    }

    @NotNull
    public StreamUtil<T> filter(@NotNull Predicate<? super T> predicate) {
        stream = stream.filter(predicate);
        return this;
    }

    @NotNull
    public StreamUtil<T> filterNotNull() {
        stream = stream.filter(Objects::nonNull);
        return this;
    }

    @NotNull
    public StreamUtil<T> distinct() {
        stream = stream.distinct();
        return this;
    }

    @NotNull
    public StreamUtil<T> distinctBy(@NotNull Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        stream = stream.filter(e -> seen.add(keyExtractor.apply(e)));
        return this;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <R> StreamUtil<R> map(@NotNull Function<? super T, ? extends R> mapper) {
        List<R> result = (List<R>) stream.map(mapper).toList();
        return StreamUtil.from(result);
    }

    @NotNull
    public <R> StreamUtil<R> flatMap(@NotNull Function<? super T, ? extends Collection<R>> mapper) {
        List<R> result = stream.flatMap(e -> {
            Collection<R> col = mapper.apply(e);
            return col == null ? Stream.empty() : col.stream();
        }).toList();

        return StreamUtil.from(result);
    }

    @NotNull
    public StreamUtil<T> sorted(@NotNull Comparator<? super T> comparator) {
        stream = stream.sorted(comparator);
        return this;
    }

    @NotNull
    public StreamUtil<T> reverse() {
        List<T> list = new ArrayList<>(stream.toList());
        Collections.reverse(list);
        stream = list.stream();
        return this;
    }

    @NotNull
    public StreamUtil<T> peek(@NotNull Consumer<? super T> action) {
        stream = stream.peek(action);
        return this;
    }

    @NotNull
    public StreamUtil<T> limit(long max) {
        stream = stream.limit(max);
        return this;
    }

    @NotNull
    public StreamUtil<T> skip(long n) {
        stream = stream.skip(n);
        return this;
    }

    @NotNull
    public StreamUtil<T> parallel() {
        stream = stream.parallel();
        return this;
    }

    @NotNull
    public StreamUtil<T> sequential() {
        stream = stream.sequential();
        return this;
    }

    @NotNull
    public <R, A> R collect(@NotNull Collector<? super T, A, R> collector) {
        return stream.collect(collector);
    }

    @NotNull
    public List<T> toList() {
        return stream.toList();
    }

    @NotNull
    public Set<T> toSet() {
        return stream.collect(Collectors.toSet());
    }

    @NotNull
    public <K> Map<K, List<T>> groupBy(@NotNull Function<? super T, ? extends K> classifier) {
        return stream.collect(Collectors.groupingBy(classifier));
    }

    @NotNull
    public <K, V> Map<K, V> toMap(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper, (a, b) -> b));
    }

    @NotNull
    public Optional<T> findFirst() {
        return stream.findFirst();
    }

    @NotNull
    public Optional<T> findAny() {
        return stream.findAny();
    }

    @NotNull
    public Optional<T> find(@NotNull Predicate<? super T> predicate) {
        return stream.filter(predicate).findFirst();
    }

    @NotNull
    public Optional<T> min(@NotNull Comparator<? super T> comparator) {
        return stream.min(comparator);
    }

    @NotNull
    public Optional<T> max(@NotNull Comparator<? super T> comparator) {
        return stream.max(comparator);
    }

    @NotNull
    public Optional<T> reduce(@NotNull BinaryOperator<T> accumulator) {
        return stream.reduce(accumulator);
    }

    public long count() {
        return stream.count();
    }

    public boolean anyMatch(@NotNull Predicate<? super T> predicate) {
        return stream.anyMatch(predicate);
    }

    public boolean allMatch(@NotNull Predicate<? super T> predicate) {
        return stream.allMatch(predicate);
    }

    public boolean noneMatch(@NotNull Predicate<? super T> predicate) {
        return stream.noneMatch(predicate);
    }

    public void forEach(@NotNull Consumer<? super T> action) {
        stream.forEach(action);
    }

    public void forEachParallel(@NotNull Consumer<? super T> action) {
        stream.parallel().forEach(action);
    }
}
