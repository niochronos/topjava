package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDAOImplCHMap;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;
    private static final MealDAO mealsDB = new MealDAOImplCHMap();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to meals");

        String action = req.getParameter("action");

        if (action != null && action.equals("delete")){
            int id = Integer.parseInt(req.getParameter("id"));
            mealsDB.delete(id);
        }
        else if (action != null && action.equals("edit")){
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("meal", mealsDB.getById(id));
            req.getRequestDispatcher("/mealForm.jsp").forward(req, resp);
        }

        List<MealWithExceed> mealWithExceedList =
                MealsUtil.getFilteredWithExceeded(mealsDB.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);

        req.setAttribute("mealWithExceedList", mealWithExceedList);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        Meal meal = new Meal(localDateTime, description, calories);

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            meal.setId(id);
            mealsDB.update(meal);
        } catch (Exception e) {
            mealsDB.add(meal);
        }

        doGet(req, resp);
    }
}
