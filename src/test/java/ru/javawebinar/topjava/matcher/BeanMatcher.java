package ru.javawebinar.topjava.matcher;

import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This test Matcher assert equality of beans and collections
 * <p>
 * It wraps every entity by Wrapper before apply to Assert.assertEquals
 * in order to check equality by custom equality.
 * Default equality is String.valueOf(entity)
 *
 * @param <T> : Bean type
 */
public class BeanMatcher<T> {

    private Equality<T> equality;

    public interface Equality<T> {
        boolean areEqual(T expected, T actual);
    }

    public BeanMatcher() {
        this((T expected, T actual) -> expected == actual || String.valueOf(expected).equals(String.valueOf(actual)));
    }

    public BeanMatcher(Equality<T> equality) {
        this.equality = equality;
    }

    private class Wrapper {
        private T entity;

        private Wrapper(T entity) {
            this.entity = entity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Wrapper that = (Wrapper) o;
            return entity != null ? equality.areEqual(entity, that.entity) : that.entity == null;
        }

        @Override
        public String toString() {
            return String.valueOf(entity);
        }
    }

    public void assertEquals(T expected, T actual) {
        Assert.assertEquals(wrap(expected), wrap(actual));
    }

    public void assertListEquals(List<T> expected, List<T> actual) {
        Assert.assertEquals(wrap(expected), wrap(actual));
    }

    private Wrapper wrap(T entity) {
        return new Wrapper(entity);
    }

    private List<Wrapper> wrap(List<T> list) {
        return list.stream().map(this::wrap).collect(Collectors.toList());
    }
}
