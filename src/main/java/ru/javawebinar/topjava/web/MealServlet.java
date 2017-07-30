package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    //private MealRepository repository;
    private ConfigurableApplicationContext appCxt;
    private MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //repository = new InMemoryMealRepositoryImpl();
        appCxt = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCxt.getBean(MealRestController.class);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        //repository.save(meal, AuthorizedUser.id());
        controller.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                //repository.delete(id, AuthorizedUser.id());
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final MealWithExceed mealWithExceed = "create".equals(action) ?
                        MealsUtil.createWithExceed(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000), false) :
//                        repository.get(getId(request), AuthorizedUser.id()
                            controller.get(getId(request)
                        );
                request.setAttribute("meal", mealWithExceed);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "filtered":
                log.info("getFiltered");
                LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
                LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
                LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
                LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
                request.setAttribute("meals",
                        controller.getFilteredByTime(startDate, startTime, endDate, endTime)
                );
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
//                        MealsUtil.createWitnExceeded(repository.getAll(AuthorizedUser.id()), MealsUtil.DEFAULT_CALORIES_PER_DAY)
                        controller.getAll()
                );
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCxt.close();
    }
}
