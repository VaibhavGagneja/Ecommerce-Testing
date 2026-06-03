package Tutorial;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class FramesHandlingAssignment {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("https://the-internet.herokuapp.com/nested_frames");

            // find top-level frames in the current context
            List<WebElement> frameElements = getFramesInCurrentContext(driver);
            Iterator<WebElement> frameElementIterator = frameElements.iterator();

            boolean found = false;
            while (frameElementIterator.hasNext()) {
                WebElement frameElement = frameElementIterator.next();
                if (Objects.equals(frameElement.getAttribute("name"), "frame-top")) {
                    driver.switchTo().frame(frameElement);
                    List<WebElement> childFrames = getFramesInCurrentContext(driver);
                    Iterator<WebElement> childFrameIterator = childFrames.iterator();
                    while (childFrameIterator.hasNext()) {
                        WebElement childFrameElement = childFrameIterator.next();
                        if (Objects.equals(childFrameElement.getAttribute("name"), "frame-middle")) {
                            driver.switchTo().frame(childFrameElement);
                            WebElement contentElement = driver.findElement(By.id("content"));
                            System.out.println(contentElement.getText());

                            // return to top-level before touching top-level element references
                            driver.switchTo().defaultContent();
                            found = true;
                            break;
                        }

//                  System.out.println(childFrameElement.getTagName() + " with name: " + childFrameElement.getAttribute("name"));
                    }

                    // if not found, ensure we return to top-level context before continuing
                    if (!found) {
                        driver.switchTo().defaultContent();
                    } else {
                        // we've found the target content; break out of outer loop as well
                        break;
                    }
                }
//            System.out.println(frameElement.getTagName() + " with name: " + frameElement.getAttribute("name"));
            }
        } finally {
            // ensure the browser and the webdriver session are cleanly shut down
            driver.quit();
        }

    }

    public static List<WebElement> getFramesInCurrentContext(WebDriver driver) {
        // finds both <iframe> and <frame>
        return driver.findElements(By.cssSelector("iframe, frame"));
    }

}
