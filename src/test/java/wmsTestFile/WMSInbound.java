package test.java.wmsTestFile;

import dto.*;
import utilities.*;
import dto.Inbound.*;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class WMSInbound extends BaseClassAPI {

//    public static String authToken = "";
//        public static String userId = "";
//    public static String warehouseId = "";
    public static String ibPgNumber = null;
    public static String pgCode = null;
    public static String taskId = null;
    public static String ibGrnCode = null;
    public static String ibGrnNumber = null;
    public static String ibPreGrnDetailsId = null;
    public static String ibGrnDetailsId = null;
    public static String ibGrnConfirmationId = null;
    public static String jsonPayload = null;
    public static String ibPutTransId = null;
    public static String ibPutDetailsId = null;
    public static String ibPutId = null;
    public static String ibTaskCode = null;
    public static String ibConfirmQty = null;
    public static String ibParentId = null;
    public static String iBliId = null;
    Utility.RandomVehicleNumberGenerator generator = new Utility.RandomVehicleNumberGenerator();

    // Generate a random vehicle number
    String vehicleNumber = generator.generateRandomVehicleNumber();
    Random random = new Random();
    int invoiceNo = random.nextInt(90000000) + 10000000;
    String invoiceNumber = "INV" +invoiceNo;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = dateFormat.format(new Date());


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

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testPreGrnCreation() throws IOException{

        IBPreGrnCreationDTO requestDTO = new IBPreGrnCreationDTO();
        requestDTO.setIbpgDate(currentDate);
        requestDTO.setIbpgType("GRNPO");
        requestDTO.setInvoiceNo(invoiceNumber);  //invoice number should be unique
        requestDTO.setInvoiceValue(1111);
        requestDTO.setInvoiceDate(currentDate);
        requestDTO.setVehicleNo(vehicleNumber);
        requestDTO.setDockNo("1");
        requestDTO.setSupplierId(85);
        requestDTO.setSupplierOrderNo("");
        requestDTO.setTaxGroupId(0);
        requestDTO.setBaName("");
        requestDTO.setRef1("");
        requestDTO.setRef2("");
        requestDTO.setRef3("");
        requestDTO.setSpecialInstructions1("");


        // Send the POST request
        Response response = RestAssured.given()
                .headers(getHeaders())
                .contentType(ContentType.JSON)
                .when().body(requestDTO)
                .post("/ibPreGrn/create").then().assertThat().statusCode(200)
                .extract().response().prettyPeek();

        // Handle the response
        System.out.println("---------------response Body----------------");


        // Add your code to process the response here
        pgCode = response.jsonPath().getString("data.pgCode");
        ibPgNumber = response.jsonPath().getString("data.ibpgNumber");

        //print preGrn code as prCode & inbound pre Grn number as ibPgNumber
        System.out.println("Pre Grn Code: " +pgCode);
        System.out.println("Pre Grn Number: " +ibPgNumber);
    }

    @Test()
    public void testUploadFileToCreatePreGrn() {
        // Specify the file path
        File file = new File(dir+"/resources/PREGRN.xlsx");

        // Upload the file
        Response response= RestAssured.given()
                .headers(getHeaders())
                .multiPart("file", file)
                .when().post("/ibPreGrn/upload")
                .then()
                .contentType(ContentType.JSON)
                .extract().response().prettyPeek();

        System.out.println(response.getBody().asString());
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testPreGrnDetailsCreation() throws IOException{
        IBPreGrnDetailsCreateDTO requestDTO = new IBPreGrnDetailsCreateDTO();
        requestDTO.setIbDate(currentDate);
        requestDTO.setIbRef("IB050623038");
        requestDTO.setItemCode(itemCode);
        requestDTO.setItemStatusId(72);
        requestDTO.setUomId(Integer.parseInt(uomId));
        requestDTO.setPackUomId(0);
        requestDTO.setIbQty(Integer.parseInt(qty));
        requestDTO.setMfgDate("2023-05-24");
        requestDTO.setExpDate("2028-05-24");
        requestDTO.setBatchNo(batchNo);
        requestDTO.setSerialNo("1111");
        requestDTO.setLotNo(lotNo);
        requestDTO.setShipDate(currentDate);
        requestDTO.setExpArrivalDate(currentDate);
        requestDTO.setLpnNo("");
        requestDTO.setColourCode("BLUE");
        requestDTO.setItemSize("1");
        requestDTO.setItemCost("1111");
        requestDTO.setItemMrp("1111");
        requestDTO.setInnerPackUomId("1");
        requestDTO.setPackCodeSeq("1");
        requestDTO.setPackingType("INTERNAL");
        requestDTO.setTaxPercent("1");
        requestDTO.setCountryId(0);
        requestDTO.setEanCode("");
        requestDTO.setUpcCode("");
        requestDTO.setHsnCode("");
        requestDTO.setIbCode("");
        requestDTO.setRemarks("good");
        requestDTO.setRemarks2("");
        requestDTO.setRemarks3("");
        requestDTO.setAttribute1("Automation");
        requestDTO.setAttribute2("");
        requestDTO.setAttribute3("");
        requestDTO.setAttribute4("");
        requestDTO.setAttribute5("");
        requestDTO.setIbpgNumber(Integer.parseInt(ibPgNumber));

        // Send the POST request
        RestAssured.given()
                .headers(getHeaders())
                .body(requestDTO)
                .contentType(ContentType.JSON)
                .when().post("/ibPreGrnDetails/create").then().assertThat().statusCode(200)
                .extract().response().prettyPeek();


    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGrnCreation() throws Exception{
        IBGrnCreationDTO requestDTO = new IBGrnCreationDTO();
        requestDTO.setGrnType("GRNPO");
        requestDTO.setGrnDate(currentDate);
        requestDTO.setSupplierId(85);
        requestDTO.setSupplierOrderNo("");
        requestDTO.setInvoiceNo(invoiceNumber); //invoice number should be unique
        requestDTO.setInvoiceValue(1111);
        requestDTO.setInvoiceDate(currentDate);
        requestDTO.setTaxGroupId(0);
        requestDTO.setBaName("");
        requestDTO.setVehicleNo(vehicleNumber);
        requestDTO.setDockNo("1");
        requestDTO.setRef1("");
        requestDTO.setRef2("");
        requestDTO.setRef3("");
        requestDTO.setSpecialInstructions1("");
        requestDTO.setSpecialInstructions2("");
        requestDTO.setSpecialInstructions3("");
        requestDTO.setForceUpload("N");


        // Send the POST request
        Response response = RestAssured.given()
                .headers(getHeaders())
                .body(requestDTO)
                .contentType(ContentType.JSON)
                .when().post("/grn/create").then().assertThat().statusCode(200)
                .extract().response().prettyPeek();

        System.out.println("-----------------Response Body-----------------------");

          //Extract GrnNumber and GrnCode
        ibGrnNumber = response.jsonPath().getString("data.grnNumber");
        ibGrnCode = response.jsonPath().getString("data.grnCode");

        System.out.println("Grn Number : " +ibGrnNumber);
        System.out.println("Grn Code : " +ibGrnCode);
    }


    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetIbPreGrnDetailsID() throws IOException{

        Response response = RestAssured.given()
                .header("Accept", "application/json, text/plain, */*")
                .header("Authorization", "Bearer " +authToken)
                .header("Connection", "keep-alive")
                .queryParams("ibpgNumber",ibPgNumber)
                .get("/ibPreGrnDetails/getByIBPGNo");

        System.out.println(response.getBody().asString());

        String preGrnDetailsId = response.jsonPath().getString("data.ibPreGrnDetails.ibPreGrnDetailsId");
        ibPreGrnDetailsId = preGrnDetailsId.replaceAll("\\[|\\]", "");

        System.out.println("GRN Details Id: "+ibPreGrnDetailsId);

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGrnDetailsCreation() throws IOException {
        testGetIbPreGrnDetailsID();
        // Create a GrnDetails object and set its properties
        IBGrnDetailsCreateDTO grnDetailsDto = new IBGrnDetailsCreateDTO();
        grnDetailsDto.setGrnNumber(Integer.parseInt(ibGrnNumber));
        grnDetailsDto.setIbPreGrnDetailsId(Integer.parseInt(ibPreGrnDetailsId));
        grnDetailsDto.setIbRef("IB050623038");
        grnDetailsDto.setIbDate(currentDate);
        grnDetailsDto.setItemCode(itemCode);
        grnDetailsDto.setUomId(Integer.parseInt(uomId));
        grnDetailsDto.setPackUomId(0);
        grnDetailsDto.setItemStatusId(72);
        grnDetailsDto.setGrnQty(Integer.parseInt(qty));
        grnDetailsDto.setTaxPercent("1");
        grnDetailsDto.setBatchNo(batchNo);
        grnDetailsDto.setSerialNo("");
        grnDetailsDto.setLotNo(lotNo);
        grnDetailsDto.setLpnNo("");
        grnDetailsDto.setMfgDate(currentDate);
        grnDetailsDto.setExpDate("2028-05-24");
        grnDetailsDto.setShipDate(currentDate);
        grnDetailsDto.setExpArrivalDate(currentDate);
        grnDetailsDto.setCountryId(0);
        grnDetailsDto.setEanCode("");
        grnDetailsDto.setUpcCode("");
        grnDetailsDto.setHsnCode("");
        grnDetailsDto.setColourCode("");
        grnDetailsDto.setItemSize("");
        grnDetailsDto.setItemCost(1111);
        grnDetailsDto.setItemMrp(1111);
        grnDetailsDto.setRemarks("good");
        grnDetailsDto.setInnerPackUomId("1");
        grnDetailsDto.setPackCodeSeq(1);
        grnDetailsDto.setPackingType("INTERNAL");
        grnDetailsDto.setRemarks2("");
        grnDetailsDto.setRemarks3("");
        grnDetailsDto.setAttribute1("");
        grnDetailsDto.setAttribute2("");
        grnDetailsDto.setAttribute3("");
        grnDetailsDto.setAttribute4("");
        grnDetailsDto.setAttribute5("");
        grnDetailsDto.setSeqNo("");

        GRNDetailsRequest payload = new GRNDetailsRequest(Collections.singletonList(grnDetailsDto));
        // Convert the payload to JSON
        ObjectMapper mapper = new ObjectMapper();
        jsonPayload = mapper.writeValueAsString(payload);

        System.out.println(jsonPayload);

        // Send the API request using Rest Assured
        baseURI=baseURL;
        Response response = given()
                .headers(getHeaders())
                .body(jsonPayload)
                .when().post("/grnDetails/create")
                .then().assertThat().statusCode(200)
                .extract().response().prettyPeek();

    }



    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGrnCreationComplete() throws IOException{

        GrnCreationCompleteDto generatePut = new GrnCreationCompleteDto();
        generatePut.setGrnNumber(Integer.parseInt(ibGrnNumber));

        // Send the POST request using RestAssured
        RestAssured.baseURI=baseURL;
        Response response = RestAssured.given()
                .headers(getHeaders())
                .body(generatePut)
                .when().post("/grn/complete")
                .then().assertThat().statusCode(200)
                .extract().response().prettyPeek();

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetTaskIdToAssign() throws IOException{
        baseURI=baseURL;
        Response response = RestAssured.given()
                .headers(getHeaders())
                .queryParams("fromDate", currentDate)
                .queryParams("toDate", currentDate)
                .get("/ibTask/getForAssign").prettyPeek();


        String ibTaskId = response.jsonPath().getList("data.task.taskId").toString();
        String taskCode = response.jsonPath().getString("data.task.taskCode");
        String grnNumber = response.jsonPath().getString("data.task.grnNumber");

        taskId = ibTaskId.replaceAll("\\[|\\]", "");

        System.out.println("Task Id : "+taskId);
        System.out.println("task Code : "+taskCode);
        System.out.println("Grn Number"+grnNumber);
    }



    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testIbAssign() throws IOException {
        testGetTaskIdToAssign();

        TaskAssignPayloadDTO payloadDto = new TaskAssignPayloadDTO();
        payloadDto.setTaskId(Collections.singletonList(Integer.valueOf(taskId)));
        payloadDto.setAssignedTo(Integer.parseInt(userId));

        // Send the POST request using RestAssured
        RestAssured.baseURI = baseURL;
        Response response = RestAssured.given()
                .headers(getHeaders())
                .body(payloadDto)
                .when().post("/ibTask/assign")
                .then().assertThat().statusCode(200)
                .extract().response().prettyPeek();


    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGrnConfirm() throws IOException{
        // Create an arrayList to hold GrnItemDto objects
        // Create new GrnItemDto object and set grnNumber '784'
        // After that, add the grnItemDto object to the grnList using the add() method
        List<GrnItemDto> grnList = new ArrayList<>();
        GrnItemDto grnItemDto = new GrnItemDto(ibGrnNumber);
        grnList.add(grnItemDto);

        // Create an instance of GrnConfirmDto and pass the grnList
        GrnConfirmDTO grnConfirmDto = new GrnConfirmDTO(grnList);

        // Send the POST request using RestAssured
        Response response = RestAssured.given()
                .headers(getHeaders())
                .body(grnConfirmDto)
                .when().post("/grn/confirm").prettyPeek();


    }
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetGrnDetailsId() throws IOException{
        baseURI=baseURL;
        Response response = RestAssured.given()
                .headers(getHeaders())
                .queryParam("grnNumber",new int[]{Integer.parseInt(ibGrnNumber)})
                .queryParam("itemStatusId",0)
                .get("/grnConfirmation/getPendingByGrn").prettyPeek();


        String iBGrnDetailsId= response.jsonPath().getString("data.grnDetails.grnDetailsId");
        String iBGrnConfirmationId = response.jsonPath().getString("data.grnDetails.grnConfirmationId");

        // Print the inbound GrnDetailsId & inbound GrnConfirmationId response
        ibGrnDetailsId = iBGrnDetailsId.replaceAll("\\[|\\]", "");
        ibGrnConfirmationId = iBGrnConfirmationId.replaceAll("\\[|\\]", "");

      // print values
        System.out.println("Ib Grn Details Id : " +ibGrnDetailsId);
        System.out.println("Ib Grn Confirmation Id : " +ibGrnConfirmationId);

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGeneratePut() throws IOException {
        testGetGrnDetailsId();
        // Create a DTO object
        IBGeneratePutDTO generatePutDTO = new IBGeneratePutDTO();
        GeneratePutItemDTO generatePutItemDTO = new GeneratePutItemDTO();
        generatePutItemDTO.setGrnDetailsId(Integer.parseInt(ibGrnDetailsId));
        generatePutItemDTO.setGrnNumber(Integer.parseInt(ibGrnNumber));
        generatePutItemDTO.setGrnConfirmationId(Integer.parseInt(ibGrnConfirmationId));
        generatePutItemDTO.setPendingQty(1);
        generatePutDTO.setGeneratePut(Collections.singletonList(generatePutItemDTO));

        // Send the POST request using RestAssured
        Response response = RestAssured.given()
                .headers(getHeaders())
                .contentType(ContentType.JSON)
                .body(generatePutDTO)
                .post("/generatePut/create")
                .then().assertThat().statusCode(200)
                .extract().response().prettyPeek();


    }

    int count=0;
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetPutProcessDetails() throws IOException, InterruptedException {
       count++;
       Thread.sleep(5000);
//        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(this::isReadyForProcessing);
        Response response = RestAssured.given()
                .headers(getHeaders())
                .queryParams("fromDate", currentDate)
                .queryParams("toDate", currentDate)
                .get("/generatePutDetails/getPending").prettyPeek();

        String results = response.jsonPath().getString("data.generatePutDetails.result");
        String result = response.jsonPath().getString("data.generatePutDetails.grnNumber");

        //retry to get put process details(api is getting longer time to get response)
        System.out.println(results+" "+count);

        if (result == null || (!result.equals(ibGrnNumber)||count==10) ){
                return;
            }
        testGetPutProcessDetails();

    }



    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetAssignPutterDetails() throws IOException, InterruptedException {
        Response response = RestAssured.given()
                .headers(getHeaders())
                .queryParams("fromDate",currentDate)
                .queryParams("toDate", currentDate)
                .get("/assignPutter/getForAssign").prettyPeek();


        //get and print required details from response body
        String putId = response.jsonPath().getString("data.put.putId");
        String putDetailsId = response.jsonPath().getString("data.put.putDetailsId");
        String putTransId = response.jsonPath().getString("data.put.putTransId");
        String taskCode = response.jsonPath().getString("data.put.taskCode");
        String confirmQty = response.jsonPath().getString("data.put.qty");
        String parentId = response.jsonPath().getString("data.put.parentId");
        String liId = response.jsonPath().getString("data.put.liId");

        //replace list with string value
        ibPutId = putId.replaceAll("\\[|\\]", "");
        ibPutDetailsId = putDetailsId.replaceAll("\\[|\\]", "");
        ibPutTransId = putTransId.replaceAll("\\[|\\]", "");
        ibTaskCode = taskCode.replaceAll("\\[|\\]", "");
        ibConfirmQty = confirmQty.replaceAll("\\[|\\]", "");
        ibParentId = parentId.replaceAll("\\[|\\]", "");
        iBliId = liId.replaceAll("\\[|\\]", "");

        System.out.println("Put Id : "+ibPutId);
        System.out.println("Put Details Id : "+ibPutDetailsId);
        System.out.println("Put Trans Id : "+ibPutTransId);
        System.out.println("Task Code : "+ibTaskCode);
        System.out.println("Confirm Quantity : "+ibConfirmQty);
        System.out.println("Parent Id : "+ibParentId);
        System.out.println("Li ID : "+iBliId);

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testAssignPutter() throws IOException {

        // Create a DTO object
        IBAssignPutterDtoListData assignPutterDto = new IBAssignPutterDtoListData();
        AssignPutterItemDTO assignPutter = new AssignPutterItemDTO();
        assignPutter.setPutDetailsId(Integer.parseInt(ibPutDetailsId));
        assignPutter.setPutId(Integer.parseInt(ibPutId));
        assignPutter.setPutterId(Integer.parseInt(userId));
        assignPutter.setPutTransId(Integer.parseInt(ibPutTransId));
        assignPutterDto.setAssignPutter(Collections.singletonList(assignPutter));

        // Send the POST request using RestAssured
        Response response = RestAssured.given()
                .headers(getHeaders())
                .contentType(ContentType.JSON)
                .body(assignPutterDto)
                .post("/assignPutter/putAssign")
                .then().assertThat().statusCode(200)
                .extract().response().prettyPeek();

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testPutConfirm() {
//
        // Create a DTO object
        IBPutConfirmDtoListData putConfirmDto = new IBPutConfirmDtoListData();
        ConfirmPutItemDTO confirmPutter = new ConfirmPutItemDTO();
        confirmPutter.setPutDetailsId(Integer.parseInt(ibPutDetailsId));
        confirmPutter.setPutId(Integer.parseInt(ibPutId));
        confirmPutter.setPutterId(Integer.parseInt(userId));
        confirmPutter.setPutTransId(Integer.parseInt(ibPutTransId));
        confirmPutter.setConfirmQty(Integer.parseInt(ibConfirmQty));
        confirmPutter.setParentId(Integer.parseInt(ibParentId));
        confirmPutter.setLiId(Integer.parseInt(iBliId));
        putConfirmDto.setConfirmPut(Collections.singletonList(confirmPutter));

        // Send the POST request using RestAssured
        Response response = RestAssured.given()
                .headers(getHeaders())
                .contentType(ContentType.JSON)
                .body(putConfirmDto)
                .post("/assignPutter/putConfirm").prettyPeek();

    }

}


