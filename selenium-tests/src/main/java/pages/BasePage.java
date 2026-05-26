package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    protected void click(By locator) {
        waitForElementClickable(locator);
        driver.findElement(locator).click();
    }

    protected void scrollAndClick(By locator) {
        // 1. Get the element onto the DOM safely
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        // 2. Force scroll via precise pixel calculations, bypassing standard scrollIntoView bugs
        String scrollScript =
                "var el = arguments[0];" +
                        "var elRect = el.getBoundingClientRect();" +
                        "var absoluteTop = elRect.top + window.pageYOffset;" +
                        "var middle = absoluteTop - (window.innerHeight / 2);" +
                        "window.scrollTo(0, middle);";

        ((JavascriptExecutor) driver).executeScript(scrollScript, element);

        // 3. Give the UI a brief moment to settle its layout
        try { Thread.sleep(250); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // 4. Ensure it is clickable now that it is centered
        wait.until(ExpectedConditions.elementToBeClickable(element));

        // 5. Attempt the click with interception protection
        wait.until(driver -> {
            try {
                element.click();
                return true;
            } catch (org.openqa.selenium.ElementClickInterceptedException |
                     org.openqa.selenium.StaleElementReferenceException e) {
                // If standard click STILL gets blocked by an overlay, use JS click as a nuclear option
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return true;
                } catch (Exception jsEx) {
                    return false;
                }
            }
        });
    }

    public void writeText(By locator, String text) {
        // 1. Wait until the element is at least visible on the DOM
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        // 2. Smart loop to handle UI twitches and blockages
        wait.until(driver -> {
            try {
                WebElement element = driver.findElement(locator);

                // Auto-scroll the element to the center of the screen so nothing covers it
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

                // Give the browser a microsecond to finish scrolling
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                element.clear();
                element.sendKeys(text);
                return true; // Success! Break the loop.

            } catch (org.openqa.selenium.StaleElementReferenceException |
                     org.openqa.selenium.ElementNotInteractableException e) {
                // If the element is stale OR temporarily blocked, return false to try again
                return false;
            }
        });
    }

    protected String readText(By locator) {
        waitForElementVisible(locator);
        return driver.findElement(locator).getText();
    }

    protected void waitForElementVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForElementClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean isElementDisplayed(By locator) {
        try {
            waitForElementVisible(locator);
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
