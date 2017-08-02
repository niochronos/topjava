package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

public class MealTestData {

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>();

//  User meals
    public static final Meal USER_MEAL_0 = new Meal(Meal.START_SEQ,
                                LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
                                "Завтрак", 500);
    public static final Meal USER_MEAL_1 = new Meal(Meal.START_SEQ + 1,
                                LocalDateTime.of(2015, Month.MAY, 30, 13, 0),
                                "Обед", 1000);
    public static final Meal USER_MEAL_2 = new Meal(Meal.START_SEQ + 2,
                                LocalDateTime.of(2015, Month.MAY, 30, 20, 0),
                                "Ужин", 500);
    public static final Meal USER_MEAL_3 = new Meal(Meal.START_SEQ + 3,
                                LocalDateTime.of(2015, Month.MAY, 31, 10, 0),
                                "Завтрак", 1000);
    public static final Meal USER_MEAL_4 = new Meal(Meal.START_SEQ + 4,
                                LocalDateTime.of(2015, Month.MAY, 31, 13, 0),
                                "Обед", 500);
    public static final Meal USER_MEAL_5 = new Meal(Meal.START_SEQ + 5,
                                LocalDateTime.of(2015, Month.MAY, 31, 20, 0),
                                "Ужин", 510);

//  Admin meals
    public static final Meal ADMIN_MEAL_0 = new Meal(Meal.START_SEQ + 6,
                                LocalDateTime.of(2015, Month.JUNE, 1, 14, 0),
                                "Админ ланч", 510);
    public static final Meal ADMIN_MEAL_1 = new Meal(Meal.START_SEQ + 7,
                                LocalDateTime.of(2015, Month.JUNE, 1, 21, 0),
                                "Админ ужин", 1500);
}
