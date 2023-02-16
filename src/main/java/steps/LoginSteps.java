package steps;

import api.user.User;
import api.user.UserClient;
import api.user.UserCredentials;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class LoginSteps {

    private UserClient userClient;

    public LoginSteps(UserClient userClient) {
        this.userClient = userClient;
    }

    @Step("Log in by existing user")
    public ValidatableResponse login(UserCredentials creds, String accessToken) {
        return userClient.login(creds, accessToken);
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
}
