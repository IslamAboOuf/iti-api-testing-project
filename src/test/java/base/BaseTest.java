package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    public static final String BASE_URL_LOCAL  = "http://localhost:5091";
    public static final String BASE_URL_REMOTE = "https://iti-exam-system.runasp.net";

    protected static final int VALID_STUDENT_ID = 2;
    protected static final int VALID_INS_ID     = 1;
    protected static final int VALID_COURSE_ID  = 10;
    protected static final int INVALID_ID       = 999;

    public static int examId;
    public static int studentExamId;

    // Extent Report
    public static ExtentReports extent;
    public static ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        // إنشاء الـ Report
        ExtentSparkReporter spark =
                new ExtentSparkReporter("reports/TestReport.html");

        spark.config().setReportName("ITI Examination System — API Test Report");
        spark.config().setDocumentTitle("API Testing Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Project",     "ITI Examination System");
        extent.setSystemInfo("Tester",      "Islam Eldosoky");
        extent.setSystemInfo("Environment", "Remote");
        extent.setSystemInfo("Base URL",    BASE_URL_REMOTE);
    }

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL_REMOTE;
    }

    @AfterMethod
    public void printResult(ITestResult result) {
        String tcName = result.getMethod().getMethodName();

        System.out.println("\n========================================");
        if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("✅ PASSED: " + tcName);
            if (test != null) test.pass("✅ Test Passed");

        } else if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("❌ FAILED: " + tcName);
            System.out.println("   Reason: " + result.getThrowable().getMessage());
            if (test != null) test.fail("❌ " + result.getThrowable().getMessage());

        } else if (result.getStatus() == ITestResult.SKIP) {
            System.out.println("⏭️ SKIPPED: " + tcName);
            if (test != null) test.skip("⏭️ Test Skipped");
        }
        System.out.println("========================================\n");
    }

    @AfterSuite
    public void tearDown() {
        if (extent != null) extent.flush();
        System.out.println("\n📊 Report saved: reports/TestReport.html");
    }
}