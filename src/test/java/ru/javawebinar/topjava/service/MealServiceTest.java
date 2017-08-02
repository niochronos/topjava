package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.web.UserTestData.USER;
import static ru.javawebinar.topjava.web.UserTestData.USER_ID;
import static ru.javawebinar.topjava.web.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.MealTestData.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        Meal meal = service.get(USER_MEAL_2.getId(), USER_ID);
        MATCHER.assertEquals(USER_MEAL_2, meal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetOtherUserMeal() throws Exception {
        service.get(USER_MEAL_5.getId(), ADMIN_ID);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(ADMIN_MEAL_0.getId(), ADMIN_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(ADMIN_MEAL_1), service.getAll(ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteOtherUserMeal() throws Exception {
        service.delete(ADMIN_MEAL_0.getId(), USER_ID);
    }

    @Test
    public void testGetBetweenDates() throws Exception {
        Collection<Meal> meals = service.getBetweenDates(
                LocalDate.of(2015, Month.JUNE, 1),
                LocalDate.of(2015, Month.JUNE, 1),
                ADMIN_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN_MEAL_1, ADMIN_MEAL_0), meals);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        Collection<Meal> meals = service.getBetweenDateTimes(
                LocalDateTime.of(2015, Month.MAY, 30, 9, 0),
                LocalDateTime.of(2015, Month.MAY, 30, 14, 0),
                USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_1, USER_MEAL_0), meals);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Meal> all = service.getAll(USER_ID);
        Collection<Meal> allUserMeals = Arrays.asList(USER_MEAL_5, USER_MEAL_4, USER_MEAL_3,
                                                        USER_MEAL_2, USER_MEAL_1, USER_MEAL_0);
        MATCHER.assertCollectionEquals(allUserMeals, all);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal update = new Meal(ADMIN_MEAL_0);
        update.setDescription("update");
        update.setCalories(150);
        service.update(update, ADMIN_ID);
        MATCHER.assertEquals(update, service.get(update.getId(), ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateOtherUserMeal() throws Exception {
        service.update(ADMIN_MEAL_1, USER_ID);
    }

    @Test
    public void testSave() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.now(), "admin diner", 300);
        Meal created = service.save(newMeal, ADMIN_ID);
        newMeal.setId(created.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(newMeal, ADMIN_MEAL_1, ADMIN_MEAL_0), service.getAll(ADMIN_ID));
    }

}