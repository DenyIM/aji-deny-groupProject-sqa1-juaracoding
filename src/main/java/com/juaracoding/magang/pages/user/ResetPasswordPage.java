package com.juaracoding.magang.pages.user;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object Model untuk Halaman Pemulihan / Lupa Kata Sandi (https://magang.dikahadir.com/absen/lupa-password).
 * Mencakup skenario SIT_USR_UI_025 s/d SIT_USR_UI_032.
 * Diampu oleh Tester Aji (User Magang).
 */
public class ResetPasswordPage extends BasePage {

    public static final String FORGOT_PASSWORD_URL = Constants.BASE_URL + "/absen/lupa-password";
    public static final String RESET_PASSWORD_EXPIRED_URL = Constants.BASE_URL + "/absen/reset-password?token=EXPIRED_TOKEN_999";

    @FindBy(css = "input#email, input[name='email'], input[type='email']")
    private WebElement emailInput;

    @FindBy(css = "button[type='submit'], button.MuiButton-containedPrimary")
    private WebElement sendRequestButton;

    @FindBy(css = "input#newPassword, input[name='newPassword'], input[name='password']")
    private WebElement newPasswordInput;

    @FindBy(css = "input#confirmPassword, input[name='confirmPassword'], input[name='passwordConfirmation']")
    private WebElement confirmPasswordInput;

    @FindBy(xpath = "//button[contains(text(), 'Simpan') or contains(text(), 'Reset') or @type='submit']")
    private WebElement saveNewPasswordButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    @FindBy(xpath = "//a[contains(@href, 'login') or contains(text(), 'Kembali ke Login') or contains(text(), 'Masuk')]")
    private WebElement backToLoginLink;

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToForgotPasswordPage() {
        driver.get(FORGOT_PASSWORD_URL);
        try {
            waitForVisibility(emailInput);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Forgot Password Page: {}", FORGOT_PASSWORD_URL);
    }

    public void navigateToExpiredResetUrl() {
        driver.get(RESET_PASSWORD_EXPIRED_URL);
        logger.info("Navigated to Expired Token Reset URL: {}", RESET_PASSWORD_EXPIRED_URL);
    }

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void clickSendRequest() {
        click(sendRequestButton);
    }

    public void requestPasswordReset(String email) {
        enterEmail(email);
        clickSendRequest();
    }

    public void enterNewPassword(String newPassword) {
        type(newPasswordInput, newPassword);
    }

    public void enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordInput, confirmPassword);
    }

    public void clickSaveNewPassword() {
        click(saveNewPasswordButton);
    }

    public void submitNewPassword(String newPassword, String confirmPassword) {
        enterNewPassword(newPassword);
        enterConfirmPassword(confirmPassword);
        clickSaveNewPassword();
    }

    public void clickBackToLogin() {
        click(backToLoginLink);
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

    public String getEmailValidationMessage() {
        try {
            return waitForVisibility(emailInput).getAttribute("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isEmailFieldDisplayed() {
        return isDisplayed(emailInput);
    }
}
