package Automationtesting;



import base.BaseTest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import configs.ConfigLoader;
import org.testng.annotations.Test;
import utills.MetamaskHelper;

import java.awt.*;
import java.nio.file.Paths;
import java.util.List;

public class RentTestCases extends BaseTest {

    @Test
    public void VerifyTheDAPP_Application_With_RentSection() throws InterruptedException {
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
        MetamaskHelper.unlockMetaMask(metamaskExtensionPage);

       /* metamaskExtensionPage.navigate("chrome-extension://nkbihfbeogaeaoehlefnkodbefgpgknn/home.html#");
        metamaskExtensionPage.locator("//input[@data-testid='unlock-password']").fill("Saty@9618");
        metamaskExtensionPage.locator("[data-testid='unlock-submit']").click();
*/
        // Test both applications with different base URLs
        testApplication(browserContext, "https://dev.streamnft.tech/open%20campus");
        testApplication(browserContext, "https://dev.ed3.xyz/");

        System.out.println("All steps completed successfully for both applications.");
    }



        private void testApplication(BrowserContext browserContext, String baseURL) throws InterruptedException {
            System.out.println("Testing application with base URL: " + baseURL);

            Page testnetpage = browserContext.newPage();

            // Connect wallet
        Locator connectBtn = testnetpage.locator("//div[@class='flex items-center justify-center flex-row font-numans']//button[@type='button'][normalize-space()='Connect']");
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

        testnetpage.navigate(baseURL + "/rent/");

        // Wait for transaction to complete
        Locator nftElement = testnetpage.locator("//h4[@id='EduVerse Early Adopter']");
        nftElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        nftElement.click();

        // Navigate to my assets page
        testnetpage.navigate(baseURL + "/rent/EduVerse-Early-Adopter/myassets/");

        // Perform Lend actions
        // Click on the first 'Lend' button
        testnetpage.locator("#Lend-1").click();

        Locator lendDurationInput = testnetpage.locator("#lend-duration-input");
        lendDurationInput.fill("2");

        Locator priceInput = testnetpage.locator("#price-input");
        priceInput.fill("0.001");

        testnetpage.locator("#Lend").click();

        // Handle MetaMask confirmation (2 popups)
        handleMetaMaskConfirmation(browserContext);
        handleMetaMaskConfirmation(browserContext);


        Thread.sleep(10000);

        // Cancel the lend action
        Locator cancelButton = testnetpage.locator("#cancel-1");
        cancelButton.click();


        // Confirm the cancel action
        Locator cancelDiv = testnetpage.locator("#Cancel");
        cancelDiv.click();

        handleMetaMaskConfirmation(browserContext);

        Thread.sleep(5000);

        // Perform Owned button actions
        Locator ownedButton = testnetpage.locator("#Owned");
        ownedButton.click();

        // Repeat Lend process
        testnetpage.locator("#Lend-1").click();
        lendDurationInput.fill("2");
        priceInput.fill("0.001");
        testnetpage.locator("#Lend").click();

        // Handle MetaMask confirmation (2 popups)
        handleMetaMaskConfirmation(browserContext);
        handleMetaMaskConfirmation(browserContext);

        // Navigate to the marketplace
        testnetpage.locator("#Marketplace").click();

        // Perform Rent actions
        Locator rentButton = testnetpage.locator("#Rent-2");
        rentButton.first().click();

        Locator durationInput = testnetpage.locator("#duration-input");
        durationInput.fill("1");

        Thread.sleep(5000);
        testnetpage.locator("#Rent").click();

        // Handle MetaMask confirmation (2 popups)
        handleMetaMaskConfirmation(browserContext);
        handleMetaMaskConfirmation(browserContext);

        Thread.sleep(5000);
        testnetpage.locator("//h5[text()='Marketplace']").click();
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




