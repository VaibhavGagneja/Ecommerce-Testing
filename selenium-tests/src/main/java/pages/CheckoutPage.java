package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {

    // Address fields
    private final By fullNameInput = By.xpath("//input[@placeholder='Full name']");
    private final By phoneInput = By.xpath("//input[@placeholder='10-digit mobile number']");
    private final By pincodeInput = By.xpath("//input[@placeholder='Pincode']");
    private final By cityInput = By.xpath("//input[@placeholder='City']");
    private final By stateSelect = By.xpath("//select");
    private final By addressTextarea = By.xpath("//textarea[@placeholder='House no., street, area']");
    private final By deliverHereButton = By.xpath("//button[text()='Deliver Here']");

    // Payment step
    private final By codRadioLabel = By.xpath("//label[contains(., 'Cash on Delivery')]");
    private final By placeOrderButton = By.xpath("//button[contains(., 'Place COD Order') or contains(., 'Pay Securely')]");

    // Order Success
    private final By orderSuccessHeader = By.xpath("//h1[text()='Order placed successfully']");
    private final By viewOrdersButton = By.xpath("//button[text()='View Orders']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void fillShippingAddress(String fullName, String phone, String pincode, String city, String state, String fullAddress) {
        writeText(fullNameInput, fullName);
        writeText(phoneInput, phone);
        writeText(pincodeInput, pincode);
        writeText(cityInput, city);
        WebElement selectState = driver.findElement(stateSelect);
        Select dropdownState = new Select(selectState);
        dropdownState.selectByVisibleText(state);
//        click(stateSelect);
//        click(By.xpath("//option[text()='" + state + "']"));
        
        writeText(addressTextarea, fullAddress);
        click(deliverHereButton);
    }

    public void selectCODAndPlaceOrder() {
        click(codRadioLabel);
        click(placeOrderButton);
    }

    public boolean isOrderSuccessDisplayed() {
        return isElementDisplayed(orderSuccessHeader);
    }

    public void clickViewOrders() {
        click(viewOrdersButton);
    }
}
