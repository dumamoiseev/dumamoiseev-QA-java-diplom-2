package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты по регистрации пользователя")
public class GetOrderTests {

    private UserCredentials userCredentials;
    private UserClient userClient;
    User user;
    OrderClient orderClient;

    @Before
    public void setUp() {
        userCredentials = new UserCredentials();
        userClient = new UserClient();
        user = User.getRandomCorrectUser();
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        String refreshToken = userCredentials.getUserRefreshToken(user);
        userClient.userLogOut(refreshToken);
    }

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    public void getUserOrdersWithAuthTest() {
        userClient.userRegistration(user);
        String token = userCredentials.getUserAccessToken(user);
        Response response = OrderClient.getOrdersOfUser(token);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("orders.total", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getUserOrdersWithOutAuthTest() {
        Response response = OrderClient.getOrdersOfUserWithOutAuth();
        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}

