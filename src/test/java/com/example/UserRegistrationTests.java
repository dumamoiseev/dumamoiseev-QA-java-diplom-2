package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тесты по регистрации пользователя")
public class UserRegistrationTests {
    User user;
    UserClient userClient;
    UserCredentials userCredentials;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userCredentials = new UserCredentials();
        user = User.getRandomCorrectUser();
        accessToken = userCredentials.getUserAccessToken(user);
    }

    @After
    public void tearDown() {
        String accessToken = userCredentials.getUserAccessToken(user);
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Позивитный тест регистрации пользователя")
    public void RegistrationUserPositiveTest() {
        Response response = userClient.userRegistration(user);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }
}