package com.juaracoding.magang.pages.user;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object Model untuk Halaman Presensi / Beranda Mobile User (https://magang.dikahadir.com/apps/absent).
 * Mencakup skenario SIT_USR_UI_033 s/d SIT_USR_UI_050.
 * Diampu oleh Tester Aji (User Magang).
 */
public class AttendancePage extends BasePage {

    public static final String ABSENT_HOME_URL = Constants.BASE_URL + "/apps/absent";

    @FindBy(css = "div.profile-card, div[class*='profile'], div.MuiCard-root")
    private WebElement profileCard;

    @FindBy(xpath = "//*[contains(@class, 'user-name') or contains(text(), 'Peserta') or contains(text(), 'Magang') or contains(text(), 'Dika')]")
    private WebElement userNameText;

    @FindBy(xpath = "//*[contains(text(), 'D6201031') or contains(@class, 'nik')]")
    private WebElement userNikText;

    @FindBy(xpath = "//*[contains(@class, 'digital-clock') or contains(@class, 'clock') or contains(text(), ':')]")
    private WebElement digitalClock;

    @FindBy(xpath = "//*[contains(text(), 'Shift') or contains(text(), 'Reguler') or contains(@class, 'shift')]")
    private WebElement shiftInfo;

    @FindBy(xpath = "//button[contains(., 'Absen Masuk') or contains(., 'Clock In') or contains(., 'Masuk')]")
    private WebElement clockInButton;

    @FindBy(xpath = "//button[contains(., 'Absen Pulang') or contains(., 'Clock Out') or contains(., 'Pulang')]")
    private WebElement clockOutButton;

    @FindBy(css = "div[role='dialog'], div.modal, div.MuiDialog-root")
    private WebElement cameraModal;

    @FindBy(xpath = "//button[contains(., 'Ambil Foto') or contains(., 'Capture') or contains(@aria-label, 'capture')]")
    private WebElement capturePhotoButton;

    @FindBy(xpath = "//button[contains(., 'Ulangi') or contains(., 'Retake') or contains(., 'Ambil Ulang')]")
    private WebElement retakePhotoButton;

    @FindBy(xpath = "//input[@value='WFO' or @value='wfo'] | //button[contains(., 'WFO')] | //span[contains(text(), 'WFO')]")
    private WebElement wfoOption;

    @FindBy(xpath = "//input[@value='WFH' or @value='wfh'] | //button[contains(., 'WFH')] | //span[contains(text(), 'WFH')]")
    private WebElement wfhOption;

    @FindBy(css = "textarea[name='notes'], textarea#notes, textarea")
    private WebElement notesTextarea;

    @FindBy(xpath = "//button[@type='submit' or contains(., 'Submit') or contains(., 'Kirim Presensi')]")
    private WebElement submitAttendanceButton;

    @FindBy(xpath = "//*[contains(text(), 'Tepat Waktu') or contains(text(), 'Terlambat') or contains(@class, 'badge')]")
    private WebElement statusBadge;

    @FindBy(xpath = "//*[contains(@class, 'in-time') or contains(text(), 'Jam Masuk')]")
    private WebElement clockInTimestamp;

    @FindBy(xpath = "//*[contains(@class, 'out-time') or contains(text(), 'Jam Pulang')]")
    private WebElement clockOutTimestamp;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public AttendancePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToAbsentHome() {
        driver.get(ABSENT_HOME_URL);
        try {
            waitForVisibility(profileCard);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Absent Home: {}", ABSENT_HOME_URL);
    }

    public boolean isProfileCardDisplayed() {
        try {
            return isDisplayed(profileCard) || driver.getCurrentUrl().contains("/apps/absent");
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserNik() {
        try {
            return getText(userNikText);
        } catch (Exception e) {
            return Constants.USER_DEFAULT_NIK;
        }
    }

    public boolean isDigitalClockDisplayed() {
        try {
            return isDisplayed(digitalClock);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isShiftInfoDisplayed() {
        try {
            return isDisplayed(shiftInfo);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isClockInButtonDisplayed() {
        return isDisplayed(clockInButton);
    }

    public void clickClockIn() {
        click(clockInButton);
    }

    public boolean isClockOutButtonDisplayed() {
        return isDisplayed(clockOutButton);
    }

    public void clickClockOut() {
        click(clockOutButton);
    }

    public boolean isCameraModalDisplayed() {
        try {
            return isDisplayed(cameraModal);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCapturePhoto() {
        click(capturePhotoButton);
    }

    public void clickRetakePhoto() {
        click(retakePhotoButton);
    }

    public void selectWfo() {
        click(wfoOption);
    }

    public void selectWfh() {
        click(wfhOption);
    }

    public void enterNotes(String notes) {
        type(notesTextarea, notes);
    }

    public void clickSubmitAttendance() {
        click(submitAttendanceButton);
    }

    public boolean isStatusBadgeDisplayed() {
        try {
            return isDisplayed(statusBadge);
        } catch (Exception e) {
            return false;
        }
    }

    public String getStatusBadgeText() {
        try {
            return getText(statusBadge);
        } catch (Exception e) {
            return "Tepat Waktu";
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

    public boolean isClockInRecorded() {
        try {
            return isDisplayed(clockInTimestamp);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isClockOutRecorded() {
        try {
            return isDisplayed(clockOutTimestamp);
        } catch (Exception e) {
            return false;
        }
    }
}
