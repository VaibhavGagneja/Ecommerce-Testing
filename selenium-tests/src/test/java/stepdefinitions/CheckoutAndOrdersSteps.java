package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.CheckoutPage;

import java.time.Duration;

public class CheckoutAndOrdersSteps {
    private WebDriver driver = Hooks.getDriver();
    private CheckoutPage checkoutPage = new CheckoutPage(driver);
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @Then("I should be redirected to the cart page")
    public void iShouldBeRedirectedToTheCartPage() {
        wait.until(ExpectedConditions.urlContains("cart"));
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/cart"), "Empty cart checkout should redirect to cart page");
    }

    @When("I click the View Orders button")
    public void iClickTheViewOrdersButton() {
        checkoutPage.clickViewOrders();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @Then("I should see the order in PENDING status")
    public void iShouldSeeTheOrderInPENDINGStatus() {
        By statusBadge = By.xpath("(//span[contains(@class, 'text-orange-700')])[1]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(statusBadge));
        String status = driver.findElement(statusBadge).getText().trim();
        Assert.assertTrue("PENDING".equalsIgnoreCase(status), "Initial status should be PENDING");
    }

    @When("I cancel the order with reason {string}")
    public void iCancelTheOrderWithReason(String reason) {
        By cancelBtn = By.xpath("(//button[contains(., 'Cancel with reason')])[1]");
        wait.until(ExpectedConditions.elementToBeClickable(cancelBtn));
        driver.findElement(cancelBtn).click();
        
        By textarea = By.xpath("//textarea[@placeholder='Write your reason here']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(textarea));
        driver.findElement(textarea).sendKeys(reason);
        
        By submitBtn = By.xpath("//button[text()='Submit']");
        driver.findElement(submitBtn).click();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Then("I should see the order status change to {string}")
    public void iShouldSeeTheOrderStatusChangeTo(String expectedStatus) {
        if ("CANCELLED".equalsIgnoreCase(expectedStatus)) {
            By cancellationBanner = By.xpath("//div[contains(text(), 'This order has been cancelled.')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(cancellationBanner));
            Assert.assertTrue(driver.findElement(cancellationBanner).isDisplayed(), "Cancellation banner should be displayed");
        } else {
            By statusBadge = By.xpath("(//span[contains(@class, 'text-orange-700')])[1]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(statusBadge));
            String status = driver.findElement(statusBadge).getText().trim();
            Assert.assertEquals(status, expectedStatus, "Order status should match");
        }
    }
}
