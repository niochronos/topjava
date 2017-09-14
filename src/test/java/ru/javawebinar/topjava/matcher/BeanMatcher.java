package ru.javawebinar.topjava.matcher;

import org.junit.Assert;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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

    private Class<T> entityClass;

    public interface Equality<T> {
        boolean areEqual(T expected, T actual);
    }

    private BeanMatcher(Class<T> entityClass, Equality<T> equality) {
        this.entityClass = entityClass;
        this.equality = equality;
    }

    public static <T> BeanMatcher<T> of(Class<T> entityClass) {
        return of(entityClass,
                (T expected, T actual) -> expected == actual || String.valueOf(expected).equals(String.valueOf(actual)));
    }

    public static <T> BeanMatcher<T> of(Class<T> entityClass, Equality<T> equality) {
        return new BeanMatcher<>(entityClass, equality);
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

    private T fromJsonValue(String json) {
        return JsonUtil.readValue(json, entityClass);
    }

    private List<T> fromJsonValues(String json) {
        return JsonUtil.readValues(json, entityClass);
    }

    public T fromJsonAction(ResultActions action) throws UnsupportedEncodingException {
        return fromJsonValue(TestUtil.getContent(action));
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

    public ResultMatcher contentMatcher(T expect) {
        return content().string(
                new TestMatcher<T>(expect) {
                    @Override
                    protected boolean compare(T expected, String body) {
                        Wrapper expectedForCompare = wrap(expected);
                        Wrapper actualForCompare = wrap(fromJsonValue(body));
                        return expectedForCompare.equals(actualForCompare);
                    }
                });
    }

    @SafeVarargs
    public final ResultMatcher contentListMatcher(T... expected) {
        return contentListMatcher(Arrays.asList(expected));
    }

    public final ResultMatcher contentListMatcher(List<T> expected) {
        return content().string(
                new TestMatcher<List<T>>(expected) {
                    @Override
                    protected boolean compare(List<T> expected, String actual) {
                        List<Wrapper> expectedList = wrap(expected);
                        List<Wrapper> actualList = wrap(fromJsonValues(actual));
                        return expectedList.equals(actualList);
                    }
                });
    }
}
