package com.juaracoding.magang.pages.user;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;

/**
 * Page Object Model untuk Halaman Pendaftaran Akun Peserta Magang (https://magang.dikahadir.com/absen/register).
 * Mencakup skenario SIT_USR_UI_013 s/d SIT_USR_UI_024.
 * Diampu oleh Tester Aji (User Magang).
 */
public class RegisterPage extends BasePage {

    public static final String REGISTER_URL = Constants.BASE_URL + "/absen/register";

    @FindBy(css = "input#nik, input[name='nik'], input[placeholder*='NIK']")
    private WebElement nikInput;

    @FindBy(xpath = "//button[contains(text(), 'Cek NIK') or contains(text(), 'Cek') or contains(@id, 'check-nik')]")
    private WebElement checkNikButton;

    @FindBy(css = "input#name, input[name='fullname'], input[name='name']")
    private WebElement nameInput;

    @FindBy(css = "input#division, input[name='division'], input[name='unit']")
    private WebElement divisionInput;

    @FindBy(css = "input#email, input[name='email'], input[type='email']")
    private WebElement emailInput;

    @FindBy(css = "input#password, input[name='password'], input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "input[type='file'], input#selfie, input[name='selfie']")
    private WebElement photoUploadInput;

    @FindBy(css = "button[type='submit']")
    private WebElement submitRegisterButton;

    @FindBy(xpath = "//a[contains(@href, 'login') or contains(text(), 'Masuk') or contains(text(), 'Sudah punya akun')]")
    private WebElement backToLoginLink;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToRegisterPage() {
        driver.get(REGISTER_URL);
        try {
            waitForVisibility(nikInput);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Register Page: {}", REGISTER_URL);
    }

    public void enterNik(String nik) {
        type(nikInput, nik);
    }

    public void clickCheckNik() {
        click(checkNikButton);
    }

    public void checkNik(String nik) {
        enterNik(nik);
        clickCheckNik();
    }

    public String getNameInputValue() {
        try {
            return waitForVisibility(nameInput).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getDivisionInputValue() {
        try {
            return waitForVisibility(divisionInput).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void enterPassword(String plainPassword) {
        type(passwordInput, plainPassword);
    }

    public void uploadPhoto(String filePath) {
        try {
            File f = new File(filePath);
            photoUploadInput.sendKeys(f.getAbsolutePath());
        } catch (Exception e) {
            logger.warn("Could not upload file to input: {}", e.getMessage());
        }
    }

    public void clickSubmitRegister() {
        click(submitRegisterButton);
    }

    public void register(String nik, String email, String password, String photoPath) {
        checkNik(nik);
        enterEmail(email);
        enterPassword(password);
        if (photoPath != null && !photoPath.isEmpty()) {
            uploadPhoto(photoPath);
        }
        clickSubmitRegister();
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

    public String getPasswordValidationMessage() {
        try {
            return waitForVisibility(passwordInput).getAttribute("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isNikInputDisplayed() {
        return isDisplayed(nikInput);
    }
}
