package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @RequestMapping
    public String getMeals(Model model) {
        log.info("meals");
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @RequestMapping("/new")
    public String newMealForm(HttpServletRequest request) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/save")
    public String saveMeal(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (StringUtils.hasLength(request.getParameter("id"))) {
            int id = Integer.parseInt(request.getParameter("id"));
            update(meal, id);
        } else {
            create(meal);
        }
        return "redirect:/meals";
    }

    @RequestMapping("/edit")
    public String editMealForm(HttpServletRequest request) {
        request.setAttribute("meal", get(Integer.parseInt(request.getParameter("id"))));
        return "mealForm";
    }

    @RequestMapping("/delete")
    public String deleteMeal(HttpServletRequest request) {
        delete(Integer.parseInt(request.getParameter("id")));
        return "redirect:/meals";
    }

    @GetMapping ("/filter")
    public String filterMeal(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
