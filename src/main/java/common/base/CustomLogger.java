package common.base;

import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import reporting.ExtentReportManager;

public class CustomLogger {
    public org.apache.logging.log4j.Logger log;

    public CustomLogger(String className) {
        log = LogManager.getLogger(className);
    }

    public void info(String message){
        log.info(message);
        ExtentReportManager.getTest().log(Status.INFO,message);
    }
}
