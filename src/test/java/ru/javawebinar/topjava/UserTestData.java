package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Objects;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", 2005, Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", 1900, Role.ROLE_ADMIN, Role.ROLE_USER);

    public static final BeanMatcher<User> MATCHER = BeanMatcher.of(User.class,
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getName(), actual.getName())
                            && Objects.equals(expected.getEmail(), actual.getEmail())
                            && Objects.equals(expected.getCaloriesPerDay(), actual.getCaloriesPerDay())
                            && Objects.equals(expected.isEnabled(), actual.isEnabled())
                            && Objects.equals(expected.getRoles(), actual.getRoles())
                    )
    );

    public static final User NEW_USER = new User(null, "New", "new@gmail.com", "newPass", 2300, Role.ROLE_USER, Role.ROLE_ADMIN);
    public static final String JSON_NEW_USER_WITH_PASSWORD = "{\"name\":\"New\",\"email\":\"new@gmail.com\",\"password\":\"password\",\"enabled\":true,\"registered\":\"2017-09-20T09:12:40.953+0000\",\"roles\":[\"ROLE_USER\",\"ROLE_ADMIN\"],\"caloriesPerDay\":2300}";
}
