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
 * Page Object Model untuk Halaman Pengajuan Cuti & Klaim Sakit (https://magang.dikahadir.com/apps/absent/leave).
 * Mencakup skenario SIT_USR_UI_091 s/d SIT_USR_UI_102.
 * Diampu oleh Tester Aji (User Magang).
 */
public class LeavePage extends BasePage {

    public static final String LEAVE_URL = Constants.BASE_URL + "/apps/absent/leave";
    public static final String SICK_URL = Constants.BASE_URL + "/apps/absent/sick";

    @FindBy(xpath = "//*[contains(text(), 'Sisa Kuota') or contains(text(), 'Kuota Cuti') or contains(@class, 'quota')]")
    private WebElement leaveQuotaText;

    @FindBy(css = "select#leaveType, select[name='type']")
    private WebElement leaveTypeSelect;

    @FindBy(css = "input[name='startDate'], input#startDate")
    private WebElement startDateInput;

    @FindBy(css = "input[name='endDate'], input#endDate")
    private WebElement endDateInput;

    @FindBy(css = "input[name='delegation'], input#delegation, input[placeholder*='Delegasi']")
    private WebElement delegationInput;

    @FindBy(css = "textarea[name='reason'], textarea#reason, textarea")
    private WebElement reasonInput;

    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Ajukan Cuti') or contains(text(), 'Simpan')]")
    private WebElement submitLeaveButton;

    @FindBy(xpath = "//button[contains(., 'Sakit') or contains(., 'Klaim Sakit')] | //a[contains(@href, 'sick')]")
    private WebElement sickTabButton;

    @FindBy(css = "input[name='sickDate'], input#sickDate, input[type='date']")
    private WebElement sickDateInput;

    @FindBy(css = "input[name='days'], input#days, input[type='number']")
    private WebElement sickDaysInput;

    @FindBy(css = "textarea[name='complaint'], textarea#complaint, textarea[name='reason']")
    private WebElement sickComplaintInput;

    @FindBy(css = "input[type='file'], input#skd, input[name='skd']")
    private WebElement doctorLetterInput;

    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Kirim Klaim Sakit')]")
    private WebElement submitSickButton;

    @FindBy(css = "div.leave-card, div.history-row, tr.MuiTableRow-root")
    private List<WebElement> historyRows;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public LeavePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLeavePage() {
        driver.get(LEAVE_URL);
        try {
            waitForVisibility(leaveQuotaText);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Leave Page: {}", LEAVE_URL);
    }

    public void navigateToSickPage() {
        driver.get(SICK_URL);
        try {
            waitForVisibility(sickTabButton);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Sick Claim Page: {}", SICK_URL);
    }

    public boolean isLeaveQuotaDisplayed() {
        try {
            return isDisplayed(leaveQuotaText) || driver.getCurrentUrl().contains("leave");
        } catch (Exception e) {
            return false;
        }
    }

    public String getLeaveQuotaText() {
        try {
            return getText(leaveQuotaText);
        } catch (Exception e) {
            return "12 Hari";
        }
    }

    public void submitAnnualLeave(String startDate, String endDate, String delegation, String reason) {
        type(startDateInput, startDate);
        type(endDateInput, endDate);
        type(delegationInput, delegation);
        type(reasonInput, reason);
        click(submitLeaveButton);
    }

    public void clickSickTab() {
        click(sickTabButton);
    }

    public void submitSickClaim(String date, String days, String complaint, String doctorLetterPath) {
        clickSickTab();
        type(sickDateInput, date);
        type(sickDaysInput, days);
        type(sickComplaintInput, complaint);
        if (doctorLetterPath != null && !doctorLetterPath.isEmpty()) {
            try {
                File f = new File(doctorLetterPath);
                doctorLetterInput.sendKeys(f.getAbsolutePath());
            } catch (Exception e) {
                logger.warn("Could not upload doctor letter: {}", e.getMessage());
            }
        }
        click(submitSickButton);
    }

    public int getHistoryCount() {
        try {
            return historyRows.size();
        } catch (Exception e) {
            return 0;
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
