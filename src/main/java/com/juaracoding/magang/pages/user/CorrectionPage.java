package com.juaracoding.magang.pages.user;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.util.List;

/**
 * Page Object Model untuk Halaman Pengajuan Koreksi Absen (https://magang.dikahadir.com/apps/absent/correction).
 * Mencakup skenario SIT_USR_UI_061 s/d SIT_USR_UI_070.
 * Diampu oleh Tester Aji (User Magang).
 */
public class CorrectionPage extends BasePage {

    public static final String CORRECTION_URL = Constants.BASE_URL + "/apps/absent/correction";

    @FindBy(xpath = "//button[contains(., 'Ajukan Koreksi') or contains(., 'Tambah') or contains(., '+')]")
    private WebElement openCorrectionModalButton;

    @FindBy(css = "input[name='date'], input#date, input[type='date']")
    private WebElement dateInput;

    @FindBy(css = "input[name='checkIn'], input#checkIn, input[name='clockIn']")
    private WebElement clockInInput;

    @FindBy(css = "input[name='checkOut'], input#checkOut, input[name='clockOut']")
    private WebElement clockOutInput;

    @FindBy(css = "textarea[name='reason'], textarea#reason, textarea")
    private WebElement reasonInput;

    @FindBy(css = "input[type='file'], input#proof")
    private WebElement proofFileInput;

    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Kirim') or contains(text(), 'Simpan')]")
    private WebElement submitCorrectionButton;

    @FindBy(css = "div.correction-item, div.history-row, tr.MuiTableRow-root")
    private List<WebElement> correctionList;

    @FindBy(xpath = "//*[contains(text(), 'Pending') or contains(text(), 'Menunggu') or contains(@class, 'status-pending')]")
    private WebElement pendingStatusBadge;

    @FindBy(xpath = "//button[contains(., 'Batal') or contains(., 'Cancel') or contains(@aria-label, 'cancel')]")
    private WebElement cancelCorrectionButton;

    @FindBy(xpath = "//button[contains(., 'Ya') or contains(., 'Konfirmasi')]")
    private WebElement confirmCancelButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public CorrectionPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToCorrectionPage() {
        driver.get(CORRECTION_URL);
        try {
            waitForVisibility(openCorrectionModalButton);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Correction Page: {}", CORRECTION_URL);
    }

    public void clickOpenCorrectionModal() {
        click(openCorrectionModalButton);
    }

    public void enterDate(String date) {
        type(dateInput, date);
    }

    public void enterClockInTime(String time) {
        type(clockInInput, time);
    }

    public void enterClockOutTime(String time) {
        type(clockOutInput, time);
    }

    public void enterReason(String reason) {
        type(reasonInput, reason);
    }

    public void uploadProof(String filePath) {
        try {
            File f = new File(filePath);
            proofFileInput.sendKeys(f.getAbsolutePath());
        } catch (Exception e) {
            logger.warn("Could not upload proof: {}", e.getMessage());
        }
    }

    public void clickSubmitCorrection() {
        click(submitCorrectionButton);
    }

    public void submitCorrection(String date, String clockIn, String clockOut, String reason, String proofPath) {
        clickOpenCorrectionModal();
        enterDate(date);
        enterClockInTime(clockIn);
        enterClockOutTime(clockOut);
        enterReason(reason);
        if (proofPath != null && !proofPath.isEmpty()) {
            uploadProof(proofPath);
        }
        clickSubmitCorrection();
    }

    public boolean isPendingBadgeDisplayed() {
        try {
            return isDisplayed(pendingStatusBadge);
        } catch (Exception e) {
            return false;
        }
    }

    public void cancelPendingCorrection() {
        click(cancelCorrectionButton);
        try {
            waitForClickability(confirmCancelButton);
            click(confirmCancelButton);
        } catch (Exception ignored) {
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

    public String getReasonValidationMessage() {
        try {
            return waitForVisibility(reasonInput).getAttribute("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }
}
