package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class ResponsiveUISteps {
    private WebDriver driver = Hooks.getDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @When("I resize the browser window to mobile width {string} and height {string}")
    public void iResizeTheBrowserWindowToMobileWidthAndHeight(String width, String height) {
        int w = Integer.parseInt(width);
        int h = Integer.parseInt(height);
        driver.manage().window().setSize(new Dimension(w, h));
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    @When("I resize the browser window to desktop width {string} and height {string}")
    public void iResizeTheBrowserWindowToDesktopWidthAndHeight(String width, String height) {
        int w = Integer.parseInt(width);
        int h = Integer.parseInt(height);
        driver.manage().window().setSize(new Dimension(w, h));
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    @Then("The mobile bottom navigation bar should be visible")
    public void theMobileBottomNavigationBarShouldBeVisible() {
        By bottomNavLocator = By.xpath("//nav[contains(@class, 'md:hidden')]");
        wait.until(ExpectedConditions.presenceOfElementLocated(bottomNavLocator));
        WebElement bottomNav = driver.findElement(bottomNavLocator);
        Assert.assertTrue(bottomNav.isDisplayed(), "Bottom navigation should be visible on mobile");
    }

    @Then("The desktop wishlist link should not be visible")
    public void theDesktopWishlistLinkShouldNotBeVisible() {
        By wishlistLink = By.xpath("//a[contains(@href, '/wishlist') and contains(@class, 'md:flex')]");
        boolean isVisible = false;
        try {
            WebElement elem = driver.findElement(wishlistLink);
            isVisible = elem.isDisplayed();
        } catch (Exception e) {
            isVisible = false;
        }
        Assert.assertFalse(isVisible, "Desktop wishlist link should be hidden on mobile");
    }

    @Then("The mobile bottom navigation bar should not be visible")
    public void theMobileBottomNavigationBarShouldNotBeVisible() {
        By bottomNavLocator = By.xpath("//nav[contains(@class, 'md:hidden')]");
        boolean isVisible = false;
        try {
            WebElement elem = driver.findElement(bottomNavLocator);
            isVisible = elem.isDisplayed();
        } catch (Exception e) {
            isVisible = false;
        }
        Assert.assertFalse(isVisible, "Bottom navigation should be hidden on desktop");
    }

    @Then("The desktop wishlist link should be visible")
    public void theDesktopWishlistLinkShouldBeVisible() {
        By wishlistLink = By.xpath("//a[contains(@href, '/wishlist') and contains(@class, 'md:flex')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistLink));
        WebElement elem = driver.findElement(wishlistLink);
        Assert.assertTrue(elem.isDisplayed(), "Desktop wishlist link should be visible on desktop");
    }
}
