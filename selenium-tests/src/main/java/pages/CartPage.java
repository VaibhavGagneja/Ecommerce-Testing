package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    // Locators
    private final By priceDetailsHeader = By.xpath("//div[text()='Price Details']");
    private final By totalMrpText = By.xpath("//div[contains(span, 'Price')]/span[2]");
    private final By discountText = By.xpath("//div[contains(span, 'Discount')]/span[2]");
    private final By finalPriceText = By.xpath("//div[contains(@class, 'border-t') and contains(span, 'Total Amount')]/span[2]");
    private final By placeOrderButton = By.xpath("//button[text()='Place Order']");
    private final By emptyCartHeading = By.xpath("//h1[text()='Your cart is empty']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void increaseQuantity(String productName) {
        By plusButton = By.xpath("//a[contains(text(), '" + productName + "')]/ancestor::div[contains(@class, 'rounded-md')]//button[contains(@class, 'p-2')][2]");
        click(plusButton);
    }

    public void decreaseQuantity(String productName) {
        By minusButton = By.xpath("//a[contains(text(), '" + productName + "')]/ancestor::div[contains(@class, 'rounded-md')]//button[contains(@class, 'p-2')][1]");
        click(minusButton);
    }

    public String getItemQuantity(String productName) {
        By quantityText = By.xpath("//a[contains(text(), '" + productName + "')]/ancestor::div[contains(@class, 'rounded-md')]//span[contains(@class, 'border-x')]");
        return readText(quantityText);
    }

    public void removeItem(String productName) {
        By removeButton = By.xpath("//a[contains(text(), '" + productName + "')]/ancestor::div[contains(@class, 'rounded-md')]//button[contains(., 'Remove')]");
        click(removeButton);
    }

    public boolean isItemInCart(String productName) {
        By itemLink = By.xpath("//a[contains(@class, 'line-clamp-2') and contains(text(), '" + productName + "')]");
        return isElementDisplayed(itemLink);
    }

    public String getTotalMRP() {
        return readText(totalMrpText);
    }

    public String getDiscount() {
        return readText(discountText);
    }

    public String getFinalPrice() {
        return readText(finalPriceText);
    }

    public void clickPlaceOrder() {
        scrollAndClick(placeOrderButton);
    }

    public boolean isEmptyCartDisplayed() {
        return isElementDisplayed(emptyCartHeading);
    }
}
