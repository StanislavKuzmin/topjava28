package ru.javawebinar.topjava.service;


import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.adminMeal1;
import static ru.javawebinar.topjava.MealTestData.adminMeal2;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;


@ActiveProfiles({"datajpa","datajpa,jpa"})
public class DataJpaUserServiceTest extends AbstractUserServiceTest{

    @Test
    public void getWithMeal() {
        User userWithMeal = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
        userWithMeal.addMealtoUser(adminMeal1);
        userWithMeal.addMealtoUser(adminMeal2);
        User user = service.getWithMeals(ADMIN_ID);
        assertEquals(user, userWithMeal);
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }
}
