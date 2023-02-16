package api.tests;

import api.order.Order;
import api.order.OrderClient;
import api.user.User;
import api.user.UserClient;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CreateSteps;
import steps.LoginSteps;
import steps.OrderSteps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;
    private CreateSteps userStep;
    private LoginSteps loginStep;
    private OrderSteps orderStep;
    private ValidatableResponse createResponse;
    private ValidatableResponse orderResponse;
    private List<String> ingredients;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        userStep = new CreateSteps(userClient);
        orderStep = new OrderSteps(orderClient);
        createResponse = null;
        orderResponse = null;
        user = userStep.createUniqueUser();
        createResponse = userStep.registerUniqueUser(user);
        token = userStep.getAccessToken(createResponse);
        ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa6d");
    }

    @Test
    public void shouldCreateOrder() {
        order = new Order(ingredients);
        orderResponse = orderStep.createOrderWithIngredients(order, token);
        assertEquals(SC_OK, orderStep.getStatusCode(orderResponse));
        assertEquals(true, orderStep.getSuccessStatus(orderResponse));
    }

    @Test
    public void shouldNotCreateOrderWithoutIngredients() {
        List<String> list = Collections.<String>emptyList();
        order = new Order(list);
        orderResponse = orderStep.createOrderWithoutIngredients(order, token);
        assertEquals(SC_BAD_REQUEST, orderStep.getStatusCode(orderResponse));
        assertEquals(false, orderStep.getSuccessStatus(orderResponse));
        assertEquals("Ingredient ids must be provided", orderStep.getMessage(orderResponse));
    }

    @Test
    public void shouldCreateOrderWithoutAuth() {
        order = new Order(ingredients);
        orderResponse = orderStep.createOrderWithoutAuth(order, token);
        assertEquals(SC_OK, orderStep.getStatusCode(orderResponse));
        assertEquals(true, orderStep.getSuccessStatus(orderResponse));
    }

    @Test
    public void shouldNotCreateOrderWithWrongHash() {
        List<String> list = Arrays.asList("61c0c5a");
        order = new Order(list);
        orderResponse = orderStep.createOrderWithoutAuth(order, token);
        assertEquals(SC_INTERNAL_SERVER_ERROR, orderStep.getStatusCode(orderResponse));
    }

    @After
    public void cleanUp() {
        userStep.deleteUser(createResponse);
    }
}

