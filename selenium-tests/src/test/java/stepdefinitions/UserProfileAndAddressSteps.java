package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.ProfilePage;

import java.time.Duration;

public class UserProfileAndAddressSteps {
    private WebDriver driver = Hooks.getDriver();
    private ProfilePage profilePage = new ProfilePage(driver);
    private WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

    @When("I click the profile tab {string}")
    public void iClickTheProfileTab(String tabName) {
        // Wait for the profile page to finish loading (the skeleton animation to disappear)
        // Wait for the tab buttons to be present and clickable
        By tabLocator = By.xpath("//button[contains(., '" + tabName + "')]");
        longWait.until(ExpectedConditions.elementToBeClickable(tabLocator));
        profilePage.clickTab(tabName);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @When("I update my personal details name to {string} and mobile number to {string}")
    public void iUpdateMyPersonalDetailsNameToAndMobileNumberTo(String name, String mobile) {
        profilePage.updatePersonalInfo(name, mobile);
    }

    @Then("I should see the toast notification {string}")
    public void iShouldSeeTheToastNotification(String expectedToast) {
        String actualToast = profilePage.getToastMessage();
        Assert.assertEquals(actualToast, expectedToast, "Toast notification should match");
    }

    @When("I add a new address with label {string}, name {string}, phone {string}, pincode {string}, line1 {string}, line2 {string}, city {string}, state {string}")
    public void iAddANewAddressWithLabelNamePhonePincodeLineLineCityState(String label, String fullName, String mobile, String pincode, String line1, String line2, String city, String state) {
        profilePage.addAddress(label, fullName, mobile, pincode, line1, line2, city, state, true);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Then("I should see the address {string} in the saved list")
    public void iShouldSeeTheAddressInTheSavedList(String label) {
        Assert.assertTrue(profilePage.isAddressDisplayed(label), "Address with label '" + label + "' should be visible");
    }

    @When("I edit the address {string} with a new pincode {string}")
    public void iEditTheAddressWithANewPincode(String label, String newPincode) {
        profilePage.editAddressPincode(label, newPincode);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Then("I should see the address {string} with pincode {string} in the saved list")
    public void iShouldSeeTheAddressWithPincodeInTheSavedList(String label, String pincode) {
        Assert.assertTrue(profilePage.isAddressDisplayed(label), "Address label should be visible");
        Assert.assertTrue(profilePage.isAddressDetailsDisplayed(label, pincode), "Address detail should contain the new pincode");
    }

    @When("I delete the address {string}")
    public void iDeleteTheAddress(String label) {
        profilePage.deleteAddress(label);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Then("I should not see the address {string} in the saved list")
    public void iShouldNotSeeTheAddressInTheSavedList(String label) {
        Assert.assertFalse(profilePage.isAddressDisplayed(label), "Address with label '" + label + "' should not be visible");
    }
}
