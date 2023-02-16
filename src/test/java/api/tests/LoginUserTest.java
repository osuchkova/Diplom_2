package api.tests;

import api.user.User;
import api.user.UserClient;
import api.user.UserCredentials;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.LoginSteps;
import steps.CreateSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class LoginUserTest {

    private User user;
    private UserClient userClient;
    private LoginSteps loginStep;
    private CreateSteps createStep;
    private ValidatableResponse createResponse;
    private ValidatableResponse loginResponse;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        createStep = new CreateSteps(userClient);
        loginStep = new LoginSteps(userClient);
        createResponse = null;
        loginResponse = null;
        user = createStep.createUniqueUser();
        createResponse = createStep.registerUniqueUser(user);
        token = createStep.getAccessToken(createResponse);
    }

    @Test
    public void shouldLoginByExistingUser() {
        loginResponse = loginStep.login(UserCredentials.from(user), token);
        assertEquals(SC_OK, loginStep.getStatusCode(loginResponse));
        assertEquals(true, loginStep.getSuccessStatus(loginResponse));
    }

    @Test
    public void shouldNotLoginByNotValidCreds() {
        loginResponse = loginStep.login(UserCredentials.passwordInvalid(user), token);
        assertEquals(SC_UNAUTHORIZED, loginStep.getStatusCode(loginResponse));
        assertEquals(false, loginStep.getSuccessStatus(loginResponse));
        assertEquals("email or password are incorrect", loginStep.getMessage(loginResponse));
    }

    @After
    public void cleanUp() {
        createStep.deleteUser(createResponse);
    }
}
