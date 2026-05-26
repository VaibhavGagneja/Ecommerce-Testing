package Tutorial;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DynamicDropdowns{

    public static void main(String[] args) throws InterruptedException{

        // 1. Invoke Browser and navigate to the application
        WebDriver driver = new ChromeDriver();
        driver.get("https://rahulshettyacademy.com/dropdownsPractise/");

        // 2. Click on the 'FROM' (Departure) dropdown to expand it
        driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT")).click();

        // 3. Select 'Bengaluru' (BLR) in the FROM dropdown.
        // Since it's the first dropdown, Selenium naturally clicks the first matching node it finds.
        driver.findElement(By.xpath("//a[@value='BLR']")).click();

        // 4. SYNCHRONIZATION: Wait for the 'TO' dropdown to automatically open and load options.
        // (Note: Thread.sleep is a temporary solution until Explicit Waits are introduced).
        Thread.sleep(2000);

        // 5. Select 'Chennai' (MAA) in the 'TO' dropdown using XPath Indexing.
        // We wrap the XPath in () and add [2] to skip the hidden 'Chennai' in the FROM dropdown
        // and target the visible 'Chennai' in the TO dropdown.
        driver.findElement(By.xpath("(//a[@value='MAA'])[2]")).click();

        // Optional: Close the browser
         driver.quit();
    }
}