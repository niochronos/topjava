package ru.javawebinar.topjava.web.json;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.View;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class JsonUtilTest {

    @Test
    public void testReadWriteValue() throws Exception {
        String json = JsonUtil.writeValue(MealTestData.ADMIN_MEAL1);
        System.out.println(json);
        Meal meal = JsonUtil.readValue(json, Meal.class);
        MealTestData.MATCHER.assertEquals(MealTestData.ADMIN_MEAL1, meal);
    }

    @Test
    public void testReadWriteValues() throws Exception {
        String json = JsonUtil.writeValue(MealTestData.MEALS);
        System.out.println(json);
        List<Meal> meals = JsonUtil.readValues(json, Meal.class);
        MealTestData.MATCHER.assertListEquals(MealTestData.MEALS, meals);
    }

    @Test
    public void testWriteWithView() throws Exception {
        ObjectWriter uiWriter = JacksonObjectMapper.getMapper().writerWithView(View.JsonUI.class);
        String json = JsonUtil.writeValue(MealTestData.ADMIN_MEAL1, uiWriter);
        System.out.println(json);
        assertThat(json, containsString("dateTimeUI"));
    }

    @Test
    public void testWriteOnlyAccess() throws Exception {
        String json = JsonUtil.writeValue(UserTestData.USER);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        User user = JsonUtil.readValue(
                UserTestData.JSON_NEW_USER_WITH_PASSWORD, User.class);
        System.out.println(user.getPassword());
        Assert.assertNotNull(user.getPassword());
    }
}