package utilities;

import dto.UserLoginDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class LoginWarehouse extends BaseClassAPI {

    @BeforeClass
    public void setup() throws Exception {
        initialiseProperties_Wms();
        // used below code to bypass ssl certificate
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test()
    @Parameters({"username","password"})
    public void testGetAccessTokens(String username, String password) {
        // providing username and password by xml suite parameter
        // Create the DTO instance with the payload
        UserLoginDTO userLoginDTO = new UserLoginDTO(username, password, 1, 1);

        // Send the POST request using RestAssured
        baseURI = baseURL;
        Response response = given().log().all().headers(getHeaders())
                .body(userLoginDTO)
                .when()
                .post("/user/login")
                .then().log().all().assertThat().statusCode(200)
                .extract().response();

        // Extract the x-Auth-token header from the response
        authToken = response.header("x-Auth-token");

        System.out.println("---------------response Body-------------------------");

        // Print the x-Auth-token
        System.out.println("x-Auth-token: " + authToken);
        wmsHeaders.put("Authorization", "Bearer " + authToken);

        // Extract UserId and Username from the response body
        userId = response.jsonPath().getString("data.user.userId");
        String  displayName = response.jsonPath().getString("data.user.displayName");
        System.out.println("Login User Id: " +userId);
        System.out.println("Login  User Name: " +displayName);
    }
}
