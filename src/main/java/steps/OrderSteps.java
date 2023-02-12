package steps;

import api.order.Order;
import api.order.OrderClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Collections;
import java.util.List;

public class OrderSteps {

    private OrderClient orderClient;

    public OrderSteps(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @Step("Create order with ingredients by authorized user")
    public ValidatableResponse createOrderWithIngredients(Order order, String accessToken) {
        return orderClient.createOrder(order, accessToken);
    }

    @Step("Create order with ingredients by unauthorized user")
    public ValidatableResponse createOrderWithoutAuth(Order order, String accessToken) {
        return orderClient.createOrderWithoutAuth(order, accessToken);
    }

    @Step("Create order without ingredients")
    public ValidatableResponse createOrderWithoutIngredients(Order order, String accessToken) {
        return orderClient.createOrderWithoutIngredients(order, accessToken);
    }

    @Step("Get orders of authorized user")
    public ValidatableResponse getOrders(String accessToken) {
        return orderClient.getOrders(accessToken);
    }

    @Step("Get orders of unauthorized user")
    public ValidatableResponse getOrdersWithoutAuth(String accessToken) {
        return orderClient.getOrdersWithoutAuth(accessToken);
    }

    @Step("Get response status code")
    public int getStatusCode(ValidatableResponse responseBody) {
        return responseBody.extract().response().statusCode();
    }

    @Step("Get success status")
    public boolean getSuccessStatus(ValidatableResponse responseBody) {
        return responseBody.extract().response().path("success");
    }

    @Step("Get message")
    public String getMessage(ValidatableResponse responseBody) {
        return responseBody.extract().response().path("message");
    }

    @Step("Get order")
    public ValidatableResponse getOrder(ValidatableResponse responseBody) {
        return responseBody.extract().path("order");
    }
}
