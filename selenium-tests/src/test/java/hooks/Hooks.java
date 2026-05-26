package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Hooks {
    private static Properties properties;

    @Before
    public void setup() {
        loadProperties();
        
        String browser = properties.getProperty("browser", "chrome");
        boolean headless = "true".equalsIgnoreCase(properties.getProperty("headless"));
        int implicitWait = Integer.parseInt(properties.getProperty("implicitWait", "10"));
        
        WebDriver driver = DriverManager.initDriver(browser, headless, implicitWait);
        driver.get(properties.getProperty("baseUrl", "http://localhost:3000"));
    }

    @After
    public void teardown() {
        DriverManager.quitDriver();
    }

    public static WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    private void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Could not load config.properties. Using default values.");
                properties.setProperty("baseUrl", "http://localhost:3000");
                properties.setProperty("implicitWait", "10");
                properties.setProperty("adminEmail", "adarsht072@gmail.com");
                properties.setProperty("adminPassword", "Adarsh@123");
            }
        }
    }
}
