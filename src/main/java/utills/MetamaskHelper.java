package utills;
import configs.ConfigLoader;
import com.microsoft.playwright.Page;


public class MetamaskHelper {
    private static final String METAMASK_EXTENSION_URL = ConfigLoader.METAMASK_EXTENSION_PATH;
    private static final String METAMASK_PASSWORD = ConfigLoader.getProperty("METAMASK_PASSWORD");

    public static void unlockMetaMask(Page metamaskPage) {
        System.out.println("Navigating to MetaMask extension...");
        metamaskPage.navigate(METAMASK_EXTENSION_URL);

        System.out.println("Filling in the MetaMask password...");
        metamaskPage.locator("//input[@data-testid='unlock-password']").fill(METAMASK_PASSWORD);

        System.out.println("Submitting the MetaMask unlock form...");
        metamaskPage.locator("[data-testid='unlock-submit']").click();

        System.out.println("MetaMask unlocked successfully!");
    }
}
