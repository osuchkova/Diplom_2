package api.tests;

import api.user.User;
import api.user.UserClient;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import steps.CreateSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;

public class CreateUserTest {

    private User user;
    private UserClient userClient;
    private CreateSteps step;
    private ValidatableResponse response;
    private ValidatableResponse response2;

    @Before
    public void setUp() {
        userClient = new UserClient();
        step = new CreateSteps(userClient);
        response = null;
        response2 = null;
    }

    @Test
    @DisplayName("Check that client is successfully registered")
    public void shouldRegisterUser() {
        user = step.createUniqueUser();
        response = step.registerUniqueUser(user);
        assertEquals(SC_OK, step.getStatusCode(response));
        assertEquals(true, step.getSuccessStatus(response));
        assertThat(step.getAccessToken(response), is(not(nullValue())));
    }

    @Test
    @DisplayName("Check that client with missing password isn't registered")
    public void shouldNotRegisterExistingUser() {
        user = step.createUniqueUser();
        response = step.registerUniqueUser(user);
        ValidatableResponse response2 = step.registerUniqueUser(user);
        assertEquals(SC_FORBIDDEN, step.getStatusCode(response2));
        assertEquals(false, step.getSuccessStatus(response2));
        assertEquals("User already exists", step.getMessage(response2));
    }

    @Test
    @DisplayName("Check that client with missing password isn't registered")
    public void shouldNotRegisterUserWithMissingField() {
        user = step.createUserWithoutPassword();
        response = step.registerUniqueUser(user);
        assertEquals(SC_FORBIDDEN, step.getStatusCode(response));
        assertEquals(false, step.getSuccessStatus(response));
        assertEquals("Email, password and name are required fields", step.getMessage(response));
    }

    @After
    public void cleanUp() {
        step.deleteUser(response);
        step.deleteUser(response2);
    }
}