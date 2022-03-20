package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тесты по регистрации пользователя")
public class UserLoginTests {
    UserClient userClient;
    User user;
    User fakeUser;
    UserCredentials userCredentials;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userCredentials = new UserCredentials();
        user = User.getRandomCorrectUser();
        userClient.userRegistration(user);
        accessToken = userCredentials.getUserAccessToken(user);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Позивитный тест логина курьера")
    public void LoginUserPositiveTest() {
        Response response = userClient.userLogIn(user);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Негативный тест логина курьера")
    public void RegistrationNonExistedUserPositiveTest() {
        fakeUser = User.getRandomCorrectUser();
        Response response = userClient.userLogIn(fakeUser);
        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }
}