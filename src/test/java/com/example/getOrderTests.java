package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты по регистрации пользователя")
public class getOrderTests {

    private UserCredentials userCredentials;
    private UserClient userClient;
    User user;
    OrderClient orderClient;

    @Before
    public void setUp() {
       userCredentials = new UserCredentials();
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
        ValidatableResponse response = new OrderClient().getOrdersOfUser(token);
        orderClient.checkResponse(response, 200,true);
        assertThat(response.extract().path("orders.total"), notNullValue());
    }
    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getUserOrdersWithOutAuthTest() {
        ValidatableResponse response = new OrderClient().getOrdersOfUserWithOutAuth();
        orderClient.checkResponse(response, 401,false);
        assertThat(response.extract().path("message"),equalTo("You should be authorised"));
    }
}

