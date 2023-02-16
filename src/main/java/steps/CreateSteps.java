package steps;

import api.user.User;
import api.user.UserClient;
import api.user.UserCredentials;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class CreateSteps {

    private UserClient userClient;
    private Faker faker = new Faker();
    private static final String EMPTY_STRING = "";

    public CreateSteps(UserClient userClient) {
        this.userClient = userClient;
    }

    @Step("Create unique user")
    public User createUniqueUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();
        return new User(email, password, name);
    }

    @Step("Register unique user")
    public ValidatableResponse registerUniqueUser(User user) {
        return userClient.create(user);
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

    @Step("Get user access token")
    public String getAccessToken(ValidatableResponse responseBody) {
        return responseBody.extract().response().path("accessToken");
    }

    @Step("Create user without password")
    public User createUserWithoutPassword() {
        String email = faker.internet().emailAddress();
        String name = faker.name().firstName();
        return new User(email, EMPTY_STRING, name);
    }

    @Step("Delete created user")
    public void deleteUser(ValidatableResponse responseBody) {
        if (responseBody != null && getSuccessStatus(responseBody) == true) {
            userClient.delete(getAccessToken(responseBody));
        }
    }

    @Step("Update password of authorized user")
    public ValidatableResponse updatePassword(User user, String password, String accessToken) {
        user.setPassword(password);
        return userClient.update(user, accessToken);
    }

    @Step("Update name of authorized user")
    public ValidatableResponse updateName(User user, String name, String accessToken) {
        user.setName(name);
        return userClient.update(user, accessToken);
    }

    @Step("Update name of authorized user")
    public ValidatableResponse updateEmail(User user, String email, String accessToken) {
        user.setEmail(email);
        return userClient.update(user, accessToken);
    }

    @Step("Update password of authorized user")
    public ValidatableResponse updatePasswordWithoutAuth(User user, String password, String accessToken) {
        user.setPassword(password);
        return userClient.updateNoAuth(user, accessToken);
    }

    @Step("Update name of authorized user")
    public ValidatableResponse updateNameWithoutAuth(User user, String name, String accessToken) {
        user.setName(name);
        return userClient.updateNoAuth(user, accessToken);
    }

    @Step("Update name of authorized user")
    public ValidatableResponse updateEmailWithoutAuth(User user, String email, String accessToken) {
        user.setEmail(email);
        return userClient.updateNoAuth(user, accessToken);
    }
}
