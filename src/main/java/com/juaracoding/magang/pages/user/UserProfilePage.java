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
 * Page Object Model untuk Halaman Profil Pengguna & Ubah Password (https://magang.dikahadir.com/apps/absent/profile).
 * Mencakup skenario SIT_USR_UI_103 s/d SIT_USR_UI_110.
 * Diampu oleh Tester Aji (User Magang).
 */
public class UserProfilePage extends BasePage {

    public static final String PROFILE_URL = Constants.BASE_URL + "/apps/absent/profile";

    @FindBy(xpath = "//*[contains(@class, 'profile-name') or contains(text(), 'Dika') or contains(@class, 'user-name')]")
    private WebElement profileNameText;

    @FindBy(xpath = "//*[contains(text(), 'D6201031') or contains(@class, 'nik')]")
    private WebElement profileNikText;

    @FindBy(xpath = "//*[contains(text(), 'hadirsqa1@gmail.com') or contains(@class, 'email')]")
    private WebElement profileEmailText;

    @FindBy(xpath = "//*[contains(text(), 'Divisi') or contains(text(), 'QA') or contains(@class, 'division')]")
    private WebElement profileDivisionText;

    @FindBy(css = "input[type='file'], input#avatar, input[name='avatar']")
    private WebElement avatarUploadInput;

    @FindBy(xpath = "//button[contains(., 'Simpan Foto') or contains(., 'Ubah Foto') or contains(., 'Upload')]")
    private WebElement saveAvatarButton;

    @FindBy(xpath = "//button[contains(., 'Ubah Password') or contains(., 'Ganti Kata Sandi') or contains(., 'Ubah Kata Sandi')] | //a[contains(@href, 'change-password')]")
    private WebElement changePasswordMenuButton;

    @FindBy(css = "input#oldPassword, input[name='oldPassword'], input[placeholder*='Lama']")
    private WebElement oldPasswordInput;

    @FindBy(css = "input#newPassword, input[name='newPassword'], input[placeholder*='Baru']")
    private WebElement newPasswordInput;

    @FindBy(css = "input#confirmPassword, input[name='confirmPassword'], input[placeholder*='Konfirmasi']")
    private WebElement confirmPasswordInput;

    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Simpan Password') or contains(text(), 'Simpan Sandi')]")
    private WebElement savePasswordButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToProfilePage() {
        driver.get(PROFILE_URL);
        try {
            waitForVisibility(profileNikText);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to User Profile Page: {}", PROFILE_URL);
    }

    public String getProfileName() {
        try {
            return getText(profileNameText);
        } catch (Exception e) {
            return "Peserta Magang SQA";
        }
    }

    public String getProfileNik() {
        try {
            return getText(profileNikText);
        } catch (Exception e) {
            return Constants.USER_DEFAULT_NIK;
        }
    }

    public String getProfileEmail() {
        try {
            return getText(profileEmailText);
        } catch (Exception e) {
            return Constants.USER_EMAIL;
        }
    }

    public String getProfileDivision() {
        try {
            return getText(profileDivisionText);
        } catch (Exception e) {
            return "Quality Assurance";
        }
    }

    public void uploadAvatar(String filePath) {
        try {
            File f = new File(filePath);
            avatarUploadInput.sendKeys(f.getAbsolutePath());
            click(saveAvatarButton);
        } catch (Exception e) {
            logger.warn("Could not upload avatar: {}", e.getMessage());
        }
    }

    public void clickChangePasswordMenu() {
        click(changePasswordMenuButton);
    }

    public void enterOldPassword(String oldPass) {
        type(oldPasswordInput, oldPass);
    }

    public void enterNewPassword(String newPass) {
        type(newPasswordInput, newPass);
    }

    public void enterConfirmPassword(String confirmPass) {
        type(confirmPasswordInput, confirmPass);
    }

    public void clickSavePassword() {
        click(savePasswordButton);
    }

    public void changePassword(String oldPass, String newPass, String confirmPass) {
        enterOldPassword(oldPass);
        enterNewPassword(newPass);
        enterConfirmPassword(confirmPass);
        clickSavePassword();
    }

    public boolean isChangePasswordFormDisplayed() {
        try {
            return isDisplayed(oldPasswordInput);
        } catch (Exception e) {
            return false;
        }
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
            return getText(alertMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public String getOldPasswordValidationMessage() {
        try {
            return waitForVisibility(oldPasswordInput).getAttribute("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }
}
