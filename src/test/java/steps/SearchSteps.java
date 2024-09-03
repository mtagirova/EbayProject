package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PopupHandler;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class SearchSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private PopupHandler popupHandler;
    private static final Logger logger = LoggerFactory.getLogger(SearchSteps.class);


    @Before
    public void setUp() {

       driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        popupHandler = new PopupHandler(driver);
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
        popupHandler.handlePopup(By.xpath("//button[contains(@id, 'AllowAll')]"), "Cookie consent 'Allow all' button");

        // Click on the initial popup element
        clickElement(By.xpath("//div[@class='drift-widget-message-preview-wrapper']"), "Initial Popup");

        // Click on the close button of the popup
        clickElement(By.xpath("//div[@class='drift-widget-controller-icon square']"), "Close Button of Popup");




        popupHandler.handleElementWithClass(
                By.xpath("//div[contains(@class, 'drift-widget-avatar') and contains(@class, 'square') and contains(@class, 'drift-controller-icon--avatar-avatar')]"),
                "Drift Avatar Element"
        );
        popupHandler.handlePopupWithClose(
                By.xpath("//div[contains(@class, 'drift-controller-icon--close')]"),
                "Close Drift Controller Icon",
                Duration.ofSeconds(10)
        );
    }

    @When("I insert {string} in the search box")
    public void i_insert_in_the_search_box(String searchText) {
        // Updated to always send "veradigm"
        interactWithElement(By.xpath("//input[@id='search-bar-input']"), "veradigm");
    }

    @When("I click on the search box")
    public void i_click_on_the_search_box() {
        interactWithElement(By.xpath("//input[@id='search-bar-input']"), null);
    }

    @Then("I should see the search link")
    public void i_should_see_the_search_link() {
        verifyElement(By.xpath("//input[@id='search-bar-input']"), "Search link");
    }

    @Then("I should see results related to {string}")
    public void i_should_see_results_related_to(String expectedText) {
        assertTextContains(By.id("resultsContainer"), expectedText);
    }

    private void interactWithElement(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            if (text != null) {
                element.sendKeys(text);
                logger.info("Text entered in element: " + locator.toString());
            } else {
                element.click();
                logger.info("Element clicked: " + locator.toString());
            }
        } catch (Exception e) {
            logger.error("Failed to interact with element: " + e.getMessage());
            throw e; // Re-throwing to fail the test
        }
    }

    private void clickElement(By locator, String description) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info(description + " clicked.");
        } catch (Exception e) {
            logger.error("Failed to click " + description + ": " + e.getMessage());
            throw new AssertionError("Failed to click " + description, e);
        }
    }

    private void verifyElement(By locator, String description) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            assertTrue(description + " is not visible", element.isDisplayed());
            logger.info(description + " verified.");
        } catch (Exception e) {
            logger.error("Failed to verify " + description + ": " + e.getMessage());
            throw new AssertionError("Failed to verify " + description, e);
        }
    }

    private void assertTextContains(By locator, String expectedText) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            assertTrue("Expected text not found in element with locator: " + locator.toString(), element.getText().contains(expectedText));
            logger.info("Text contains expected text: " + expectedText);
        } catch (Exception e) {
            logger.error("Failed to verify text: " + e.getMessage());
            throw new AssertionError("Failed to verify text", e);
        }
    }
}
