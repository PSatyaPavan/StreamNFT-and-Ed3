package Automationtesting;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.Test;
import java.awt.*;
import java.nio.file.Paths;
import java.util.List;

public class UtilityTestcases {

    @Test
    public void VerifyTheDAPP_Application_With_UtilitySection() throws InterruptedException {
        Playwright playwright = Playwright.create();
        String pathToExtension = Paths.get("C:/Metamask/Playwright/nkbihfbeogaeaoehlefnkodbefgpgknn/12.9.3_0")
                .toAbsolutePath().toString();
        String userDataDir = "C:\\Users\\DELL\\AppData\\Local\\Google\\Chrome\\User Data";

        BrowserType.LaunchPersistentContextOptions options = new BrowserType.LaunchPersistentContextOptions()
                .setHeadless(false) // Set to false to see the browser window
                .setArgs(List.of(
                        "--disable-extensions-except=" + pathToExtension,
                        "--load-extension=" + pathToExtension)) // Add extension path
                .setChannel("chrome"); // Optional, to launch a specific version/channel of Chromium

        // Launch the browser with a persistent context
        BrowserContext browserContext = playwright.chromium().launchPersistentContext(
                Paths.get(userDataDir), // Path to user data directory
                options // Pass the configured options
        );

        // Maximize the browser window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Page firstPage = browserContext.pages().get(0);
        firstPage.evaluate("window.moveTo(0, 0)");
        firstPage.evaluate("window.resizeTo(screen.width, screen.height)");
        browserContext.pages().get(0).setViewportSize(width, height);
        System.out.println("Browser window maximized to width: " + width + ", height: " + height);

        browserContext.setDefaultTimeout(60000); // Increased timeout
        browserContext.clearCookies();

        // Open MetaMask extension and unlock
        Page metamaskextentionPage = browserContext.pages().get(0);
        metamaskextentionPage.navigate("chrome-extension://nkbihfbeogaeaoehlefnkodbefgpgknn/home.html#");
        metamaskextentionPage.locator("//input[@data-testid='unlock-password']").fill("Saty@9618");
        metamaskextentionPage.locator("[data-testid='unlock-submit']").click();

        // Navigate to DApp
        Page testnetpage = browserContext.newPage();
        testnetpage.navigate("https://dev.streamnft.tech/skale%20nebula/loan/lend");

        // Connect wallet
        Locator connectBtn = testnetpage.locator("//button[@id='connect-button']");
        connectBtn.click();
        Locator metamaskBtn = testnetpage.locator("//div/div[contains(text(),'MetaMask')]");

        // Wait for MetaMask connection page
        Page extensionPage = browserContext.waitForPage(metamaskBtn::click);
        Locator metaconnectBtn = extensionPage.locator("[data-testid='confirm-btn']");
        metaconnectBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        metaconnectBtn.click();

        // Handle Sign Message in DApp
        Locator signinButton = testnetpage.locator("//div[text()='Sign message']");
        signinButton.click();

        // Handle Confirm Button in MetaMask for Transaction
        Locator confirmBtn = extensionPage.locator("[data-testid='confirm-footer-button']");
        confirmBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        confirmBtn.click();
        System.out.println("Transaction confirmed in MetaMask.");

        // Navigate to Loan/Lend page
        Thread.sleep(5000);

        testnetpage.navigate("https://dev.streamnft.tech/utility/create");


        Locator NextButton = testnetpage.locator("//button[text()='Next']");
        NextButton.first().click();

       /* Locator checkbox = testnetpage.locator("//input[@type='checkbox']");
        checkbox.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        checkbox.first().click();
        System.out.println("Checkbox clicked after waiting.");*/


        Locator checkboxLabel = testnetpage.locator("//input[@type='checkbox' and ancestor::div//img[@alt='Quest']]");
        checkboxLabel.first().click();
        System.out.println("Checkbox for 'Quest' clicked.");



    }

    }
