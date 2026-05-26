package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class SecurityEdgeCasesSteps {
    private WebDriver driver = Hooks.getDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    private WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

    @When("I directly navigate to the {string} URL")
    public void iDirectlyNavigateToTheURL(String path) {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        driver.get(Hooks.getProperty("baseUrl") + path);
        // Wait for the splash screen to finish and the React app to render
        // The splash screen lasts 3 seconds, plus webpack compilation time
        longWait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//nav | //main | //input[@placeholder='Search products, brands, deals'] | //a[contains(@href,'/login')]")
        ));
        // Additional wait for React state to settle (useEffect to fire and ProtectedRoute to redirect)
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
    }

    @Then("I should be redirected away from the admin page")
    public void iShouldBeRedirectedAwayFromTheAdminPage() {
        // Wait up to 15 seconds for the URL to change away from /admin
        try {
            longWait.until(driver -> !driver.getCurrentUrl().endsWith("/admin"));
        } catch (Exception e) {
            // Fall through to assertion
        }
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, Hooks.getProperty("baseUrl") + "/admin",
            "User should not be able to access /admin directly. Current URL: " + currentUrl);
    }

    @When("I inject an XSS payload into the search bar")
    public void iInjectAnXSSPayloadIntoTheSearchBar() {
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Search products, brands, deals']"));
        String xssPayload = "<script>alert('XSS')</script>";
        searchInput.clear();
        searchInput.sendKeys(xssPayload);
        searchInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.urlContains("search"));
    }

    @Then("The payload should not execute and should be rendered as safe text")
    public void thePayloadShouldNotExecuteAndShouldBeRenderedAsSafeText() {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        boolean alertPresent = false;
        try {
            driver.switchTo().alert();
            alertPresent = true;
        } catch (Exception e) {
            alertPresent = false;
        }
        Assert.assertFalse(alertPresent, "XSS Payload should not execute as an alert");
        
        // Ensure the payload is rendered as safe text in the results
        WebElement resultHeader = driver.findElement(By.tagName("h1"));
        Assert.assertTrue(resultHeader.getText().contains("<script>alert('XSS')</script>"), "Payload should be rendered safely as text");
    }
}
