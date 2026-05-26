package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPage;

import java.util.UUID;

public class RegistrationCoverageSteps {
    private LoginPage loginPage = new LoginPage(Hooks.getDriver());

    private String generateRandomSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @When("I submit registration details with a 9-digit phone number")
    public void iSubmitRegistrationDetailsWithA9DigitPhoneNumber() {
        String suffix = generateRandomSuffix();
        loginPage.register("Test " + suffix, "test_" + suffix + "@test.com", "123456789", "MALE", "ValidPass123", "ValidPass123");
    }

    @When("I submit registration details with a 5-character password")
    public void iSubmitRegistrationDetailsWithA5CharacterPassword() {
        String suffix = generateRandomSuffix();
        String phone = "99" + suffix.replaceAll("[a-zA-Z\\-]", "1");
        loginPage.register("Test " + suffix, "test_" + suffix + "@test.com", phone, "MALE", "Short", "Short");
    }

    @When("I submit registration details with mismatched passwords")
    public void iSubmitRegistrationDetailsWithMismatchedPasswords() {
        String suffix = generateRandomSuffix();
        String phone = "88" + suffix.replaceAll("[a-zA-Z\\-]", "2");
        loginPage.register("Test " + suffix, "test_" + suffix + "@test.com", phone, "MALE", "ValidPass123", "ValidPass456");
    }

    @Then("I should see a registration error message {string}")
    public void iShouldSeeARegistrationErrorMessage(String expectedMessage) {
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed on registration form");
        Assert.assertEquals(loginPage.getErrorText(), expectedMessage);
    }
}
