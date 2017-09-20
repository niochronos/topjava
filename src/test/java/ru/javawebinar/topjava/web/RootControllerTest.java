package ru.javawebinar.topjava.web;

import org.junit.Test;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

import static ru.javawebinar.topjava.MealTestData.*;

public class RootControllerTest extends AbstractControllerTest {

    @Test
    public void testUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    public void testMeals() throws Exception {
        List<MealWithExceed> mealWithExceeds = MealsUtil.getFilteredWithExceeded(MEALS, MEAL1.getTime(),
                MEAL1.getTime(), MealsUtil.DEFAULT_CALORIES_PER_DAY);

        MealWithExceed mealWithExceed = !mealWithExceeds.isEmpty() ? mealWithExceeds.get(0) : null;

        if (mealWithExceed == null) throw new Exception("mealWithExceed == null");

        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", hasItem(
                        allOf(
                                hasProperty("id", is(mealWithExceed.getId())),
                                hasProperty("dateTime", is(mealWithExceed.getDateTime())),
                                hasProperty("description", is(mealWithExceed.getDescription())),
                                hasProperty("calories", is(mealWithExceed.getCalories())),
                                hasProperty("exceed", is(mealWithExceed.isExceed()))
                        ))));
    }
}