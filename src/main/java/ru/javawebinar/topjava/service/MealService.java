package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface MealService {
    Meal save(Meal meal, int userId);

    void delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    void update(Meal meal, int userId) throws NotFoundException;

    List<Meal> getAll(int userId);

    Collection<Meal> getFilteredByTime(LocalTime startTime, LocalTime endTime, int userId);
}