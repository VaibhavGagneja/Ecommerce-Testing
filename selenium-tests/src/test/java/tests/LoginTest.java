package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

import java.util.UUID;

public class LoginTest extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Go to login page
        homePage.navigateToLogin();

        // Login with seeded admin
        String email = properties.getProperty("adminEmail");
        String password = properties.getProperty("adminPassword");
        loginPage.login(email, password);

        // Verify successful login
        Assert.assertTrue(homePage.isUserLoggedIn("Admin"), "User name should show up in user account dropdown!");
    }

    @Test
    public void testFailedLogin() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.navigateToLogin();
        loginPage.login("wronguser@ecom.com", "WrongPassword");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed for wrong credentials");
        Assert.assertEquals(loginPage.getErrorText(), "Email not registered", "Error text should match");
    }

    @Test
    public void testUserRegistration() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.navigateToLogin();
        
        // Generate unique details
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String name = "Test User " + randomSuffix;
        String email = "user_" + randomSuffix + "@test.com";
        // Generate random 10 digit number starting with 9
        String phone = "9" + String.format("%09d", (long)(Math.random() * 1000000000L));
        
        loginPage.register(name, email, phone, "MALE", "Secret123", "Secret123");

        Assert.assertTrue(loginPage.isSuccessDisplayed(), "Success message should be displayed after registration");
        Assert.assertEquals(loginPage.getSuccessText(), "Account created. Please login.", "Success text should match");
    }
}
