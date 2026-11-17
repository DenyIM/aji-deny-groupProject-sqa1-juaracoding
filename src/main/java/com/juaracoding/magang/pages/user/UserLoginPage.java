package com.juaracoding.magang.pages.user;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model untuk Halaman Login User Magang Mobile Mirroring (https://magang.dikahadir.com/absen/login).
 * Diampu oleh Tester Aji (User Magang).
 */
public class UserLoginPage extends BasePage {

    @FindBy(css = "input[name='email'], input#email, input[type='email']")
    private WebElement emailInput;

    @FindBy(css = "input[name='password'], input#password, input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    @FindBy(xpath = "//a[contains(@href, 'register') or contains(text(), 'Daftar') or contains(text(), 'Register')]")
    private WebElement registerLink;

    public UserLoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        driver.get(Constants.USER_LOGIN_URL);
        logger.info("Navigated to User Login Page: {}", Constants.USER_LOGIN_URL);
    }

    public void login(String email, String plainPassword) {
        logger.info("Performing User Login with email: {}", email);
        type(emailInput, email);
        type(passwordInput, plainPassword);
        click(loginButton);
    }

    public void loginWithDefaultUser() {
        String decodedPassword = SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED);
        login(Constants.USER_EMAIL, decodedPassword);
    }

    public boolean isAlertMessageDisplayed() {
        return isDisplayed(alertMessage);
    }

    public String getAlertText() {
        return getText(alertMessage);
    }

    public boolean isRegisterLinkDisplayed() {
        return isDisplayed(registerLink);
    }
}

