package package1;
import org.testng.annotations.Test;

import files.payload;
import files.reusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class retailCNs extends BaseClass{
	@Test()
	public void getfromAddress() {
		RestAssured.baseURI="https://zoom-kubernetes.stg.rivigo.com";
		String CfromAddress= given().log().all().header("Content-Type","application/json")
				.header("Authorization","Bearer "+accessToken)
		.body(payload.getFromAddress())
		.when().post("/arrow/backend/master/client_address")
		.then().log().all().assertThat().statusCode(200).body("status", equalTo("SUCCESS"))
		.extract().response().asString();
		
		
		JsonPath js1= reusableMethods.rawToJson(CfromAddress);
		consignorId =js1.getString("payload.id");
		System.out.println(consignorId);
		
	}
	@Test(priority=1)
	public void gettoAddress() {
		RestAssured.baseURI="https://zoom-kubernetes.stg.rivigo.com";
		String CtoAddress= given().log().all().header("Content-Type","application/json")
				.header("Authorization","Bearer "+accessToken)
		.body(payload.getToAddress())
		.when().post("/arrow/backend/master/client_address")
		.then().log().all().assertThat().statusCode(200).body("status", equalTo("SUCCESS"))
		.extract().response().asString();
		
		
		JsonPath js2= reusableMethods.rawToJson(CtoAddress);
		consigneeId =js2.getString("payload.id");
		System.out.println(consigneeId);
		
	}
	@Test(priority=2)
	public void createCn() {
		RestAssured.baseURI="https://zoom-kubernetes.stg.rivigo.com";
		given().log().all().header("Content-Type","application/json")
				.header("Authorization","Bearer "+accessToken)
		.body(payload.createRetailCn())
		.when().post("/arrow/backend/operations/consignments")
		.then().log().all().assertThat().statusCode(200).body("status", equalTo("SUCCESS"));
			
		
	}
	

}

