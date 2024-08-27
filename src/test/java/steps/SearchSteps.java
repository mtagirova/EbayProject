package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class SearchSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Increased wait time
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I am on the search page")
    public void i_am_on_the_search_page() {
        driver.get("https://veradigm.com/search/");
        handlePopup(By.xpath("//button[contains(@id, 'AllowAll')]"), "Cookie consent 'Allow all' button");
        handlePopup(By.xpath("//button[@class='drift-widget-naked-button drift-widget-message-close-button drift-widget-close-button--align-right']"), "Pop-up close button");

        verifyElement(By.xpath("//h1[contains(text(),'Search')]"), "Search page heading");
        verifyElement(By.xpath("//a[@id='search-link']"), "Search link");
    }

    @When("I insert {string} in the search box")
    public void i_insert_in_the_search_box(String searchText) {
        interactWithElement(By.id("searchBox"), searchText, "search box");
    }

    @When("I click on the search box")
    public void i_click_on_the_search_box() {
        interactWithElement(By.id("searchBox"), null, "search box");
    }

    @Then("I should see results related to {string}")
    public void i_should_see_results_related_to(String expectedText) {
        assertTextContains(By.id("resultsContainer"), expectedText, "results");
    }

    private void handlePopup(By locator, String description) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            if (element.isDisplayed()) {
                element.click();
                logAction(description + " clicked.");
            }
        } catch (TimeoutException e) {
            logAction(description + " did not appear within the timeout period.");
        } catch (Exception e) {
            logError("Error handling " + description + ": " + e.getMessage());
        }
    }

    private void interactWithElement(By locator, String text, String description) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            if (text != null) {
                element.sendKeys(text);
                logAction("Text entered in " + description);
            } else {
                element.click();
                logAction(description + " clicked.");
            }
        } catch (Exception e) {
            logError("Failed to interact with " + description + ": " + e.getMessage());
        }
    }

    private void verifyElement(By locator, String description) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            assertTrue(description + " is not visible", element.isDisplayed());
        } catch (Exception e) {
            throw new AssertionError("Failed to verify " + description, e);
        }
    }

    private void assertTextContains(By locator, String expectedText, String description) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String text = element.getText();
            assertTrue("Expected text not found in " + description, text.contains(expectedText));
        } catch (Exception e) {
            throw new AssertionError("Failed to verify text in " + description, e);
        }
    }

    private void logAction(String message) {
        System.out.println(message);
    }

    private void logError(String message) {
        System.err.println(message);
    }

    private void handleAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
            logAction("Alert accepted.");
        } catch (TimeoutException e) {
            logAction("No alert appeared within the timeout period.");
        } catch (Exception e) {
            logError("Error handling the alert: " + e.getMessage());
        }
    }
}
