package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PopupHandler {

    private WebDriver driver;
    private WebDriverWait wait;

    public PopupHandler(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void handlePopup(By locator, String description) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            if (element.isDisplayed()) {
                element.click();
                System.out.println(description + " clicked.");
            }
        } catch (Exception e) {
            System.err.println(description + " did not appear or could not be clicked: " + e.getMessage());
        }
    }

    public void handlePopupWithTwoSteps(By revealLocator, By closeLocator, String description, Duration waitTime) {
        String parentWindowHandle = driver.getWindowHandle();
        try {
            WebElement revealElement = wait.withTimeout(waitTime).until(ExpectedConditions.elementToBeClickable(revealLocator));
            revealElement.click();
            System.out.println(description + " revealed.");

            String subWindowHandle = wait.until(driver -> {
                for (String handle : driver.getWindowHandles()) {
                    if (!handle.equals(parentWindowHandle)) {
                        return handle;
                    }
                }
                return null;
            });

            if (subWindowHandle != null) {
                driver.switchTo().window(subWindowHandle);
                WebElement closeElement = wait.until(ExpectedConditions.elementToBeClickable(closeLocator));
                closeElement.click();
                System.out.println(description + " closed.");
                driver.switchTo().window(parentWindowHandle);
            } else {
                System.err.println("No new popup window appeared.");
            }
        } catch (Exception e) {
            System.err.println(description + " handling failed: " + e.getMessage());
        }
    }

    public void handleElementWithClass(By locator, String description) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            if (element.isDisplayed()) {
                element.click();
                System.out.println(description + " handled.");
            }
        } catch (Exception e) {
            System.err.println(description + " did not appear or could not be interacted with: " + e.getMessage());
        }
    }

    public void handlePopupWithClose(By locator, String description, Duration waitTime) {
        try {
            WebElement closeElement = wait.withTimeout(waitTime).until(ExpectedConditions.elementToBeClickable(locator));
            if (closeElement.isDisplayed()) {
                closeElement.click();
                System.out.println(description + " closed.");
            }
        } catch (Exception e) {
            System.err.println(description + " did not appear or could not be closed: " + e.getMessage());
        }
    }
}
