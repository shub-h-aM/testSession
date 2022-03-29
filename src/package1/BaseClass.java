package package1;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import files.PropertiesUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class BaseClass {
	
	    public static String accessToken=null;
	    public static String cNote=null;
	    public static String consignorId=null;
	    public static String consigneeId=null;
	    public static String projectdir = System.getProperty("user.dir");
	    protected static PropertiesUtils prop = null;
	    public static String baseURL = null;
	    public static String tokenURL = null;
	    public static String ClientId = null;
	    public static String ToPin = null;
	    public static String FromPin = null;
	    public static String numberOfBoxes = null;
	    public static String client_code = null;
	    
	     public ExtentHtmlReporter htmlReporter;
	        public ExtentReports extent;
	        public ExtentTest test;
	    
	    @BeforeClass
	    public void setUp() throws Exception{
	        initialiseProperties_Zoom();
	        getCnote();
	        setExtent();
	        
	        
	    }
	     public void setExtent()
	     {
	         htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/CreateReport.html");
	         htmlReporter.config().setDocumentTitle("Automation Report");//Title of the report
	         htmlReporter.config().setReportName("Functional Report"); //Report Name
	         htmlReporter.config().setTheme(Theme.DARK);
	         extent = new ExtentReports();
	         extent.attachReporter(htmlReporter);
	         extent.setSystemInfo("OS", "Linux");
	         extent.setSystemInfo("Browser", "Chrome");
	         extent.setSystemInfo("Author", "Ravi" +"");
	     }

	     @AfterClass
	     public void endReport ()
	     {
	         extent.flush();
	     }

	     @AfterMethod
	     public void tearDown (ITestResult result) throws IOException {
	         if (result.getStatus() == ITestResult.FAILURE) {
	             test.log(Status.FAIL, "TEST CASE FAILED IS " + result.getName()); // to add name in extent report
	             test.log(Status.FAIL, "TEST CASE FAILED IS " + result.getThrowable()); // to add error/exception in extent report
	         } else if (result.getStatus() == ITestResult.SKIP) {
	             test.log(Status.SKIP, "Test Case SKIPPED IS " + result.getName());
	         } else if (result.getStatus() == ITestResult.SUCCESS) {
	             test.log(Status.PASS, "Test Case PASSED IS " + result.getName());
	         }

	     }
	    @Parameters({"username", "password"})
	    @Test
	    public void getAccessToken(String username, String password) {
	    	test = extent.createTest("getAccessToken");
	        String user =username;
	        String pwd =password;
	        RestAssured.baseURI=tokenURL;
	         String generateAccessToken = given().log().all().header("content-type", "application/x-www-form-urlencoded")
	        .formParam("client_id","sso")
	        .formParam("username",user)
	        .formParam("password",pwd)
	        .formParam("grant_type","password")
	        .formParam("session_reset","no")
	        .when().post()
	        .then().log().all().assertThat().statusCode(200)
	        .extract().response().asString();
	            
	            JsonPath js= new JsonPath(generateAccessToken);                               //reusableMethods.rawToJson(generateAccessToken);
	            accessToken = js.get("response.access_token");
	            System.out.println(accessToken);
	        
	        }
	    
	             
	    
	
	    public static void getCnote() {
	    	

	          System.out.println("Getting time in milliseconds in Java 11: " + 
	          ZonedDateTime.now().toInstant().toEpochMilli());
	          long timeinstamp = ZonedDateTime.now().toInstant().toEpochMilli();
	          Long cnote= timeinstamp/1000;

	          cNote = Long.toString(cnote);
	          System.out.println(cNote);
//	        return cNote;
	          
	    }
	    @Test
	    public static void initialiseProperties_Zoom() throws Exception {
	           prop = new PropertiesUtils();
	           prop.setPropertyFile(projectdir+"/resources/application.properties");
	           baseURL=prop.gettingValueOfProperty("ZoomBaseUrl");
	           ClientId=prop.gettingValueOfProperty("ClientId");
	           ToPin=prop.gettingValueOfProperty("ToPin");
	           FromPin=prop.gettingValueOfProperty("FromPin");
	           numberOfBoxes=prop.gettingValueOfProperty("NumberOfBoxes");
	           client_code=prop.gettingValueOfProperty("client_code");
//	         
	           tokenURL=prop.gettingValueOfProperty("login.stg.url");
	        }
	    
	    //Extent report

	   
	    
	

}
