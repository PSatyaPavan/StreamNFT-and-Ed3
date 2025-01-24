package base;

import listeners.TestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import reporting.ExtentReportManager;
import com.microsoft.playwright.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;
    protected static Page page;

    @BeforeClass
    public void setUp() {
        // Initialize Playwright and browser
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        // Create a browser context
        browserContext = browser.newContext();

        // Create a new page
        page = browserContext.newPage();
        System.out.println("Playwright setup completed.");
    }

    @BeforeMethod
    public void beforeMethod(ITestResult iTestResult) {
        // Start ExtentReport for the current test
        ExtentReportManager.startTest(iTestResult.getMethod().getMethodName());
    }

    public static void captureScreenshot(Page currentPage) {
        try {
            // Use Playwright's screenshot functionality
            String timestamp = new SimpleDateFormat("MMddHHmmss").format(new Date());
            String screenshotPath = "src/resources/reports/screenshots/screenshot_" + timestamp + ".png";

            // Save the screenshot
            currentPage.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
            System.out.println("Screenshot saved: " + screenshotPath);
        } catch (Exception e) {
            throw new RuntimeException("Error while taking the screenshot", e);
        }
    }


    @AfterClass
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        ExtentReportManager.getExtent().flush(); // Generate reports
    }


}

