package com.example;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient extends ApiClient {

    @Step("Регистрация курьера")
    public Response userRegistration(User user) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(user)
                .when()
                .post("/auth/register");
    }

    @Step("login курьера")
    public Response userLogIn(User user) {
        return given()
                .spec(getBaseSpec())
                .when()
                .and()
                .body(user)
                .post("/auth/login");
    }

    @Step("Редактирование курьера с авторизацией")
    public Response userEdit(User user, String authentication) {
        return given()
                .headers(
                        "Authorization", "Bearer " + authentication,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .spec(getBaseSpec())
                .when()
                .and()
                .body(user)
                .patch("/auth/user");
    }

    @Step("Редактирование курьера без авторизации")
    public Response userEditWithOutAuth(User user) {
        return given()
                .spec(getBaseSpec())
                .when()
                .and()
                .body(user)
                .patch("/auth/user");
    }

    @Step("LogOut курьера")
    public void userLogOut(String token) {
        given()
                .spec(getBaseSpec())
                .when()
                .and()
                .body(token)
                .post("/auth/logout");
    }

    @Step("Registration и Login")
    public void userRegistrationAndLogin(User user) {
        userRegistration(user);
        userLogIn(user);
    }

    @Step("Удаление клиента")
    public void delete(String authentication) {
        given()
                .headers(
                        "Authorization", "Bearer " + authentication,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .spec(getBaseSpec())
                .when()
                .delete("auth/user")
                .then()
                .statusCode(202);
    }
}
