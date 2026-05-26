package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import hooks.Hooks;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.*;

import java.util.UUID;

public class CheckoutSteps {
    private WebDriver driver = Hooks.getDriver();
    private HomePage homePage = new HomePage(driver);
    private LoginPage loginPage = new LoginPage(driver);
    private ProductDetailPage productDetailPage = new ProductDetailPage(driver);
    private CartPage cartPage = new CartPage(driver);
    private CheckoutPage checkoutPage = new CheckoutPage(driver);

    @Given("I register and log in a new customer account")
    public void iRegisterAndLogInANewCustomerAccount() {
        homePage.navigateToLogin();
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String name = "BDD Customer " + randomSuffix;
        String email = "bdd_" + randomSuffix + "@test.com";
        String phone = "9" + String.format("%09d", (long)(Math.random() * 1000000000L));
        
        loginPage.register(name, email, phone, "MALE", "Secret123", "Secret123");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        loginPage.toggleToLogin();
        loginPage.login(email, "Secret123");
        Assert.assertTrue(homePage.isUserLoggedIn(name), "User should be logged in");
    }

    private String searchedProduct;

    @When("I search for the product {string}")
    public void iSearchForTheProduct(String productName) {
        this.searchedProduct = productName;
        homePage.searchProduct(productName);
        Assert.assertTrue(homePage.isProductVisible(productName), "Product should be visible in search results");
    }

    @When("I open the product page and add it to my cart")
    public void iOpenTheProductPageAndAddItToMyCart() {
        String product = searchedProduct != null ? searchedProduct : "Logitech MX Master 3S Wireless Mouse";
        homePage.clickProduct(product);
        productDetailPage.clickAddToCart();
        productDetailPage.waitForAddedText();
    }

    @When("I navigate to the cart page and proceed to checkout")
    public void iNavigateToTheCartPageAndProceedToCheckout() {
        homePage.clickCart();
        cartPage.clickPlaceOrder();
    }

    @When("I enter my shipping address name {string}, phone {string}, pincode {string}, city {string}, state {string}, address {string}")
    public void iEnterMyShippingAddressNamePhonePincodeCityStateAddress(String name, String phone, String pincode, String city, String state, String address) {
        checkoutPage.fillShippingAddress(name, phone, pincode, city, state, address);
    }

    @When("I select Cash on Delivery and submit the order")
    public void iSelectCashOnDeliveryAndSubmitTheOrder() {
        checkoutPage.selectCODAndPlaceOrder();
    }

    @Then("I should see the order success confirmation screen")
    public void iShouldSeeTheOrderSuccessConfirmationScreen() {
        Assert.assertTrue(checkoutPage.isOrderSuccessDisplayed(), "Order placed successfully message should be displayed");
    }
}
