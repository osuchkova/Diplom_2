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
import steps.OrderSteps;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class GetOrdersTest {

    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;
    private OrderSteps orderStep;
    private CreateSteps userStep;
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
        order = new Order(ingredients);
    }

    @Test
    public void shouldReturnOrders() {
        orderStep.createOrderWithIngredients(order, token);
        orderResponse = orderStep.getOrders(token);
        assertEquals(SC_OK, orderStep.getStatusCode(orderResponse));
        assertEquals(true, orderStep.getSuccessStatus(orderResponse));
    }

    @Test
    public void shouldNotReturnOrders() {
        orderStep.createOrderWithIngredients(order, token);
        orderResponse = orderStep.getOrdersWithoutAuth(token);
        assertEquals(SC_UNAUTHORIZED, orderStep.getStatusCode(orderResponse));
        assertEquals(false, orderStep.getSuccessStatus(orderResponse));
        assertEquals("You should be authorised", orderStep.getMessage(orderResponse));
    }

    @After
    public void cleanUp() {
        userStep.deleteUser(createResponse);
    }
}
