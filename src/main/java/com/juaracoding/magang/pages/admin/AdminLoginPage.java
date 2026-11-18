package com.juaracoding.magang.pages.admin;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object Model untuk Halaman Login Admin HRD (https://magang.dikahadir.com/authentication/login).
 * Mencakup cakupan skenario SIT_ADM_UI_001 s/d SIT_ADM_UI_012.
 * Diampu oleh Tester Deny (Admin HRD) pada branch feature/deny-admin-login.
 */
public class AdminLoginPage extends BasePage {

    public static final String ADMIN_DASHBOARD_URL = Constants.BASE_URL + "/dashboards/pending";

    @FindBy(css = "input#email, input[name='email'], input[type='email']")
    private WebElement emailInput;

    @FindBy(css = "input#password")
    private WebElement passwordInput;

    @FindBy(css = "div.MuiInputAdornment-root button[type='button']")
    private WebElement showHidePasswordButton;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = "div.MuiAlert-message")
    private WebElement alertMessage;

    @FindBy(xpath = "//header//button[.//div[contains(@class,'MuiAvatar-root')] or .//h5 or position()=last()]")
    private WebElement profileMenuButton;

    @FindBy(xpath = "//button[contains(., 'Logout') or contains(., 'Keluar')] | //li[contains(., 'Logout') or contains(., 'Keluar')]")
    private WebElement logoutButton;

    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        driver.get(Constants.ADMIN_LOGIN_URL);
        waitForVisibility(emailInput);
        logger.info("Navigated to Admin Login Page: {}", Constants.ADMIN_LOGIN_URL);
    }

    public void navigateToProtectedDashboardDirectly() {
        driver.get(ADMIN_DASHBOARD_URL);
        logger.info("Directly accessed protected Admin Dashboard URL: {}", ADMIN_DASHBOARD_URL);
    }

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void enterPassword(String plainPassword) {
        type(passwordInput, plainPassword);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public void login(String email, String plainPassword) {
        logger.info("Performing Admin Login with email: {}", email);
        enterEmail(email);
        enterPassword(plainPassword);
        clickLoginButton();
    }

    public void loginWithDefaultAdmin() {
        String decodedPassword = SecurityUtil.decode(Constants.ADMIN_PASSWORD_ENCODED);
        login(Constants.ADMIN_EMAIL, decodedPassword);
    }

    public void clickShowHidePassword() {
        click(showHidePasswordButton);
    }

    public String getPasswordFieldType() {
        return waitForVisibility(passwordInput).getAttribute("type");
    }

    public String getPasswordFieldValue() {
        return waitForVisibility(passwordInput).getAttribute("value");
    }

    public String getEmailValidationMessage() {
        return waitForVisibility(emailInput).getAttribute("validationMessage");
    }

    public boolean isEmailFieldDisplayed() {
        return isDisplayed(emailInput);
    }

    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(passwordInput);
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }

    public boolean isAlertMessageDisplayed() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOf(alertMessage));
            return el.isDisplayed() && !el.getText().trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getAlertText() {
        try {
            wait.until(d -> !alertMessage.getText().trim().isEmpty());
            return alertMessage.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean waitForUrlContains(String fraction) {
        try {
            return wait.until(ExpectedConditions.urlContains(fraction));
        } catch (Exception e) {
            return driver.getCurrentUrl().contains(fraction);
        }
    }

    public void performLogout() {
        logger.info("Attempting Admin Logout from dashboard...");
        try {
            waitForClickability(profileMenuButton);
            click(profileMenuButton);
            waitForClickability(logoutButton);
            click(logoutButton);
        } catch (Exception e) {
            logger.warn("Fallback logout menu lookup: {}", e.getMessage());
            try {
                WebElement fallbackProfileBtn = driver.findElement(By.cssSelector("header button[aria-label='menu'], header button:last-of-type"));
                click(fallbackProfileBtn);
                WebElement fallbackLogoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[contains(text(),'Logout') or contains(text(),'Keluar')]")));
                click(fallbackLogoutBtn);
            } catch (Exception ex) {
                driver.manage().deleteAllCookies();
                driver.navigate().to(Constants.ADMIN_LOGIN_URL);
            }
        }
    }
}
