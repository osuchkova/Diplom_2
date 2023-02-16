package api.order;

import api.Client;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private static final String PATH = "api/orders";

    public ValidatableResponse createOrder(Order order, String access_token) {
        return given().spec(getSpec()).header("Authorization", access_token).body(order).when().post(PATH).then().log().all();
    }

    public ValidatableResponse createOrderWithoutIngredients(Order order, String access_token) {
        return given().spec(getSpec()).body("").when().post(PATH).then().log().all();
    }

    public ValidatableResponse createOrderWithoutAuth(Order order, String access_token) {
        return given().spec(getSpec()).body(order).when().post(PATH).then().log().all();
    }

    public ValidatableResponse getOrders(String access_token) {
        return given().spec(getSpec()).header("Authorization", access_token).body("").when().get(PATH).then().log().all();
    }

    public ValidatableResponse getOrdersWithoutAuth(String access_token) {
        return given().spec(getSpec()).body("").when().get(PATH).then().log().all();
    }

}
