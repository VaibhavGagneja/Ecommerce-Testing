package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductDetailPage;

import java.util.UUID;

public class CartOperationsTest extends BaseTest {

    private void registerAndLoginCustomer(HomePage homePage, LoginPage loginPage) {
        homePage.navigateToLogin();
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String name = "Customer " + randomSuffix;
        String email = "cust_" + randomSuffix + "@test.com";
        String phone = "9" + String.format("%09d", (long)(Math.random() * 1000000000L));
        
        loginPage.register(name, email, phone, "MALE", "Secret123", "Secret123");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        loginPage.toggleToLogin();
        loginPage.login(email, "Secret123");
    }

    @Test
    public void testCartAddUpdateRemove() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        CartPage cartPage = new CartPage(driver);

        // 1. Setup - register and login customer
        registerAndLoginCustomer(homePage, loginPage);

        // 2. Search product
        String productName = "Logitech MX Master 3S Wireless Mouse";
        homePage.searchProduct(productName);
        Assert.assertTrue(homePage.isProductVisible(productName), "Seeded product should be found");

        // 3. Open product details and verify details
        homePage.clickProduct(productName);
        Assert.assertEquals(productDetailPage.getProductName(), productName, "Product detail header name should match");

        // 4. Add to cart
        productDetailPage.clickAddToCart();
        
        // 5. Navigate to cart and verify
        homePage.clickCart();
        Assert.assertTrue(cartPage.isItemInCart(productName), "Item should be visible in the cart page");
        Assert.assertEquals(cartPage.getItemQuantity(productName), "1", "Quantity should initially be 1");

        // 6. Increase quantity
        cartPage.increaseQuantity(productName);
        // Add a tiny sleep or wait to let state change propagate
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        Assert.assertEquals(cartPage.getItemQuantity(productName), "2", "Quantity should update to 2");

        // 7. Remove item
        cartPage.removeItem(productName);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        Assert.assertTrue(cartPage.isEmptyCartDisplayed(), "Cart should be empty after item removal");
    }
}
