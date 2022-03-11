package com.example;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class OrderClient extends ApiClient {

    private String baseURI = "/orders";

    @Step("Создать заказ с токеном")
    public ValidatableResponse createOrderWithAuth(Ingredients ingredients, String accessToken) {
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
                .post(baseURI)
                .then();
    }

    @Step("Создать заказ без токена")
    public ValidatableResponse createOrderWithOutAuth(Ingredients ingredients) {
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(baseURI)
                .then();
    }

    @Step("Получить список заказов пользователя")
    public ValidatableResponse getOrdersOfUser(String authentication) {
        return given()
                .headers(
                        "Authorization", "Bearer " + authentication,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .spec(getBaseSpec())
                .when()
                .get(baseURI)
                .then();
    }

    @Step("Получить список заказов пользователя")
    public ValidatableResponse getOrdersOfUserWithOutAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(baseURI)
                .then();
    }
}