package stepdefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import hooks.Hooks;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.HomePage;
import pages.LoginPage;

public class LoginSteps {
    private WebDriver driver = Hooks.getDriver();
    private HomePage homePage = new HomePage(driver);
    private LoginPage loginPage = new LoginPage(driver);

    @Given("I navigate to the login page")
    public void iNavigateToTheLoginPage() {
        homePage.navigateToLogin();
    }

    @When("I submit username and password from configuration")
    public void iSubmitUsernameAndPasswordFromConfiguration() {
        String email = Hooks.getProperty("adminEmail");
        String password = Hooks.getProperty("adminPassword");
        loginPage.login(email, password);
    }

    @Then("I should be successfully logged in as user {string}")
    public void iShouldBeSuccessfullyLoggedInAsUser(String username) {
        Assert.assertTrue(homePage.isUserLoggedIn(username), "User name should be visible in user account menu");
    }

    @When("I submit username {string} and password {string}")
    public void iSubmitUsernameAndPassword(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String expectedError) {
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertEquals(loginPage.getErrorText(), expectedError, "Error message text should match");
    }

    @Then("Check and Validate the Session")
    public void checkAndValidateTheSession() {
        Cookie sessionCookie = driver.manage().getCookieNamed("ECOM_AUTH");
        Assert.assertNotNull(sessionCookie, "Login failed: Session cookie was not created.");
        System.out.println("Session active! Token value: " + sessionCookie.getValue());
    }

    @When("I click on the logout button")
    public void iLogOutOfTheApplication() {
        homePage.logout();
    }

    @Then("I should be logged out and redirected to the login page")
    public void iShouldBeLoggedOutAndRedirectedToTheLoginPage() {
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "User should be redirected to login page after logout");
    }
}
