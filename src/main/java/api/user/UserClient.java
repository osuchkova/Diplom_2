package api.user;

import api.Client;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String PATH = "api/auth/register";
    private static final String PATH_LOGIN = "api/auth/login";

    private static final String PATH_UPDATE = "api/auth/user";

    public ValidatableResponse create(User user) {
        return given().spec(getSpec()).body(user).when().post(PATH).then().log().all();
    }

    public ValidatableResponse login(UserCredentials creds, String access_token) {
        return given().spec(getSpec()).header("Authorization", access_token).body(creds).when().post(PATH_LOGIN).then().log().all();
    }

    public ValidatableResponse update(User user, String access_token) {
        return given().spec(getSpec()).header("Authorization", access_token).body(user).when().patch(PATH_UPDATE).then().log().all();
    }

    public ValidatableResponse updateNoAuth(User user, String access_token) {
        return given().spec(getSpec()).body(user).when().patch(PATH_UPDATE).then().log().all();
    }

    public ValidatableResponse delete(String access_token) {
        return given().spec(getSpec()).header("Authorization", access_token).body("").when().delete(PATH).then().log().all();
    }
}