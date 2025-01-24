package Automationtesting;

import base.BaseTest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.Test;
import java.awt.*;
import java.nio.file.Paths;
import java.util.List;

public class RentTestcasesED3 {


        @Test
        public void VerifyTheDAPP_Application_With_RentSection() throws InterruptedException {
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
            testnetpage.navigate("https://testnet.ed3.xyz/rent");

            // Connect wallet
            Locator connectBtn = testnetpage.locator("button[id='connect-button']");
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

            testnetpage.navigate("https://testnet.ed3.xyz/rent");

            // Wait for transaction to complete
            Locator nftElement = testnetpage.locator("//h4[text()='EduVerse Early Adopter']");
            nftElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            nftElement.click();

            // Navigate to my assets page
            testnetpage.navigate("https://testnet.ed3.xyz/rent/EduVerse-Early-Adopter/myassets");

            // Perform Lend actions
            // Click on the first 'Lend' button
            testnetpage.locator("//span[text()='Lend']").first().click();

            Locator lendDurationInput = testnetpage.locator("//input[@placeholder='Lend Duration']");
            lendDurationInput.fill("2");

            Locator priceInput = testnetpage.locator("//input[@placeholder='Price']");
            priceInput.fill("7");

            testnetpage.locator("//button[contains(text(),'Lend')]").click();

            // Handle MetaMask confirmation (2 popups)
            handleMetaMaskConfirmation(browserContext);
            handleMetaMaskConfirmation(browserContext);

            // Refresh the page and confirm lending
            // testnetpage.reload();

            // Cancel the lend action
            Locator cancelButton = testnetpage.locator("//span[normalize-space()='0.010 EDU/HR']");
            cancelButton.click();


            // Confirm the cancel action
            Locator cancelDiv = testnetpage.locator("//button[contains(text(),'Cancel')]");
            cancelDiv.click();

            handleMetaMaskConfirmation(browserContext);

            Thread.sleep(5000);

            // Perform Owned button actions
            Locator ownedButton = testnetpage.locator("//h5[@class='font-numans font-semibold text-[14px] leading-[20px] transition-all duration-200 text-electric-violet' and text()='Owned']");
            ownedButton.click();

            // Repeat Lend process
            testnetpage.locator("//span[text()='Lend']").first().click();
            lendDurationInput.fill("2");
            priceInput.fill("7");
            testnetpage.locator("//button[contains(text(),'Lend')]").click();

            // Handle MetaMask confirmation (2 popups)
            handleMetaMaskConfirmation(browserContext);
            handleMetaMaskConfirmation(browserContext);

            // Navigate to the marketplace
            testnetpage.locator("//h5[text()='Marketplace']").click();

            // Perform Rent actions
            Locator rentButton = testnetpage.locator("//span[text()='7.000 USDC/HR']");
            rentButton.first().click();

            Locator durationInput = testnetpage.locator("//input[@placeholder='Duration']");
            durationInput.fill("1");

            Thread.sleep(5000);
            testnetpage.locator("//div[contains(text(),'Rent')]").click();

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








