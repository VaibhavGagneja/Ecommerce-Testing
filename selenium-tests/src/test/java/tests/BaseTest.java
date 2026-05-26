package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.DriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    protected Properties properties;

    @BeforeMethod
    public void setup() {
        loadProperties();
        
        String browser = properties.getProperty("browser", "chrome");
        boolean headless = "true".equalsIgnoreCase(properties.getProperty("headless"));
        int implicitWait = Integer.parseInt(properties.getProperty("implicitWait", "10"));
        
        driver = DriverManager.initDriver(browser, headless, implicitWait);
        driver.get(properties.getProperty("baseUrl", "http://localhost:3000"));
    }

    @AfterMethod
    public void teardown() {
        DriverManager.quitDriver();
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Could not load config.properties. Using default values.");
            properties.setProperty("baseUrl", "http://localhost:3000");
            properties.setProperty("implicitWait", "10");
        }
    }
}
