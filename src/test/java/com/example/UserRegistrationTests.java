package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тесты по регистрации пользователя")
public class UserRegistrationTests {

    UserClient userClient;
    UserCredentials userCredentials;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userCredentials = new UserCredentials();
    }

    @Test
    @DisplayName("Позивитный тест регистрации пользователя")
    public void RegistrationUserPositiveTest() {
        User user = User.getRandomCorrectUser();
        Response response = userClient.userRegistration(user);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        String accessToken = userCredentials.getUserAccessToken(user);
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Негативный тест регистрации уже существующего пользователя")
    public void RegistrationExistedUserNegativeTest() {
        User user = User.getRandomCorrectUser();
        userClient.userRegistration(user);
        Response response = userClient.userRegistration(user);
        response.then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Негативный тест регистрации пользователя без обязательного поля 'Email'")
    public void RegistrationUserWithOutRequiredFieldEmailNegativeTest() {
        User user = User.getUserWithOutEmail();
        Response response = userClient.userRegistration(user);
        response.then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Негативный тест регистрации пользователя без обязательного поля 'Password'")
    public void RegistrationUserWithOutRequiredFieldPasswordNegativeTest() {
        User user = User.getUserWithOutPassword();
        Response response = userClient.userRegistration(user);
        response.then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Негативный тест регистрации пользователя без обязательного поля 'Name'")
    public void RegistrationUserWithOutRequiredFieldNameNegativeTest() {
        User user = User.getUserWithOutName();
        Response response = userClient.userRegistration(user);
        response.then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}