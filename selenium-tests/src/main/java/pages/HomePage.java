package pages;

import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    // Locators
    private final By searchInput = By.xpath("//input[@placeholder='Search products, brands, deals']");
    private final By userMenuTrigger = By.xpath("//button[contains(., 'Login') or contains(., 'Account') or .//span]");
    private final By loginButtonInDropdown = By.xpath("//a[text()='Login / Sign up']");
    private final By profileLink = By.xpath("//a[text()='My Profile']");
    private final By adminDashboardLink = By.xpath("//a[text()='Admin Dashboard']");
    private final By logoutButton = By.xpath("//button[contains(., 'Logout')]");
    private final By cartLink = By.xpath("//a[contains(@href, '/cart')]");
    private final By wishlistLink = By.xpath("//a[contains(., 'Wishlist')]");
    private final By trendingHeading = By.xpath("//h2[text()='Trending Products']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void searchProduct(String query) {
        writeText(searchInput, query);
        driver.findElement(searchInput).sendKeys(Keys.ENTER);
    }

    public void navigateToLogin() {
        click(userMenuTrigger);
        click(loginButtonInDropdown);
    }

    public void navigateToProfile() {
        click(userMenuTrigger);
        click(profileLink);
    }

    public void navigateToAdminDashboard() {
        click(userMenuTrigger);
        click(adminDashboardLink);
    }

    public void logout() {
        click(userMenuTrigger);
        click(logoutButton);
    }

    public void clickCategory(String categoryName) {
        By categoryLink = By.xpath("//a[contains(text(), '" + categoryName + "')]");
        click(categoryLink);
    }

    public void clickCart() {
        WebElement cartElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartLink));

        // 2. Pass the resolved 'cartElement' (NOT 'cartIcon') into the script
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartElement);
//        click(cartLink);
        scrollAndClick(cartLink);

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/cart"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Place Order']"))
        ));
    }

    public void clickWishlist() {
        click(wishlistLink);
    }

    public boolean isUserLoggedIn(String username) {
        try {
            // Wait for trigger to not display "Login"
            wait.until(driver -> {
                String text = driver.findElement(userMenuTrigger).getText();
                return text != null && !text.toLowerCase().contains("login");
            });

            click(userMenuTrigger);
            
            // Allow a brief moment for the dropdown to open/render
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}

            String pageSource = driver.getPageSource();
            boolean matches = pageSource.toLowerCase().contains(username.toLowerCase());

            click(userMenuTrigger); // Close dropdown
            return matches;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProductVisible(String productName) {
        By productCard = By.xpath("//h3[contains(text(), '" + productName + "')] | //div[contains(text(), '" + productName + "')]");
        return isElementDisplayed(productCard);
    }

    public void clickProduct(String productName) {
        By productCard = By.xpath("//h3[contains(text(), '" + productName + "')] | //div[contains(text(), '" + productName + "')]");
        click(productCard);
    }
}
