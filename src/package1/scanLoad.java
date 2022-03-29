package package1;

import org.testng.annotations.Test;

import files.payload;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;

public class scanLoad {
	@Test
	public void ScanLoadUnload() {
		RestAssured.baseURI= "https://zoom-kubernetes.stg.rivigo.com";
		given().log().all().header("Conetent-Type","application/json")
		.header("Authorization","Bearer 830cb047-6f44-45cc-bc6b-23b20143e916")
		.body(payload.loadUnloadCN())
		.when().put("/walker/wms/tasks/1117/sync")
		.then().log().all().assertThat().statusCode(200);
		
	}
	

}
