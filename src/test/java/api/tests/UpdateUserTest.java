package api.tests;

import api.user.User;
import api.user.UserClient;
import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CreateSteps;
import steps.LoginSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class UpdateUserTest {

    private User user;
    private UserClient userClient;
    private LoginSteps loginStep;
    private CreateSteps step;
    private ValidatableResponse createResponse;

    private ValidatableResponse updateResponse;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        step = new CreateSteps(userClient);
        loginStep = new LoginSteps(userClient);
        createResponse = null;
        updateResponse = null;
        user = step.createUniqueUser();
        createResponse = step.registerUniqueUser(user);
        token = step.getAccessToken(createResponse);
    }

    @Test
    public void shouldUpdateName() {
        updateResponse = step.updateName(user, "Test User", token);
        assertEquals(SC_OK, step.getStatusCode(updateResponse));
        assertEquals(true, step.getSuccessStatus(updateResponse));
    }

    @Test
    public void shouldUpdateEmail() {
        Faker faker = new Faker();
        updateResponse = step.updateEmail(user, faker.internet().emailAddress(), token);
        assertEquals(SC_OK, step.getStatusCode(updateResponse));
        assertEquals(true, step.getSuccessStatus(updateResponse));
    }

    @Test
    public void shouldUpdatePassword() {
        updateResponse = step.updatePassword(user, "testtest1", token);
        assertEquals(SC_OK, step.getStatusCode(updateResponse));
        assertEquals(true, step.getSuccessStatus(updateResponse));
    }

    @Test
    public void shouldNotUpdateName() {
        updateResponse = step.updateNameWithoutAuth(user, "Test User", token);
        assertEquals(SC_UNAUTHORIZED, step.getStatusCode(updateResponse));
        assertEquals(false, step.getSuccessStatus(updateResponse));
        assertEquals("You should be authorised", step.getMessage(updateResponse));
    }

    @Test
    public void shouldNotUpdateEmail() {
        updateResponse = step.updateEmailWithoutAuth(user, "olga_test@yandex.ru", token);
        assertEquals(SC_UNAUTHORIZED, step.getStatusCode(updateResponse));
        assertEquals(false, step.getSuccessStatus(updateResponse));
        assertEquals("You should be authorised", step.getMessage(updateResponse));
    }

    @Test
    public void shouldNotUpdatePassword() {
        updateResponse = step.updatePasswordWithoutAuth(user, "testtest1", token);
        assertEquals(SC_UNAUTHORIZED, step.getStatusCode(updateResponse));
        assertEquals(false, step.getSuccessStatus(updateResponse));
        assertEquals("You should be authorised", step.getMessage(updateResponse));
    }

    @After
    public void cleanUp() {
        step.deleteUser(createResponse);
    }
}
