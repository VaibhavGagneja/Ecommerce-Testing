package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Locators
    private final By emailInput = By.xpath("//input[@placeholder='Email Address *']");
    private final By passwordInput = By.xpath("//input[@placeholder='Password *']");
    private final By submitButton = By.xpath("//button[@type='submit'][normalize-space()='Login' or normalize-space()='Create Account']");
    private final By registerTab = By.xpath("//button[text()='Register']");
    private final By loginTab = By.xpath("//button[@type='button'][normalize-space()='Login']");
    private final By fullNameInput = By.xpath("//input[@placeholder='Full Name *']");
    private final By phoneInput = By.xpath("//input[@placeholder='Phone Number (10 digits) *']");
    private final By genderSelect = By.xpath("//select");
    private final By confirmPasswordInput = By.xpath("//input[@placeholder='Confirm Password *']");
    private final By errorMessage = By.xpath("//div[contains(@class, 'text-red-700')]");
    private final By successMessage = By.xpath("//div[contains(@class, 'text-green-700')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String email, String password) {
        writeText(emailInput, email);
        writeText(passwordInput, password);
        click(submitButton);
    }

    public void register(String fullName, String email, String phone, String gender, String password, String confirmPassword) {
        click(registerTab);
        writeText(fullNameInput, fullName);
        writeText(phoneInput, phone);
        
        // Select gender
        org.openqa.selenium.support.ui.Select selectGender = new org.openqa.selenium.support.ui.Select(driver.findElement(genderSelect));
        selectGender.selectByValue(gender.toUpperCase());
        
        writeText(emailInput, email);
        writeText(passwordInput, password);
        writeText(confirmPasswordInput, confirmPassword);
        click(submitButton);
    }

    public void toggleToLogin() {
        scrollAndClick(loginTab);
    }

    public String getErrorText() {
        return readText(errorMessage);
    }

    public String getSuccessText() {
        return readText(successMessage);
    }

    public boolean isErrorDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    public boolean isSuccessDisplayed() {
        return isElementDisplayed(successMessage);
    }
}
