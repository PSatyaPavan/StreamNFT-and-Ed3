package configs;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String INVALID_PROPERTY_LOG_MESSAGE = "Invalid property name #";
    private static final String EXCEPTION_MESSAGE= "Please enter valid property name";

    public ConfigReader() {

    }

    public static String getProperty(String propertyName) {
        return readProperty(propertyName);
    }

    private static void loadProperties() {
        try {
            properties.load(ConfigReader.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            // log.error(e.getMessage(),e);
            throw new RuntimeException("Issue in loading properties",e);
        }
    }

    private static String readProperty(String propertyName) {
        propertyName = Objects.requireNonNullElse(propertyName, "");
        if (propertyName.equals("")) {
            //log.error(INVALID_PROPERTY_LOG_MESSAGE + propertyName);
            throw new RuntimeException(EXCEPTION_MESSAGE,new RuntimeException());
        }
        loadProperties();
        return properties.getProperty(propertyName);
    }

    public String getApplication(){
        String application=System.getenv("APPLICATION");
        if(application==null){
            application=properties.getProperty("APPLICATION");
        }
        if (application!=null && !application.trim().isEmpty()){
            return application;
        }else {
            throw new RuntimeException("application not specified in the config,properties file");

        }
    }




}
