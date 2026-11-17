package com.juaracoding.magang.pages.admin;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model untuk Halaman Login Admin HRD (https://magang.dikahadir.com/authentication/login).
 * Diampu oleh Tester Deny (Admin HRD).
 */
public class AdminLoginPage extends BasePage {

    @FindBy(css = "input[name='email'], input#email, input[type='email']")
    private WebElement emailInput;

    @FindBy(css = "input[name='password'], input#password, input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        driver.get(Constants.ADMIN_LOGIN_URL);
        logger.info("Navigated to Admin Login Page: {}", Constants.ADMIN_LOGIN_URL);
    }

    public void login(String email, String plainPassword) {
        logger.info("Performing Admin Login with email: {}", email);
        type(emailInput, email);
        type(passwordInput, plainPassword);
        click(loginButton);
    }

    public void loginWithDefaultAdmin() {
        String decodedPassword = SecurityUtil.decode(Constants.ADMIN_PASSWORD_ENCODED);
        login(Constants.ADMIN_EMAIL, decodedPassword);
    }

    public boolean isAlertMessageDisplayed() {
        return isDisplayed(alertMessage);
    }

    public String getAlertText() {
        return getText(alertMessage);
    }
}

