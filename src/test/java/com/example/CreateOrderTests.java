package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты по регистрации пользователя")
public class CreateOrderTests {

    private UserCredentials userCredentials;
    private UserClient userClient;
    User user;
    OrderClient orderClient;
    String accessToken;

    @Before
    public void setUp() {
        userCredentials = new UserCredentials();
        userClient = new UserClient();
        user = User.getRandomCorrectUser();
        orderClient = new OrderClient();
        userClient.userRegistration(user);
        accessToken = userCredentials.getUserAccessToken(user);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Позитивный тест создания заказа с авторизацией")
    public void createOrderWithAuthTest() {
        Response response = OrderClient.createOrderWithAuth(Ingredients.getRandomBurger(), accessToken);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создания заказа без авторизации")
    public void createOrderWithOutAuthTest() {
        Response response = OrderClient.createOrderWithOutAuth(Ingredients.getRandomBurger());
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создания заказа без авторизации и без ингридиентов")
    public void createOrderWithOutAuthAndWithOutIngredientsTest() {
        Response response = OrderClient.createOrderWithOutAuth(Ingredients.getEmptyBurger());
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создания заказа с некорректным ингридиентом")
    public void createOrderWithIncorrectIngredientsTest() {
        Response response = OrderClient.createOrderWithOutAuth(Ingredients.getIncorrectBurger());
        response.then()
                .assertThat()
                .statusCode(500);
    }
}

