package com.juaracoding.magang.pages.user;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object Model untuk Halaman Login User Magang Mobile Mirroring (https://magang.dikahadir.com/absen/login).
 * Mencakup cakupan skenario SIT_USR_UI_001 s/d SIT_USR_UI_012.
 * Diampu oleh Tester Aji (User Magang).
 */
public class UserLoginPage extends BasePage {

    public static final String ABSENT_HOME_URL = Constants.BASE_URL + "/apps/absent";

    @FindBy(css = "input#email, input[name='email'], input[type='email']")
    private WebElement emailInput;

    @FindBy(css = "input#password, input[name='password'], input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "div.MuiInputAdornment-root button, button[aria-label*='password'], button[aria-label*='kata sandi']")
    private WebElement showHidePasswordButton;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    @FindBy(xpath = "//a[contains(@href, 'register') or contains(text(), 'Daftar') or contains(text(), 'Register')]")
    private WebElement registerLink;

    @FindBy(xpath = "//a[contains(@href, 'lupa-password') or contains(@href, 'forgot') or contains(text(), 'Lupa Password') or contains(text(), 'Lupa')]")
    private WebElement forgotPasswordLink;

    @FindBy(xpath = "//header//button[.//div[contains(@class,'MuiAvatar-root')] or position()=last()] | //a[contains(@href,'profile')]")
    private WebElement profileMenuButton;

    @FindBy(xpath = "//button[contains(., 'Logout') or contains(., 'Keluar')] | //li[contains(., 'Logout') or contains(., 'Keluar')] | //div[contains(text(),'Logout')]")
    private WebElement logoutButton;

    public UserLoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        driver.get(Constants.USER_LOGIN_URL);
        try {
            waitForVisibility(emailInput);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to User Login Page: {}", Constants.USER_LOGIN_URL);
    }

    public void navigateToProtectedAbsentDirectly() {
        driver.get(ABSENT_HOME_URL);
        logger.info("Directly accessed protected Absent Home URL: {}", ABSENT_HOME_URL);
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
        logger.info("Performing User Login with email: {}", email);
        enterEmail(email);
        enterPassword(plainPassword);
        clickLoginButton();
    }

    public void loginWithDefaultUser() {
        String decodedPassword = SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED);
        login(Constants.USER_EMAIL, decodedPassword);
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

    public boolean isRegisterLinkDisplayed() {
        return isDisplayed(registerLink);
    }

    public void clickRegisterLink() {
        click(registerLink);
    }

    public boolean isForgotPasswordLinkDisplayed() {
        return isDisplayed(forgotPasswordLink);
    }

    public void clickForgotPasswordLink() {
        click(forgotPasswordLink);
    }

    public boolean waitForUrlContains(String fraction) {
        try {
            return wait.until(ExpectedConditions.urlContains(fraction));
        } catch (Exception e) {
            return driver.getCurrentUrl().contains(fraction);
        }
    }

    public void performLogout() {
        logger.info("Attempting User Logout from mobile session...");
        try {
            driver.get(Constants.BASE_URL + "/apps/absent/profile");
            waitForClickability(logoutButton);
            click(logoutButton);
        } catch (Exception e) {
            logger.warn("Standard user logout menu lookup: {}", e.getMessage());
            try {
                WebElement fallbackLogout = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[contains(text(),'Logout') or contains(text(),'Keluar')]")));
                click(fallbackLogout);
            } catch (Exception ex) {
                driver.manage().deleteAllCookies();
                driver.navigate().to(Constants.USER_LOGIN_URL);
            }
        }
    }
}
