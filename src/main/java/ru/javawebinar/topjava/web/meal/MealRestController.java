package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.*;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Meal save(Meal meal) throws NotFoundException {
        return service.save(meal, AuthorizedUser.id());
    }

    public void delete(int id) throws NotFoundException {
        service.delete(id, AuthorizedUser.id());
    }

    public MealWithExceed get(int id) throws NotFoundException {
        Meal meal = service.get(id, AuthorizedUser.id());

        return MealsUtil.createWitnExceeded(service.getAll(AuthorizedUser.id()), meal);
    }

    public void update(Meal meal) throws NotFoundException {
        service.update(meal, AuthorizedUser.id());
    }

    public List<MealWithExceed> getAll() {
        return createWitnExceeded(service.getAll(AuthorizedUser.id()), DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealWithExceed> getFilteredByTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return getFilteredWithExceeded(service.getAll(AuthorizedUser.id()),
                LocalDateTime.of(startDate, startTime).toLocalTime(),
                LocalDateTime.of(endDate, endTime).toLocalTime(), DEFAULT_CALORIES_PER_DAY);
    }
}