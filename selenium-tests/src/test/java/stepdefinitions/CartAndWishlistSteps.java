package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailPage;

import java.time.Duration;

public class CartAndWishlistSteps {
    private WebDriver driver = Hooks.getDriver();
    private HomePage homePage = new HomePage(driver);
    private ProductDetailPage productDetailPage = new ProductDetailPage(driver);
    private CartPage cartPage = new CartPage(driver);
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @When("I add the product to my cart")
    public void iAddTheProductToMyCart() {
        productDetailPage.clickAddToCart();
        productDetailPage.waitForAddedText();
    }

    @Then("The cart badge count should be {string}")
    public void theCartBadgeCountShouldBe(String expectedCount) {
        By badge = By.xpath("//a[contains(@href, '/cart')]/span[contains(@class, 'rounded-full')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(badge));
        String actualCount = driver.findElement(badge).getText().trim();
        Assert.assertEquals(actualCount, expectedCount, "Cart badge count should match");
    }

    @When("I navigate to the cart page")
    public void iNavigateToTheCartPage() {
        homePage.clickCart();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @Then("I should see the product {string} in the cart with quantity {string}")
    public void iShouldSeeTheProductInTheCartWithQuantity(String productName, String expectedQty) {
        Assert.assertTrue(cartPage.isItemInCart(productName), "Item '" + productName + "' should be in cart");
        Assert.assertEquals(cartPage.getItemQuantity(productName), expectedQty, "Item quantity in cart should match");
    }

    @When("I increment the quantity of {string} in the cart")
    public void iIncrementTheQuantityOfInTheCart(String productName) {
        cartPage.increaseQuantity(productName);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @When("I decrement the quantity of {string} in the cart")
    public void iDecrementTheQuantityOfInTheCart(String productName) {
        cartPage.decreaseQuantity(productName);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @Then("I should see the empty cart message")
    public void iShouldSeeTheEmptyCartMessage() {
        Assert.assertTrue(cartPage.isEmptyCartDisplayed(), "Empty cart header should be visible");
    }

    @When("I attempt to click Add to Cart")
    public void iAttemptToClickAddToCart() {
        productDetailPage.clickAddToCart();
    }

    @Then("I should see the alert {string}")
    public void iShouldSeeTheAlert(String expectedAlertText) {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String actualAlertText = alert.getText();
        Assert.assertEquals(actualAlertText, expectedAlertText, "Alert message text should match");
        alert.accept();
    }

    @When("I click the wishlist heart button on the product card")
    public void iClickTheWishlistHeartButtonOnTheProductCard() {
        By wishlistBtn = By.xpath("(//button[contains(@class, 'hover:text-red-500')])[1]");
        wait.until(ExpectedConditions.elementToBeClickable(wishlistBtn));
        driver.findElement(wishlistBtn).click();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    @When("I click the Wishlist link in the navbar")
    public void iClickTheWishlistLinkInTheNavbar() {
        homePage.clickWishlist();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @Then("I should see {string} in the wishlist list")
    public void iShouldSeeInTheWishlistList(String productName) {
        By productTitle = By.xpath("//h3[contains(text(), '" + productName + "')]");
        Assert.assertTrue(driver.findElement(productTitle).isDisplayed(), "Wishlisted product should be displayed");
    }

    @When("I click the wishlist heart button on the wishlist page product card")
    public void iClickTheWishlistHeartButtonOnTheWishlistPageProductCard() {
        By wishlistBtn = By.xpath("(//button[contains(@class, 'hover:text-red-500')])[1]");
        wait.until(ExpectedConditions.elementToBeClickable(wishlistBtn));
        driver.findElement(wishlistBtn).click();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @Then("I should see the empty wishlist message")
    public void iShouldSeeTheEmptyWishlistMessage() {
        By emptyWishlistHeader = By.xpath("//h3[contains(text(), 'wishlist is empty') or contains(text(), 'Your wishlist is empty')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(emptyWishlistHeader));
        Assert.assertTrue(driver.findElement(emptyWishlistHeader).isDisplayed(), "Empty wishlist message should be displayed");
    }

    @Then("I should see the admin preview mode message {string}")
    public void iShouldSeeTheAdminPreviewModeMessage(String expectedMessage) {
        By adminMsg = By.xpath("//div[contains(text(), 'Admin preview mode')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(adminMsg));
        String actualMsg = driver.findElement(adminMsg).getText().trim();
        Assert.assertEquals(actualMsg, expectedMessage, "Admin preview message should match");
    }

    @Then("I should be redirected to the login page")
    public void iShouldBeRedirectedToTheLoginPage() {
        // The splash screen takes 3 seconds, then React mounts and ProtectedRoute redirects
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        longWait.until(ExpectedConditions.urlContains("login"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "User should be redirected to login page");
    }
}
