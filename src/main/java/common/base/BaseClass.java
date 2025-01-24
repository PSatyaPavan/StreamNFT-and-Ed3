package common.base;

import com.microsoft.playwright.*;

import java.awt.*;
import java.nio.file.Paths;
import java.util.List;

public class BaseClass {

    protected Playwright playwright;
    protected BrowserContext browserContext;
    protected Page page;

    public void setUp() throws Exception {
        playwright = Playwright.create();
        String pathToExtension = Paths.get("C:/Metamask/Playwright/nkbihfbeogaeaoehlefnkodbefgpgknn/12.9.3_0")
                .toAbsolutePath().toString();
        String userDataDir = "C:\\Users\\DELL\\AppData\\Local\\Google\\Chrome\\User Data";

        BrowserType.LaunchPersistentContextOptions options = new BrowserType.LaunchPersistentContextOptions()
                .setHeadless(false) // Set to false to see the browser window
                .setArgs(List.of(
                        "--disable-extensions-except=" + pathToExtension,
                        "--load-extension=" + pathToExtension)) // Add extension path
                .setChannel("chrome"); // Optional, to launch a specific version/channel of Chromium

        browserContext = playwright.chromium().launchPersistentContext(
                Paths.get(userDataDir), // Path to user data directory
                options // Pass the configured options
        );

        // Maximize the browser window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        page = browserContext.pages().get(0);
        page.evaluate("window.moveTo(0, 0)");
        page.evaluate("window.resizeTo(screen.width, screen.height)");
        browserContext.pages().get(0).setViewportSize(width, height);
        System.out.println("Browser window maximized to width: " + width + ", height: " + height);

        browserContext.setDefaultTimeout(20000); // Increased timeout
        browserContext.clearCookies();
    }

  /*  public void tearDown(boolean b) {
        if (browserContext != null) {
            browserContext.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }*/

    protected void navigateTo(String url) {
        System.out.println("Navigating to URL: " + url);
        page.navigate(url);
    }
}
