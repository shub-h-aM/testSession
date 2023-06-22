package utilities;

    import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

    public class ExtentReportUtil {
        private static ExtentReports extentReports;
        private static ExtentTest extentTest;

        public static void initializeExtentReport(String reportFilePath) {
            extentReports = new ExtentReports();
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportFilePath);
            extentReports.attachReporter(htmlReporter);
        }

        public static void startTest(String testName) {
            extentTest = extentReports.createTest(testName);
        }

        public static void logInfo(String message) {
            extentTest.log(Status.INFO, message);
        }

        public static void logError(String errorMessage) {
            extentTest.log(Status.ERROR, errorMessage);
        }

        public static void logPass(String message) {
            extentTest.log(Status.PASS, message);
        }

        public static void logFail(String errorMessage) {
            extentTest.log(Status.FAIL, errorMessage);
        }

        public static void endTest() {
            extentReports.flush();
        }
    }


