package com.example;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends ApiClient {

    @Step("Создать заказ с токеном")
    public static Response createOrderWithAuth(Ingredients ingredients, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .headers(
                        "Authorization", "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .body(ingredients)
                .when()
                .post("/orders");
    }

    @Step("Создать заказ без токена")
    public static Response createOrderWithOutAuth(Ingredients ingredients) {
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post("/orders");
    }

    @Step("Получить список заказов пользователя")
    public static Response getOrdersOfUser(String authentication) {
        return given()
                .headers(
                        "Authorization", "Bearer " + authentication,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .spec(getBaseSpec())
                .when()
                .get("/orders");
    }

    @Step("Получить список заказов пользователя")
    public static Response getOrdersOfUserWithOutAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/orders");
    }
}