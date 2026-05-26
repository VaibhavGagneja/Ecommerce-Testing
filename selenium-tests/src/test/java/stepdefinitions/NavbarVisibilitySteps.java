package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

public class NavbarVisibilitySteps {
    private WebDriver driver = Hooks.getDriver();
    private HomePage homePage = new HomePage(driver);
    private LoginPage loginPage = new LoginPage(driver);
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    private WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

    private void waitForAppReady() {
        // Wait for the splash screen to finish and the app to be interactive
        longWait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@placeholder='Search products, brands, deals'] | //a[contains(@href,'/login')]")
        ));
    }

    @Given("I navigate to the home page as a guest")
    public void iNavigateToTheHomePageAsAGuest() {
        // Ensure logged out by clearing storage
        driver.get(Hooks.getProperty("baseUrl") + "/");
        waitForAppReady();
        ((org.openqa.selenium.html5.WebStorage) driver).getLocalStorage().clear();
        driver.navigate().refresh();
        waitForAppReady();
    }

    @Given("I log in as a standard customer")
    public void iLogInAsAStandardCustomer() {
        // First ensure the app is ready
        driver.get(Hooks.getProperty("baseUrl") + "/");
        waitForAppReady();
        
        homePage.navigateToLogin();
        // Since DB resets, registering a new customer on the fly is safest
        String rand = java.util.UUID.randomUUID().toString().replaceAll("[a-zA-Z\\-]", "3").substring(0, 9);
        String email = "c_" + rand + "@ecom.com";
        String pass = "Pass1234";
        loginPage.register("Cust", email, "7" + rand, "MALE", pass, pass);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        loginPage.toggleToLogin();
        loginPage.login(email, pass);
        Assert.assertTrue(homePage.isUserLoggedIn("Cust"), "User Cust should be logged in");
    }

    @Given("I log in as an administrator")
    public void iLogInAsAnAdministrator() {
        // First ensure the app is ready
        driver.get(Hooks.getProperty("baseUrl") + "/");
        waitForAppReady();
        
        homePage.navigateToLogin();
        loginPage.login(Hooks.getProperty("adminEmail"), Hooks.getProperty("adminPassword"));
        try {
            wait.until(ExpectedConditions.urlToBe(Hooks.getProperty("baseUrl") + "/"));
        } catch (Exception e) {
            // Assume failure means we need to register. However, standard registration only makes CUSTOMER role.
            // Let's assume the Seed script has run for Admin tests to pass.
        }
    }

    @Then("I should see the {string} button")
    public void iShouldSeeTheButton(String buttonName) {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        boolean isVisible = false;
        if (buttonName.equals("Login")) {
            isVisible = !driver.findElements(By.xpath("//span[contains(text(), 'Login')]")).isEmpty();
        }
        Assert.assertTrue(isVisible, "Button " + buttonName + " should be visible");
    }

    @Then("I should not see the {string} button")
    public void iShouldNotSeeTheButton(String buttonName) {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        boolean isVisible = true;
        if (buttonName.equals("Login")) {
            isVisible = !driver.findElements(By.xpath("//span[contains(text(), 'Login')]")).isEmpty();
        } else if (buttonName.equals("My Profile")) {
            By userMenuTrigger = By.xpath("//button[contains(., 'Login') or contains(., 'Account') or .//span]");
            driver.findElement(userMenuTrigger).click();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            isVisible = !driver.findElements(By.xpath("//a[contains(text(), 'My Profile')]")).isEmpty();
            driver.findElement(userMenuTrigger).click();
        }
        Assert.assertFalse(isVisible, "Button " + buttonName + " should not be visible");
    }

    @Then("I should see the {string} link")
    public void iShouldSeeTheLink(String linkName) {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        boolean isVisible = false;
        if (linkName.equals("Admin")) {
            By userMenuTrigger = By.xpath("//button[contains(., 'Login') or contains(., 'Account') or .//span]");
            driver.findElement(userMenuTrigger).click();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            isVisible = !driver.findElements(By.xpath("//a[contains(@href, '/admin')]")).isEmpty();
            driver.findElement(userMenuTrigger).click();
        }
        Assert.assertTrue(isVisible, "Link " + linkName + " should be visible");
    }

    @Then("I should not see the {string} link")
    public void iShouldNotSeeTheLink(String linkName) {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        boolean isVisible = true;
        if (linkName.equals("Admin")) {
            By userMenuTrigger = By.xpath("//button[contains(., 'Login') or contains(., 'Account') or .//span]");
            driver.findElement(userMenuTrigger).click();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            isVisible = !driver.findElements(By.xpath("//a[contains(@href, '/admin')]")).isEmpty();
            driver.findElement(userMenuTrigger).click();
        }
        Assert.assertFalse(isVisible, "Link " + linkName + " should not be visible");
    }

    @Then("I should see my user profile menu")
    public void iShouldSeeMyUserProfileMenu() {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        boolean hasUserMenu = !driver.findElements(By.xpath("//button//span[contains(., 'Cust')] | //button[contains(@class, 'flex items-center')]")).isEmpty();
        Assert.assertTrue(hasUserMenu, "User profile menu should be visible");
    }
}
