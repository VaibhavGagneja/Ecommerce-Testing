package Tutorial;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Iterator;
import java.util.Set;

public class WindowHandlingAssignment {
    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.get("https://the-internet.herokuapp.com/windows");

        WebElement parentWindow = driver.findElement(By.cssSelector("div[class='example'] h3"));
        System.out.println("Parent Window Text: " + parentWindow.getText());
        driver.findElement(By.cssSelector("div[class='example'] a")).click();
        Set<String> allWindows = driver.getWindowHandles();
        Iterator<String> iterator = allWindows.iterator();
        String parentWindowId = iterator.next();
        String childWindowId = iterator.next();
        System.out.println("Parent Window ID: " + parentWindowId);
        System.out.println("Child Window ID: " + childWindowId);
        driver.switchTo().window(childWindowId);
        WebElement childWindow = driver.findElement(By.cssSelector("div[class='example'] h3"));
        System.out.println("Child Window Text: " + childWindow.getText());
        driver.close();

    }

}
