package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalTime;
import java.util.Collection;

public interface MealRepository {
    Meal save(Meal Meal, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userId);

    // null if not found
    Collection<Meal> getAll(int userId);

    Collection<Meal> getFilteredByTime(LocalTime startTime, LocalTime endTime, int userId);
}
