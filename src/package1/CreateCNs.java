package package1;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class CreateCNs extends BaseClass{
	@Test
	public void getfromAddress() {
		test = extent.createTest("getfromAddress");
		RestAssured.baseURI=baseURL;
		String CfromAddress= given().log().all().header("Content-Type","application/json")
				.header("Authorization","Bearer "+accessToken)
		.body(payload.getFromAddress()).log().all()
		.when().post("/backend/master/client_address")
		.then().log().all().assertThat().statusCode(200).body("status", equalTo("SUCCESS"))
		.extract().response().asString();
		
		
		JsonPath js1= reusableMethods.rawToJson(CfromAddress);
		consignorId =js1.getString("payload.id");
		System.out.println(consignorId);
		
	}
	@Test
	public void gettoAddress() {
		test = extent.createTest("gettoAddress");
		RestAssured.baseURI=baseURL;
		String CtoAddress= given().log().all().header("Content-Type","application/json")
				.header("Authorization","Bearer "+accessToken)
		.body(payload.getToAddress())
		.when().post("/backend/master/client_address")
		.then().log().all().assertThat().statusCode(200).body("status", equalTo("SUCCESS"))
		.extract().response().asString();
		
		
		JsonPath js2= reusableMethods.rawToJson(CtoAddress);
		consigneeId =js2.getString("payload.id");
		System.out.println(consigneeId);
		
	}
	@Test
	public void createCn() {
		test = extent.createTest("createCn");
		RestAssured.baseURI=baseURL;
		given().log().all().header("Content-Type","application/json")
				.header("Authorization","Bearer "+accessToken)
		.body(payload.createCn())
		.when().post("/backend/operations/consignments")
		.then().log().all().assertThat().statusCode(200).body("status", equalTo("SUCCESS"));
			


	}



}

