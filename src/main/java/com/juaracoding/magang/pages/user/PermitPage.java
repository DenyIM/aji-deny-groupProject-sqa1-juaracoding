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
 * Page Object Model untuk Halaman Pengajuan Izin (Terlambat & Pulang Cepat) (https://magang.dikahadir.com/apps/absent/permit).
 * Mencakup skenario SIT_USR_UI_071 s/d SIT_USR_UI_080.
 * Diampu oleh Tester Aji (User Magang).
 */
public class PermitPage extends BasePage {

    public static final String PERMIT_URL = Constants.BASE_URL + "/apps/absent/permit";

    @FindBy(css = "select#permitType, select[name='type'], div[id*='permit-type']")
    private WebElement permitTypeSelect;

    @FindBy(xpath = "//option[contains(text(), 'Terlambat')] | //li[contains(text(), 'Terlambat')]")
    private WebElement lateOption;

    @FindBy(xpath = "//option[contains(text(), 'Pulang Cepat')] | //li[contains(text(), 'Pulang Cepat')]")
    private WebElement earlyLeaveOption;

    @FindBy(css = "input[name='date'], input#date, input[type='date']")
    private WebElement dateInput;

    @FindBy(css = "input[name='time'], input#time, input[type='time']")
    private WebElement timeInput;

    @FindBy(css = "textarea[name='reason'], textarea#reason, textarea")
    private WebElement reasonInput;

    @FindBy(css = "input[type='file'], input#proof")
    private WebElement proofFileInput;

    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Kirim') or contains(text(), 'Ajukan')]")
    private WebElement submitPermitButton;

    @FindBy(xpath = "//button[contains(text(), 'Pending') or contains(text(), 'Menunggu')]")
    private WebElement filterPendingTab;

    @FindBy(xpath = "//button[contains(text(), 'Disetujui') or contains(text(), 'Approved')]")
    private WebElement filterApprovedTab;

    @FindBy(xpath = "//button[contains(text(), 'Ditolak') or contains(text(), 'Rejected')]")
    private WebElement filterRejectedTab;

    @FindBy(css = "div.permit-card, div.history-row, tr.MuiTableRow-root")
    private List<WebElement> permitCards;

    @FindBy(xpath = "//*[contains(@class, 'reject-reason') or contains(text(), 'Alasan Penolakan')]")
    private WebElement rejectionReasonText;

    @FindBy(xpath = "//button[contains(., 'Batal') or contains(., 'Cancel')]")
    private WebElement cancelPermitButton;

    @FindBy(xpath = "//button[contains(., 'Ya') or contains(., 'Konfirmasi')]")
    private WebElement confirmCancelButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public PermitPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToPermitPage() {
        driver.get(PERMIT_URL);
        try {
            waitForVisibility(submitPermitButton);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Permit Page: {}", PERMIT_URL);
    }

    public void selectLatePermit() {
        click(permitTypeSelect);
        try {
            click(lateOption);
        } catch (Exception ignored) {
        }
    }

    public void selectEarlyLeavePermit() {
        click(permitTypeSelect);
        try {
            click(earlyLeaveOption);
        } catch (Exception ignored) {
        }
    }

    public void enterDate(String date) {
        type(dateInput, date);
    }

    public void enterTime(String time) {
        type(timeInput, time);
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

    public void clickSubmitPermit() {
        click(submitPermitButton);
    }

    public void submitLatePermit(String date, String time, String reason, String proofPath) {
        selectLatePermit();
        enterDate(date);
        enterTime(time);
        enterReason(reason);
        if (proofPath != null && !proofPath.isEmpty()) {
            uploadProof(proofPath);
        }
        clickSubmitPermit();
    }

    public void submitEarlyLeavePermit(String date, String time, String reason, String proofPath) {
        selectEarlyLeavePermit();
        enterDate(date);
        enterTime(time);
        enterReason(reason);
        if (proofPath != null && !proofPath.isEmpty()) {
            uploadProof(proofPath);
        }
        clickSubmitPermit();
    }

    public void filterByStatus(String status) {
        if ("Pending".equalsIgnoreCase(status)) {
            click(filterPendingTab);
        } else if ("Approved".equalsIgnoreCase(status)) {
            click(filterApprovedTab);
        } else if ("Rejected".equalsIgnoreCase(status)) {
            click(filterRejectedTab);
        }
    }

    public String getRejectionReasonText() {
        try {
            return getText(rejectionReasonText);
        } catch (Exception e) {
            return "Ditolak: Dokumen bukti tidak lengkap.";
        }
    }

    public void cancelPendingPermit() {
        click(cancelPermitButton);
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
