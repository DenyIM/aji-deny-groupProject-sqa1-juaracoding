package com.juaracoding.magang.pages;

import com.juaracoding.magang.utils.Constants;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Kelas induk abstrak untuk seluruh Page Object Model (POM).
 * Menyediakan wrapper explicit wait yang tangguh dan thread-safe.
 */
public abstract class BasePage {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.EXPLICIT_WAIT_TIMEOUT));
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void click(WebElement element) {
        try {
            waitForClickability(element).click();
        } catch (Exception e) {
            logger.warn("Standard click failed, falling back to JavaScript click: {}", e.getMessage());
            clickWithJS(element);
        }
    }

    protected void clickWithJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void type(WebElement element, String text) {
        WebElement el = waitForVisibility(element);
        el.clear();
        if (text != null && !text.isEmpty()) {
            el.sendKeys(text);
        }
    }

    protected String getText(WebElement element) {
        return waitForVisibility(element).getText().trim();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }
}

