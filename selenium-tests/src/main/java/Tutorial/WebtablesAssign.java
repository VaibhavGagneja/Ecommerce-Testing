package Tutorial;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class WebtablesAssign {
    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");

        By tableLocator = By.cssSelector("table[name='courses'] tr");
        By courseLocator = By.cssSelector("table[name='courses'] tr:nth-child(1) th");
        List<WebElement> table = driver.findElements(tableLocator);
        System.out.println("Total number of rows in the table: " + table.size());
        List<WebElement> tableRow = driver.findElements(courseLocator);
        System.out.println("Total number of columns: " + tableRow.size());

        List<WebElement> courseRow = driver.findElements(By.cssSelector("table[name='courses'] tr:nth-child(3) td"));
        for (WebElement row : courseRow) {

            System.out.print(row.getText() + " | ");
        }
        driver.close();
    }
}