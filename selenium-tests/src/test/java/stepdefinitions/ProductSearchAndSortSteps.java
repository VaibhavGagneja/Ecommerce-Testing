package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.HomePage;
import pages.ProductDetailPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchAndSortSteps {
    private WebDriver driver = Hooks.getDriver();
    private HomePage homePage = new HomePage(driver);
    private ProductDetailPage productDetailPage = new ProductDetailPage(driver);
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @When("I open the product page")
    public void iOpenTheProductPage() {
        // Assume search results are displayed, click the first product
        // XPath matches product name link or title in search results
        By firstProductTitle = By.xpath("(//h3[contains(@class, 'line-clamp-2')])[1]");
        wait.until(ExpectedConditions.elementToBeClickable(firstProductTitle));
        driver.findElement(firstProductTitle).click();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    @Then("The product details should display a price of {string}")
    public void theProductDetailsShouldDisplayAPriceOf(String expectedPrice) {
        String actualPrice = productDetailPage.getProductPrice().replace("Rs", "").replace(",", "").trim();
        Assert.assertEquals(actualPrice, expectedPrice, "Product price should match expected");
    }

    @Then("The discount should show {string}")
    public void theDiscountShouldShow(String expectedDiscount) {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // Try multiple locator strategies for the discount text
        By discountTextLocator = By.xpath(
            "//span[contains(@class, 'text-orange-600') and contains(text(), 'off')] | " +
            "//div[contains(@class, 'items-baseline')]//span[contains(text(), 'off')] | " +
            "//span[contains(@class, 'font-bold') and contains(text(), '% off')]"
        );
        WebElement discountElem = longWait.until(ExpectedConditions.visibilityOfElementLocated(discountTextLocator));
        String actualDiscount = discountElem.getText().trim();
        Assert.assertEquals(actualDiscount, expectedDiscount, "Product discount percentage should match");
    }

    @When("I sort the search results by {string}")
    public void iSortTheSearchResultsBy(String sortOption) {
        By sortSelect = By.xpath("//select");
        wait.until(ExpectedConditions.visibilityOfElementLocated(sortSelect));
        WebElement selectElement = driver.findElement(sortSelect);
        Select select = new Select(selectElement);
        select.selectByVisibleText(sortOption);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Then("The search results should be sorted by price in ascending order")
    public void theSearchResultsShouldBeSortedByPriceInAscendingOrder() {
        List<WebElement> priceElements = driver.findElements(By.xpath("//span[contains(@class, 'text-lg') and contains(@class, 'font-black') and contains(text(), 'Rs')]"));
        List<Double> prices = new ArrayList<>();
        for (WebElement element : priceElements) {
            String cleanText = element.getText().replace("Rs", "").replace(",", "").trim();
            prices.add(Double.parseDouble(cleanText));
        }

        Assert.assertTrue(prices.size() > 0, "There should be search result prices to verify");
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1), "Prices are not sorted Low to High: " + prices.get(i) + " > " + prices.get(i+1));
        }
    }

    @Then("The search results should be sorted by price in descending order")
    public void theSearchResultsShouldBeSortedByPriceInDescendingOrder() {
        List<WebElement> priceElements = driver.findElements(By.xpath("//span[contains(@class, 'text-lg') and contains(@class, 'font-black') and contains(text(), 'Rs')]"));
        List<Double> prices = new ArrayList<>();
        for (WebElement element : priceElements) {
            String cleanText = element.getText().replace("Rs", "").replace(",", "").trim();
            prices.add(Double.parseDouble(cleanText));
        }

        Assert.assertTrue(prices.size() > 0, "There should be search result prices to verify");
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i + 1), "Prices are not sorted High to Low: " + prices.get(i) + " < " + prices.get(i+1));
        }
    }

    @When("I apply the price filter {string}")
    public void iApplyThePriceFilter(String priceFilterLabel) {
        By filterRadio = By.xpath("//label[contains(., '" + priceFilterLabel + "')]/input");
        wait.until(ExpectedConditions.elementToBeClickable(filterRadio));
        WebElement radio = driver.findElement(filterRadio);
        if (!radio.isSelected()) {
            radio.click();
        }
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Then("All search results should have prices under {string}")
    public void allSearchResultsShouldHavePricesUnder(String maxPriceStr) {
        double maxPrice = Double.parseDouble(maxPriceStr);
        List<WebElement> priceElements = driver.findElements(By.xpath("//span[contains(@class, 'text-lg') and contains(@class, 'font-black') and contains(text(), 'Rs')]"));
        Assert.assertTrue(priceElements.size() > 0, "There should be filtered search results");
        for (WebElement element : priceElements) {
            String cleanText = element.getText().replace("Rs", "").replace(",", "").trim();
            double price = Double.parseDouble(cleanText);
            Assert.assertTrue(price <= maxPrice, "Filtered price " + price + " is above max limit " + maxPrice);
        }
    }
}
