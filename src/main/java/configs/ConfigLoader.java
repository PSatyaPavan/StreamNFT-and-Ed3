package configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    public static final String METAMASK_EXTENSION_PATH;
    public static final String CHROME_USER_DATA_DIR;
    public static final String METAMASK_PASSWORD;
    public static final boolean ENABLE_LEND;
    public static final boolean ENABLE_OFFER;
    public static final boolean ENABLE_BORROW;
    public static final boolean ENABLE_LOANS;

    static {
        // Load the properties file from the resources folder
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties in the resources folder");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        // Retrieve the values from the properties file
        METAMASK_EXTENSION_PATH = properties.getProperty("METAMASK_EXTENSION_PATH");
        CHROME_USER_DATA_DIR = properties.getProperty("CHROME_USER_DATA_DIR");
        METAMASK_PASSWORD = properties.getProperty("METAMASK_PASSWORD");
        ENABLE_LEND = Boolean.parseBoolean(properties.getProperty("ENABLE_LEND", "true"));
        ENABLE_OFFER = Boolean.parseBoolean(properties.getProperty("ENABLE_OFFER", "true"));
        ENABLE_BORROW = Boolean.parseBoolean(properties.getProperty("ENABLE_BORROW", "true"));
        ENABLE_LOANS = Boolean.parseBoolean(properties.getProperty("ENABLE_LOANS", "true"));

        // Validate that the required properties are not missing
        if (METAMASK_EXTENSION_PATH == null || CHROME_USER_DATA_DIR == null || METAMASK_PASSWORD == null) {
            throw new IllegalStateException("Missing required properties in config.properties");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
