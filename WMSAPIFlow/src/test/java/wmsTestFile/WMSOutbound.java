package wmsTestFile;

import utilities.*;
import dto.outbound.*;
import org.testng.annotations.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Collections;
import java.text.SimpleDateFormat;

import static io.restassured.RestAssured.baseURI;

public class WMSOutbound extends BaseClassAPI {

    public static String orderId=null;
    public static String orderCode=null;
    public static String orderReference=null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = dateFormat.format(new Date());



    @BeforeClass
    public void setup() throws Exception {
        initialiseProperties_Wms();

        // used below code to bypass ssl certificate
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void testSelectedWarehouse() throws IOException {

        baseURI=baseURL;
        RestAssured.given()
                .headers(getHeaders())
                .body("{\"warehouseId\": " + warehouseId + "}")
                .when().post("/warehouse/selectedWarehouse")
                .then().assertThat().statusCode(200)
                .extract().response().prettyPeek();

        System.out.println("Selected WareHouse ID : " +warehouseId);
    }


    @Test()
    public void testOrderUpload() {
        // Specify the file path
        File file = new File(dir+"/resources/ORDER.xlsx");

        System.out.println("File Name: "+ file);


        // Upload the file
        Response response= RestAssured.given()
                .header("Authorization", "Bearer "+authToken)
                .multiPart("file", file)
                .when().post("/order/upload")
                .then()
                .contentType(ContentType.JSON)
                .extract().response().prettyPeek();
    }

    @Test()
    public void testGetOrderProcess(){
        Response response = RestAssured.given()
                .headers(getHeaders())
                .queryParams("fromDate", currentDate)
                .queryParams("toDate", currentDate)
                .get("/orderDetails/getForProcess").prettyPeek();

        orderId = response.jsonPath().getString("data.order.orderId");

        System.out.println(orderId);
    }

    @Test()
    public void testOrderProcess(){
        Response response= RestAssured.given()
                .headers(getHeaders())
                .contentType(ContentType.JSON)
                .body("{\"orderId\":[" + orderId + "]}")
                .when()
                .post("/order/process")
                .then()
                .contentType(ContentType.JSON)
                .extract().response().prettyPeek();

    }

    @Test()
    public void testWaveCreation(){
        Response response = RestAssured.given()
                .headers(getHeaders())
                .queryParams("fromDate", currentDate)
                .queryParams("toDate", currentDate)
                .get("/waveCreation/getForWaveFilter")
                .prettyPeek();

        orderId = response.jsonPath().getString("data.order.orderId");
        orderCode = response.jsonPath().getString("data.order.orderCode");
        orderReference = response.jsonPath().getString("data.order.orderReference");

        System.out.println(orderId);
        System.out.println(orderCode);
        System.out.println(orderReference);
    }
    @Test()
    public void testGetPendingWaveCreationProcess(){
        Response response = RestAssured.given()
                .headers(getHeaders())
                .queryParams("fromDate", currentDate)
                .queryParams("toDate", currentDate)
                .get("/waveCreation/getPending")
                .prettyPeek();
    }
    @Test()
    public void testGetAssignPickerForWaveRelease(){
        Response response = RestAssured.given()
                .headers(getHeaders())
                .queryParams("fromDate", currentDate)
                .queryParams("toDate", currentDate)
                .get("/assignPicker/getForRelease")
                .prettyPeek();
    }

    @Test()
    public void testCreateWave(){
        // Create the DTO payload
        WaveCreationDTO payload = new WaveCreationDTO();
        payload.setOrderId(Collections.singletonList(Integer.valueOf(orderId)));
        payload.setRouteId("");
        payload.setCustomerId(150);
        payload.setCityId(42);
        payload.setItemCategoryId(114);
        payload.setItemGroupId(100);
        payload.setItemSubgroupId(138);
        payload.setItemStatusId(72);
        payload.setOrderTypeCode("OBCO");
        payload.setPriorityCode("HIGH");
        payload.setVehicleTypeCode("");
        payload.setTransportMode("");
        payload.setReqArrivalDate(currentDate);
        payload.setScheduledArrivalDate("2023-06-14");
        payload.setExpectedShipDate("2023-06-14");
        payload.setOrderStopByDate("");
        payload.setFromDate(currentDate);
        payload.setToDate(currentDate);
        payload.setBasedOn("OB");

        // Send the request using Rest Assured
        Response response = RestAssured.given()
                .contentType(ContentType.TEXT)
                .body(payload)
                .post("/waveCreation/getLoadData")
                .prettyPeek();

        // Print the response
        System.out.println(response.getBody().asString());
    }




    @Test()
    public void testWaveAdd(){
        // Create the DTO payload
        AddWaveCreationDTO payload = new AddWaveCreationDTO();
        payload.setBasedOn("OB");
        payload.addWaveLineLevelDetail(Integer.parseInt(orderId), 4508);

        Response response = RestAssured.given()
                .header("Content-Type", ContentType.TEXT)
                .headers(getHeaders())
                .body(payload)
                .post("/waveCreation/add")
                .prettyPeek();
    }

    @Test
    public void testGenerateWave() {
        waveCreationGenerateWaveDTO payload = new waveCreationGenerateWaveDTO();
        // Set values for the fields
        payload.setOrderId(Collections.singletonList(Integer.valueOf(orderId)));
        payload.setRouteId("");
        payload.setCustomerId(150);
        payload.setCityId(42);
        payload.setItemCategoryId(114);
        payload.setItemGroupId(100);
        payload.setItemSubgroupId(138);
        payload.setItemStatusId(72);
        payload.setOrderTypeCode("OBCO");
        payload.setPriorityCode("HIGH");
        payload.setVehicleTypeCode("");
        payload.setTransportMode("");
        payload.setReqArrivalDate("2023-06-15");
        payload.setScheduledArrivalDate("2023-06-15");
        payload.setExpectedShipDate("2023-06-15");
        payload.setOrderStopByDate("2023-06-15");
        payload.setFromDate("2023-06-08");
        payload.setToDate("2023-06-15");
        payload.setBasedOn("OB");
        payload.setIsLineLevel("Y");

        Response response = RestAssured.given()
                .headers(getHeaders())
                .body(payload)
                .when().post("/waveCreation/generateWave")
                .prettyPeek();
    }

}





