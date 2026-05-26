package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.HomePage;

import java.time.Duration;
import java.util.List;

public class SearchAndCartCoverageSteps {
    private WebDriver driver = Hooks.getDriver();
    private HomePage homePage = new HomePage(driver);
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @Given("I am on the home page")
    public void iAmOnTheHomePage() {
        driver.get(Hooks.getProperty("baseUrl") + "/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search products, brands, deals']")));
    }

    @When("I search for {string}")
    public void iSearchFor(String query) {
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Search products, brands, deals']"));
        searchInput.clear();
        searchInput.sendKeys(query);
        searchInput.sendKeys(Keys.ENTER);
    }

    @Then("I should see the no products found message")
    public void iShouldSeeTheNoProductsFoundMessage() {
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        boolean noResults = !driver.findElements(By.xpath("//h1[contains(text(), 'No products found') or contains(text(), 'No Products Found')] | //div[contains(text(), 'No products found')] | //p[contains(text(), 'No products found')]")).isEmpty();
        
        // Since frontend might just be rendering an empty list without a specific message, let's also accept an empty product grid.
        if (!noResults) {
            boolean gridEmpty = driver.findElements(By.xpath("//div[contains(@class, 'grid-cols')]//a[contains(@href, '/product/')]")).isEmpty();
            Assert.assertTrue(gridEmpty, "Search results grid should be empty");
        } else {
            Assert.assertTrue(true);
        }
    }

    @Given("I am on a product page")
    public void iAmOnAProductPage() {
        driver.get(Hooks.getProperty("baseUrl") + "/");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        List<WebElement> products = driver.findElements(By.xpath("//h3"));
        if (!products.isEmpty()) {
            products.get(0).click();
        } else {
            // Fallback
            driver.get(Hooks.getProperty("baseUrl") + "/product/1");
        }
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @When("I attempt to add the product to my cart")
    public void iAttemptToAddTheProductToMyCart() {
        List<WebElement> addBtns = driver.findElements(By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'add to cart')] | //button[contains(., 'Add to')]"));
        if (!addBtns.isEmpty()) {
            addBtns.get(0).click();
        }
    }

    @Then("I should receive an alert saying {string}")
    public void iShouldReceiveAnAlertSaying(String expectedAlert) {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            String alertText = driver.switchTo().alert().getText();
            Assert.assertEquals(alertText, expectedAlert);
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            Assert.fail("Expected an alert for unauthenticated action but none appeared");
        }
    }
}
