package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductDetailPage extends BasePage {

    // Locators
    private final By addToCartButton = By.xpath("//button[contains(., 'Add to Cart') or contains(., 'Added')]");
    private final By buyNowButton = By.xpath("//button[contains(., 'Buy Now')]");
    private final By plusButton = By.xpath("//button[text()='+']");
    private final By minusButton = By.xpath("//button[text()='-']");
    private final By quantityText = By.xpath("//span[contains(@class, 'border-x') and contains(@class, 'font-bold')]");
    private final By pincodeInput = By.xpath("//input[@placeholder='Enter delivery pincode']");
    private final By pincodeCheckButton = By.xpath("//button[text()='Check']");
    private final By pincodeMessage = By.xpath("//p[contains(@class, 'text-green-600') or contains(@class, 'text-red-600')]");
    private final By wishlistButton = By.xpath("//button[contains(@class, 'text-gray-400') or contains(@class, 'hover:text-red-500')]");
    private final By productNameHeader = By.xpath("//h1[contains(@class, 'text-white') and contains(@class, 'text-xl')]");
    private final By productPriceText = By.xpath("//span[contains(@class, 'text-3xl') and contains(@class, 'font-black')]");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public void clickAddToCart() {
        click(addToCartButton);
    }

    public void waitForAddedText() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(addToCartButton, "Added"));
    }

    public void clickBuyNow() {
        click(buyNowButton);
    }

    public void increaseQuantity() {
        click(plusButton);
    }

    public void decreaseQuantity() {
        click(minusButton);
    }

    public String getQuantity() {
        return readText(quantityText);
    }

    public void checkPincode(String pincode) {
        writeText(pincodeInput, pincode);
        click(pincodeCheckButton);
    }

    public String getPincodeMessage() {
        return readText(pincodeMessage);
    }

    public void toggleWishlist() {
        click(wishlistButton);
    }

    public String getProductName() {
        return readText(productNameHeader);
    }

    public String getProductPrice() {
        return readText(productPriceText);
    }
}
