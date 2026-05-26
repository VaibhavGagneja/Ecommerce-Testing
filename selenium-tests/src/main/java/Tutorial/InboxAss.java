package Tutorial;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class InboxAss {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");

        WebElement checkboxOption1 = driver.findElement(By.cssSelector("div[id='checkbox-example'] input[value='option1']"));

        if (checkboxOption1.isSelected()) {
            checkboxOption1.click();
        } else {
            System.out.println("Already unchecked");
        }
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("div[id='checkbox-example'] input"));
        System.out.println("Total checkboxes: " + checkboxes.size());
        driver.quit();
    }
}
