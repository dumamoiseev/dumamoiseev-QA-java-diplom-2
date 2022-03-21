package com.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class Ingredients extends ApiClient {

    public ArrayList<String> ingredients;
    public Ingredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
    private static final String INGREDIENTS_PATH = "ingredients";

    @Step("Создать ингридиенты бургера")
    public static Ingredients getRandomBurger() {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .statusCode(200);

        ArrayList<String> ingredients = new ArrayList<>();

        List<String> bunIngredient = response.extract().jsonPath().getList("data.findAll{it.type =='bun'}._id");
        List<String> sauceIngredient = response.extract().jsonPath().getList("data.findAll{it.type =='sauce'}._id");
        List<String> mainIngredient = response.extract().jsonPath().getList("data.findAll{it.type =='main'}._id");

        ingredients.add(bunIngredient.get(nextInt(0,bunIngredient.size())));
        ingredients.add(sauceIngredient.get(nextInt(0,sauceIngredient.size())));
        ingredients.add(mainIngredient.get(nextInt(0,mainIngredient.size())));
        return new Ingredients(ingredients);
    }

    @Step("Создание бургера без ингридиентов")
    public static Ingredients getEmptyBurger() {
        ArrayList<String> ingredients = new ArrayList<>();
        return new Ingredients(ingredients);
    }

    @Step("Создать пустые ингридиенты бургера")
    public static Ingredients getIncorrectBurger() {
        ArrayList<String> ingredients = new ArrayList<>();
        String someIngredient = (RandomStringUtils.randomAlphabetic(3));
        ingredients.add(someIngredient);
        return new Ingredients(ingredients);
    }
}