package Tutorial;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DynamicSelect {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement dropdown = driver.findElement(By.className("ui-autocomplete-input"));
        dropdown.click();
        dropdown.sendKeys("Ind");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul[class='ui-menu ui-widget ui-widget-content ui-autocomplete ui-front'] li")));
        List<WebElement> India = driver.findElements(By.cssSelector("ul[class='ui-menu ui-widget ui-widget-content ui-autocomplete ui-front'] li"));
        for (WebElement i : India) {
            if(i.getText().equals("India")) {
                i.click();
                break;
            }
            System.out.println(i.getText());
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }
}
