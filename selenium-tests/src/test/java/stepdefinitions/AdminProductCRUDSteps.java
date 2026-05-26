package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.HomePage;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;

public class AdminProductCRUDSteps {
    private WebDriver driver = Hooks.getDriver();
    private HomePage homePage = new HomePage(driver);
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @Given("I navigate to the Admin Dashboard")
    public void iNavigateToTheAdminDashboard() {
        homePage.navigateToAdminDashboard();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @When("I click the Add Product button")
    public void iClickTheAddProductButton() {
        By addProductBtn = By.xpath("//button[contains(., 'Add Product')]");
        safeClick(addProductBtn);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @When("I fill the basic product details: name {string}, brand {string}, price {string}, stock {string}, category {string}, description {string}")
    public void iFillTheBasicProductDetails(String name, String brand, String price, String stock, String category, String description) {
        // Select Category
        By catSelect = By.xpath("//select[@required]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(catSelect));
        WebElement selectElement = driver.findElement(catSelect);
        Select select = new Select(selectElement);
        if ("Electronics".equalsIgnoreCase(category)) {
            select.selectByValue("appliances"); // mapped to Home Appliances & Electronics
        } else {
            select.selectByValue(category.toLowerCase());
        }

        // Fill fields
        driver.findElement(By.xpath("//input[@name='name']")).sendKeys(name);
        driver.findElement(By.xpath("//input[@name='brand']")).sendKeys(brand);
        driver.findElement(By.xpath("//textarea[@name='description']")).sendKeys(description);
        driver.findElement(By.xpath("//input[@name='price']")).sendKeys(price);
        driver.findElement(By.xpath("//input[@name='stockQuantity']")).sendKeys(stock);
    }

    @When("I upload the product image")
    public void iUploadTheProductImage() {
        // Navigate to Images tab
        By imagesTab = By.xpath("//button[contains(., 'Images')]");
        safeClick(imagesTab);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // Ensure dummy image exists
        createDummyPngIfNotExist();
        File dummyImg = new File("src/test/resources/test_image.png");
        String absolutePath = dummyImg.getAbsolutePath();

        // Send path to file input
        By fileInput = By.xpath("//input[@type='file']");
        driver.findElement(fileInput).sendKeys(absolutePath);
        try { Thread.sleep(3000); } catch (InterruptedException e) {} // Wait for mock Cloudinary upload proxy
    }

    @When("I navigate to the Manufacturer tab")
    public void iNavigateToTheManufacturerTab() {
        By manufacturerTab = By.xpath("//button[contains(., 'Manufacturer')]");
        safeClick(manufacturerTab);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    @When("I submit the product form")
    public void iSubmitTheProductForm() {
        By submitBtn = By.xpath("//button[@type='submit' and contains(., 'Add Product')]");
        safeClick(submitBtn);
    }

    @Then("I should see the product {string} listed in the Admin products table")
    public void iShouldSeeTheProductListedInTheAdminProductsTable(String productName) {
        By searchInput = By.xpath("//input[@placeholder='Search product or brand']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        WebElement input = driver.findElement(searchInput);
        input.clear();
        input.sendKeys(productName);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}

        By productRow = By.xpath("//tr[contains(., '" + productName + "')]");
        Assert.assertTrue(driver.findElement(productRow).isDisplayed(), "Product should be visible in Admin list");
    }

    @When("I locate the product {string} in the Admin products table")
    public void iLocateTheProductInTheAdminProductsTable(String productName) {
        By searchInput = By.xpath("//input[@placeholder='Search product or brand']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        WebElement input = driver.findElement(searchInput);
        input.clear();
        input.sendKeys(productName);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @When("I click the delete button for {string} and accept the confirmation alert")
    public void iClickTheDeleteButtonForAndAcceptTheConfirmationAlert(String productName) {
        By deleteBtn = By.xpath("//tr[contains(., '" + productName + "')]//button[contains(@class, 'text-red-600')]");
        safeClick(deleteBtn);
        
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    private void safeClick(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        WebElement element = driver.findElement(locator);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    @Then("I should not see {string} in the Admin products table")
    public void iShouldNotSeeInTheAdminProductsTable(String productName) {
        By productRow = By.xpath("//tr[contains(., '" + productName + "')]");
        boolean invisible = false;
        try {
            invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(productRow));
        } catch (Exception e) {
            invisible = false;
        }
        Assert.assertTrue(invisible, "Product should be removed from Admin list");
    }

    private void createDummyPngIfNotExist() {
        File resourcesDir = new File("src/test/resources");
        if (!resourcesDir.exists()) {
            resourcesDir.mkdirs();
        }
        File file = new File("src/test/resources/test_image.png");
        if (!file.exists()) {
            try {
                // 1x1 transparent PNG base64
                byte[] bytes = Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==");
                Files.write(file.toPath(), bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
