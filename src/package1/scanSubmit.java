package package1;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class scanSubmit {
	@Test
	public void ScanUnload() {
		RestAssured.baseURI= "https://zoom-kubernetes.stg.rivigo.com";
		given().log().all().header("Conetent-Type","application/json")
		.header("Authorization","Bearer 830cb047-6f44-45cc-bc6b-23b20143e916")
		.body(payload.submitCN())
		.when().put("/walker/wms/tasks/1117/sync")
		.then().log().all().assertThat().statusCode(200);
		
	}

}
