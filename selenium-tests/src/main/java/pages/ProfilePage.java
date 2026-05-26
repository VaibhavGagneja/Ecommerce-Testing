package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ProfilePage extends BasePage {

    // Personal info locators
    private final By personalFullNameInput = By.xpath("//h2[text()='Personal information']/following-sibling::div//input[@placeholder='Full name']");
    private final By personalMobileInput = By.xpath("//h2[text()='Personal information']/following-sibling::div//input[@placeholder='Mobile number']");
    private final By personalEmailInput = By.xpath("//h2[text()='Personal information']/following-sibling::div//input[@placeholder='Email']");
    private final By saveChangesButton = By.xpath("//button[text()='Save changes']");

    // Address fields
    private final By addressLabelInput = By.xpath("//input[@placeholder='Label']");
    private final By addressFullNameInput = By.xpath("//h2[contains(text(), 'address')]/following-sibling::div//input[@placeholder='Full name']");
    private final By addressMobileInput = By.xpath("//input[@placeholder='Mobile']");
    private final By addressPincodeInput = By.xpath("//input[@placeholder='Pincode']");
    private final By addressLine1Input = By.xpath("//input[@placeholder='House, street, area']");
    private final By addressLine2Input = By.xpath("//input[@placeholder='Landmark']");
    private final By addressCityInput = By.xpath("//input[@placeholder='City']");
    private final By addressStateSelect = By.xpath("//select");
    private final By addressDefaultCheckbox = By.xpath("//input[@type='checkbox']");
    private final By saveAddressButton = By.xpath("//button[text()='Save address']");
    
    // Verification locators
    private final By sendEmailOtpButton = By.xpath("//button[text()='Send email OTP']");
    private final By verifyEmailOtpButton = By.xpath("//button[text()='Verify' and preceding-sibling::input[@placeholder='Email OTP']] | (//button[text()='Verify'])[1]");
    private final By toastNotification = By.xpath("//div[contains(@class, 'fixed') and contains(@class, 'right-4') and contains(@class, 'top-20')]");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    public void clickTab(String tabName) {
        By tabLocator = By.xpath("//button[contains(., '" + tabName + "')]");
        click(tabLocator);
    }

    public void updatePersonalInfo(String name, String mobile) {
        writeText(personalFullNameInput, name);
        writeText(personalMobileInput, mobile);
        click(saveChangesButton);
    }

    public void addAddress(String label, String fullName, String mobile, String pincode, String line1, String line2, String city, String state, boolean makeDefault) {
        writeText(addressLabelInput, label);
        writeText(addressFullNameInput, fullName);
        writeText(addressMobileInput, mobile);
        writeText(addressPincodeInput, pincode);
        writeText(addressLine1Input, line1);
        writeText(addressLine2Input, line2);
        writeText(addressCityInput, city);
        
        WebElement selectElement = driver.findElement(addressStateSelect);
        Select selectState = new Select(selectElement);
        selectState.selectByVisibleText(state);

        if (makeDefault && !driver.findElement(addressDefaultCheckbox).isSelected()) {
            click(addressDefaultCheckbox);
        }
        click(saveAddressButton);
    }

    public void editAddressPincode(String label, String newPincode) {
        By editBtn = By.xpath("//p[contains(text(), '" + label + "')]/ancestor::div[contains(@class, 'rounded-md')]//button[@title='Edit address']");
        click(editBtn);
        writeText(addressPincodeInput, newPincode);
        click(saveAddressButton);
    }

    public boolean isAddressDisplayed(String label) {
        By addressCard = By.xpath("//p[contains(@class, 'font-black') and contains(text(), '" + label + "')]");
        return isElementDisplayed(addressCard);
    }

    public boolean isAddressDetailsDisplayed(String label, String detailText) {
        By detailsTextLocator = By.xpath("//p[contains(text(), '" + label + "')]/ancestor::div[contains(@class, 'rounded-md')]//p[contains(text(), '" + detailText + "')]");
        return isElementDisplayed(detailsTextLocator);
    }

    public void deleteAddress(String label) {
        By deleteBtn = By.xpath("//p[contains(text(), '" + label + "')]/ancestor::div[contains(@class, 'rounded-md')]//button[@title='Delete address']");
        click(deleteBtn);
    }

    public void triggerEmailOtp() {
        click(sendEmailOtpButton);
    }

    public void clickVerifyEmailOtp() {
        click(verifyEmailOtpButton);
    }

    public String getToastMessage() {
        waitForElementVisible(toastNotification);
        return readText(toastNotification);
    }
}
