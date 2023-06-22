package utilities;

import com.relevantcodes.extentreports.LogStatus;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public class BaseClassAPI {
    public static String dir = System.getProperty("user.dir");
    public static String downloadFilepath = dir + "/temp";
    protected static Utility util = null;
    public static String authToken = null;
    public static String warehouseId = null;
    public static String userId = null;
    public static String itemCode = null;
    public static String uomId = null;
    public static String qty = null;
    public static String lotNo = null;
    public static String batchNo = null;


    public static String baseURL = null;

    /* WMS application properties */
    public static Map<String, String> wmsHeaders = null;



    @BeforeMethod
    public void beforeMethod(Method method) {
        ExtentTestManager.startTest(method.getName());


    }

    @AfterMethod
    public void tearDownAfterEveryMethod(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
        }
        ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
        ExtentManager.getReporter().flush();
    }
    public static void initialiseProperties_Wms() throws Exception {
        util = new Utility();
        util.setPropertyFile(dir + "/config/Wms.properties");
        baseURL = util.gettingValueOfProperty("WmsBaseUrl");
        authToken = util.gettingValueOfProperty("AuthorizationToken");
        warehouseId = util.gettingValueOfProperty("WarehouseId");
        userId = util.gettingValueOfProperty("userId");
        itemCode = util.gettingValueOfProperty("itemCode");
        uomId = util.gettingValueOfProperty("uomId");
        qty = util.gettingValueOfProperty("Quantity");
        lotNo = util.gettingValueOfProperty("LotNumber");
        batchNo = util.gettingValueOfProperty("BatchNumber");


    }
    protected Headers getHeaders() {

        Header header1 = new Header("Authorization", "Bearer "+authToken);
        Header header2 = new Header("Content-Type", "application/json");
        Header header3 = new Header("Accept", "application/json, text/plain, */*");


        return new Headers(header1, header2,header3);
    }

}
