package com.example;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тесты по регистрации пользователя")
public class UserEditionTests {
    UserClient userClient;
    UserCredentials userCredentials;
    User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userCredentials = new UserCredentials();
        user = User.getRandomCorrectUser();
    }

    @Test
    @DisplayName("Позивитный тест на изменение данных курьера")
    public void EditUserWithAuthorisationTest() {
        userClient.userRegistration(user);
        String token = userCredentials.getUserAccessToken(user);
        User newUser = User.getRandomCorrectUser();
        Response response = userClient.userEdit(newUser, token);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("user.email", equalTo(newUser.email))
                .body("user.name", equalTo(newUser.name));
        userClient.userLogOut(token);
    }

    @Test
    @DisplayName("Негативный тест на изменение данных курьера без авторизации")
    public void EditUserWithOutAuthorisationTest() {
        userClient.userRegistrationAndLogin(user);
        User newUser = User.getRandomCorrectUser();
        Response response = userClient.userEditWithOutAuth(newUser);
        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}