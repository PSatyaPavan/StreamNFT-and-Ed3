package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
//import org.testng.annotations.AfterTest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentReportManager {
    private static final ExtentReports extent = new ExtentReports();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static synchronized ExtentTest getTest() {
        return extentTest.get();
    }

    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.createTest(testName, desc);
        extentTest.set(test);
        return test;
    }


    public static synchronized ExtentReports getExtent() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-ddhhmmss").format(new Date());
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("./src/resources/reports/extent-report-" + timeStamp + ".html");
        // ExtentSparkReporter htmlReporter = new ExtentSparkReporter("C:\\Users\\DELL\\IdeaProjects\\TestDrivenFramework\\src\\resources\\reports" + timeStamp + ".html");
        extent.attachReporter(htmlReporter);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("DAPPTestProject");
        htmlReporter.config().setReportName("StreamDAPP Automation Report");
        extent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        return extent;
    }



//    @AfterTest
//    public static void flushExtent() {
//        extent.flush();
//    }
}
