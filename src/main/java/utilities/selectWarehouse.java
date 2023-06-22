package main.java.utilities;

import dto.UserLoginDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class selectWarehouse extends BaseClassAPI{

    @BeforeClass
    public void setup() throws Exception {
        initialiseProperties_Wms();
        // used below code to bypass ssl certificate
        RestAssured.useRelaxedHTTPSValidation();
    }




// get warehouse is not needed currently, we are providing warehouse id from wms property

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetWarehouse() {

        Response getWarehouse = RestAssured.given()
                .headers(getHeaders())
                .when()
                .get("/user/warehouse/")
                .then().assertThat().statusCode(200)
                .extract().response();

        // Extract the response body
        System.out.println("--------------response Body------------------");
        System.out.println(getWarehouse.getBody().asString());

        // Extract warehouseId from the response body
        String wareHouseId = getWarehouse.jsonPath().getString("data.warehouse.warehouseId");
        // Print the warehouseId List
        System.out.println("Warehouse ID: " + wareHouseId);

        // Extract unit value of warehouse from List to use further
        MathUtil x = new MathUtil();
        String trimmedString = wareHouseId.substring(1, wareHouseId.length() - 1);

        // Split the string using the comma as the delimiter
        String[] array = trimmedString.split(", ");

        // Convert the array to a list
        List<String> list = Arrays.asList(array);
        List<Integer> integerList = new ArrayList<>();
        for (String str : list) {
            int intValue = Integer.parseInt(str);
            integerList.add(intValue);
        }
        warehouseId = String.valueOf(MathUtil.function(integerList));
        System.out.println("Warehouse ID to be Selected: " +warehouseId);

    }
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testSelectedWarehouse() throws IOException {

        baseURI=baseURL;
        Response selected =RestAssured.given()
                .headers(getHeaders())
                .body("{\"warehouseId\": " + warehouseId + "}")
                .when().post("/warehouse/selectedWarehouse")
                .then().assertThat().statusCode(200)
                .extract().response().prettyPeek();

        // Print the response body
        System.out.println("-------------------response Body-------------------");
        System.out.println("Selected WareHouse ID : " +warehouseId);
    }
}
