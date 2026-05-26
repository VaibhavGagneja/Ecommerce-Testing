package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import java.util.UUID;

public class CheckoutFlowTest extends BaseTest {

    private void registerAndLoginCustomer(HomePage homePage, LoginPage loginPage) {
        homePage.navigateToLogin();
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String name = "Buyer " + randomSuffix;
        String email = "buyer_" + randomSuffix + "@test.com";
        String phone = "9" + String.format("%09d", (long)(Math.random() * 1000000000L));
        
        loginPage.register(name, email, phone, "MALE", "Secret123", "Secret123");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        loginPage.toggleToLogin();
        loginPage.login(email, "Secret123");
        Assert.assertTrue(homePage.isUserLoggedIn(name), "User should be logged in");
    }

    @Test
    public void testCheckoutFlowCOD() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // 1. Setup - register and login customer
        registerAndLoginCustomer(homePage, loginPage);

        // 2. Search & Select Product
        String productName = "Logitech MX Master 3S Wireless Mouse";
        homePage.searchProduct(productName);
        Assert.assertTrue(homePage.isProductVisible(productName), "Logitech mouse should be visible in search results");
        homePage.clickProduct(productName);

        // 3. Add to Cart
        productDetailPage.clickAddToCart();
        productDetailPage.waitForAddedText();

        // 4. Cart verification & Checkout initiation
        homePage.clickCart();
        Assert.assertTrue(cartPage.isItemInCart(productName), "Logitech mouse should be in the cart");
        cartPage.clickPlaceOrder();

        // 5. Checkout address input
        checkoutPage.fillShippingAddress(
            "Adarsh Kumar",
            "9876543210",
            "110001",
            "New Delhi",
            "Delhi",
            "12/4, Connaught Place, Block B"
        );

        // 6. Select Payment Method & Submit Order
        checkoutPage.selectCODAndPlaceOrder();

        // 7. Verify Success Screen
        Assert.assertTrue(checkoutPage.isOrderSuccessDisplayed(), "Order confirmation message should be visible!");
    }
}
