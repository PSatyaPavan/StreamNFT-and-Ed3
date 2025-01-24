package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.microsoft.playwright.Page;
import common.base.PlaywrightSetup;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reporting.ExtentReportManager;
import com.microsoft.playwright.BrowserContext;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentReportManager.getExtent();

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("Execution Started: " + iTestResult.getMethod().getMethodName());
        ExtentReportManager.getTest().pass("Test Execution Started: " + iTestResult.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("Test Passed: " + iTestResult.getMethod().getMethodName());
        ExtentReportManager.getTest().pass("Test Passed: " + iTestResult.getMethod().getMethodName());
    }


    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("Test Failed: " + iTestResult.getMethod().getMethodName());
        ExtentReportManager.getTest().fail("Test Failed: " + iTestResult.getMethod().getMethodName());
        ExtentReportManager.getTest().fail(iTestResult.getThrowable());

        BrowserContext browserContext = PlaywrightSetup.getBrowserContext();
        if (browserContext != null) {
            Page page = browserContext.pages().get(0); // Assuming the first page
            String timestamp = new SimpleDateFormat("MMddHHmmss").format(new Date());
            String screenshotPath = "src/resources/reports/screenshots/screenshot_" + timestamp + ".png";

            try {
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
                System.out.println("Screenshot captured at: " + screenshotPath);
                ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath, "Screenshot on Failure");
            } catch (Exception e) {
                System.err.println("Error capturing screenshot: " + e.getMessage());
            }
        } else {
            System.err.println("Browser context is null. Unable to capture screenshot.");
        }
    }


    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("Test Skipped: " + iTestResult.getMethod().getMethodName());
        ExtentReportManager.getTest().skip("Test Skipped: " + iTestResult.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("Test Suite Started: " + iTestContext.getName());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("Test Suite Finished: " + iTestContext.getName());
        ExtentReportManager.getExtent().flush();
    }
}
