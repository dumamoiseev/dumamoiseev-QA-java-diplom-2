package com.example;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тесты по регистрации пользователя")
public class UserLoginTests {
    UserClient userClient;
    User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomCorrectUser();
    }

    @Test
    @DisplayName("Позивитный тест логина курьера")
    public void LoginUserPositiveTest() {
        userClient.userRegistration(user);
        Response response = userClient.userLogIn(user);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        String accessToken = response.path("accessToken");
        userClient.userLogOut(accessToken);
    }

    @Test
    @DisplayName("Негативный тест логина курьера")
    public void RegistrationExistedUserPositiveTest() {
        Response response = userClient.userLogIn(user);
        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }


}