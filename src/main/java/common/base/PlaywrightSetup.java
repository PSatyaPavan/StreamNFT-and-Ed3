package common.base;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.List;

public class PlaywrightSetup {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    private static final String METAMASK_EXTENSION_PATH = "C:/Metamask/Playwright/nkbihfbeogaeaoehlefnkodbefgpgknn/12.9.3_0";
    private static final String USER_DATA_DIR = "C:/Users/DELL/AppData/Local/Google/Chrome/User Data";

    // Method to get BrowserContext
    public static BrowserContext getBrowserContext() {
        if (browserContext == null) {
            initializePlaywrightWithMetaMask();
        }
        return browserContext;
    }

    // Method to initialize Playwright with MetaMask extension
    private static void initializePlaywrightWithMetaMask() {
        playwright = Playwright.create();
        BrowserType.LaunchPersistentContextOptions options = new BrowserType.LaunchPersistentContextOptions()
                .setHeadless(false) // Change to true for headless execution
                .setArgs(List.of(
                        "--disable-extensions-except=" + METAMASK_EXTENSION_PATH,
                        "--load-extension=" + METAMASK_EXTENSION_PATH))
                .setChannel("chrome");

        browserContext = playwright.chromium().launchPersistentContext(Paths.get(USER_DATA_DIR), options);
    }

    // Method to unlock MetaMask
    public static void unlockMetaMask(BrowserContext browserContext, String password) {
        try {
            Page metaMaskPage = browserContext.newPage();
            metaMaskPage.navigate("chrome-extension://nkbihfbeogaeaoehlefnkodbefgpgknn/home.html#");
            metaMaskPage.locator("//input[@data-testid='unlock-password']").fill(password);
            metaMaskPage.locator("[data-testid='unlock-submit']").click();
            System.out.println("MetaMask unlocked successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to unlock MetaMask: " + e.getMessage(), e);
        }
    }

    // Method to close Playwright and BrowserContext
    public static void closeBrowserContext() {
        if (browserContext != null) {
            browserContext.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
