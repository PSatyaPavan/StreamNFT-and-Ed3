package Automationtesting;

import base.BaseTest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import configs.ConfigLoader;
import org.testng.annotations.Test;
import java.awt.*;
import java.nio.file.Paths;
import java.util.List;

public class LoanTestCases extends BaseTest {

    @Test
    public void VerifyTheDAPP_Application_With_LoanSection() throws InterruptedException {
        Playwright playwright = Playwright.create();

        String pathToExtension = ConfigLoader.METAMASK_EXTENSION_PATH;
        String userDataDir = ConfigLoader.CHROME_USER_DATA_DIR;
        BrowserType.LaunchPersistentContextOptions options = new BrowserType.LaunchPersistentContextOptions()
                .setHeadless(false)
                .setArgs(List.of(
                        "--disable-extensions-except=" + pathToExtension,
                        "--load-extension=" + pathToExtension))
                .setChannel("chrome");

        // Launch browser with persistent context
        BrowserContext browserContext = playwright.chromium().launchPersistentContext(
                Paths.get(userDataDir),
                options
        );

        // Maximize browser window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Page firstPage = browserContext.pages().get(0);
        firstPage.evaluate("window.moveTo(0, 0)");
        firstPage.evaluate("window.resizeTo(screen.width, screen.height)");
        browserContext.pages().get(0).setViewportSize(width, height);
        System.out.println("Browser window maximized to width: " + width + ", height: " + height);

        browserContext.setDefaultTimeout(60000);
        browserContext.clearCookies();

        // Unlock MetaMask
        Page metamaskExtensionPage = browserContext.pages().get(0);
        metamaskExtensionPage.navigate("chrome-extension://nkbihfbeogaeaoehlefnkodbefgpgknn/home.html#");
        metamaskExtensionPage.locator("//input[@data-testid='unlock-password']").fill("Saty@9618");
        metamaskExtensionPage.locator("[data-testid='unlock-submit']").click();

        // Test both applications with different base URLs
        testApplication(browserContext, "https://dev.streamnft.tech/open%20campus");
        testApplication(browserContext, "https://dev.ed3.xyz/");     //https://dev.ed3.xyz/loan/loans

        System.out.println("All steps completed successfully for both applications.");
    }

    private void testApplication(BrowserContext browserContext, String baseURL) throws InterruptedException {
        System.out.println("Testing application with base URL: " + baseURL);

        Page testPage = browserContext.newPage();

        // Connect wallet
        testPage.navigate(baseURL + "/loan/lend");
        Locator connectBtn = testPage.locator("button[id='connect-button']");
        connectBtn.first().click();
        Locator metamaskBtn = testPage.locator("//div/div[contains(text(),'MetaMask')]");
        Page extensionPage = browserContext.waitForPage(metamaskBtn::click);
        Locator metaconnectBtn = extensionPage.locator("[data-testid='confirm-btn']");
        metaconnectBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        metaconnectBtn.click();
        Locator signInButton = testPage.locator("//div[text()='Sign message']");
        signInButton.click();
        Locator confirmBtn = extensionPage.locator("[data-testid='confirm-footer-button']");
        confirmBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        confirmBtn.click();
        System.out.println("Transaction confirmed in MetaMask.");

        Thread.sleep(5000);

        // Perform lending actions if enabled
        if (ConfigLoader.ENABLE_LEND) {
            System.out.println("Performing LEND actions...");
            testPage.navigate(baseURL + "/loan/lend");
            Locator lendButton = testPage.locator("#lend-button-1");
            lendButton.click();
            Locator amountField = testPage.locator("input[placeholder='Enter Offer amount here']");
            amountField.fill("0.001");
            Locator lendButton1 = testPage.locator("#Lend");
            lendButton1.click();
            handleMetaMaskConfirmation(browserContext);
        }

        // Perform offer actions if enabled
        if (ConfigLoader.ENABLE_OFFER) {
            System.out.println("Performing OFFER actions...");
            testPage.navigate(baseURL + "/loan/offers");
            Locator editButton = testPage.locator("//img[@alt='edit']");
            editButton.click();
            Locator updateButton = testPage.locator("#update-offers");
            updateButton.click();
            Locator offersField = testPage.locator("input[placeholder='Enter the number of offers']");
            offersField.fill("2");
            Locator saveButton = testPage.locator("//*[text()='Save']");
            saveButton.click();
            handleMetaMaskConfirmation(browserContext);
        }

        // Perform borrowing actions if enabled
        if (ConfigLoader.ENABLE_BORROW) {
            System.out.println("Performing BORROW actions...");
            testPage.navigate(baseURL + "/loan/borrow");
            Locator borrowButton = testPage.locator("#borrow-button-1");
            borrowButton.click();
            Locator popup = testPage.locator("//h5[text()='EduVerse Early Adopter']");
            popup.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            Locator nftButton = testPage.locator("//img[@alt='item img']");
            nftButton.click();
            Locator borrowCapitalButton = testPage.locator("#BorrowCapital");
            borrowCapitalButton.click();
            handleMetaMaskConfirmation(browserContext);
            handleMetaMaskConfirmation(browserContext);
        }

        // Perform loan actions if enabled
        if (ConfigLoader.ENABLE_LOANS) {
            System.out.println("Performing LOANS actions...");
            testPage.navigate(baseURL + "/loan/loans");
            Locator repayButton = testPage.locator("(//button[contains(text(),'Repay')])[1]");
            repayButton.click();
            Locator repayLoanButton = testPage.locator("//button[normalize-space()='Repay Loan']");
            repayLoanButton.click();
            handleMetaMaskConfirmation(browserContext);
        }

        System.out.println("Tests completed for base URL: " + baseURL);
    }


    private void handleMetaMaskConfirmation(BrowserContext browserContext) throws InterruptedException {
        Thread.sleep(5000);
        Page extensionPage = browserContext.waitForPage(() -> {
            System.out.println("Waiting for MetaMask confirmation popup...");
        });
        Locator confirmButton = extensionPage.locator("[data-testid='confirm-footer-button']");
        confirmButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        confirmButton.click();
        System.out.println("MetaMask confirmation completed.");
    }
}
