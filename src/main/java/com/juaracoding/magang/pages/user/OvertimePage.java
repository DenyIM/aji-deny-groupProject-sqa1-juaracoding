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
 * Page Object Model untuk Halaman Pengajuan Lembur (https://magang.dikahadir.com/apps/absent/overtime).
 * Mencakup skenario SIT_USR_UI_081 s/d SIT_USR_UI_090.
 * Diampu oleh Tester Aji (User Magang).
 */
public class OvertimePage extends BasePage {

    public static final String OVERTIME_URL = Constants.BASE_URL + "/apps/absent/overtime";

    @FindBy(xpath = "//button[contains(., 'Ajukan Lembur') or contains(., 'Tambah') or contains(., '+')]")
    private WebElement openOvertimeModalButton;

    @FindBy(css = "input[name='date'], input#date, input[type='date']")
    private WebElement dateInput;

    @FindBy(css = "input[name='startTime'], input#startTime, input[placeholder*='Mulai']")
    private WebElement startTimeInput;

    @FindBy(css = "input[name='endTime'], input#endTime, input[placeholder*='Selesai']")
    private WebElement endTimeInput;

    @FindBy(css = "textarea[name='task'], textarea#task, textarea[name='description']")
    private WebElement taskDescriptionInput;

    @FindBy(css = "input[type='file'], input#spk")
    private WebElement spkFileInput;

    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Kirim') or contains(text(), 'Simpan')]")
    private WebElement submitOvertimeButton;

    @FindBy(css = "div.overtime-item, div.history-row, tr.MuiTableRow-root")
    private List<WebElement> overtimeCards;

    @FindBy(css = "select#monthFilter, select[name='month']")
    private WebElement monthFilterSelect;

    @FindBy(xpath = "//button[contains(., 'Batal') or contains(., 'Cancel')]")
    private WebElement cancelOvertimeButton;

    @FindBy(xpath = "//button[contains(., 'Ya') or contains(., 'Konfirmasi')]")
    private WebElement confirmCancelButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public OvertimePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToOvertimePage() {
        driver.get(OVERTIME_URL);
        try {
            waitForVisibility(openOvertimeModalButton);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Overtime Page: {}", OVERTIME_URL);
    }

    public void clickOpenOvertimeModal() {
        click(openOvertimeModalButton);
    }

    public void enterDate(String date) {
        type(dateInput, date);
    }

    public void enterStartTime(String startTime) {
        type(startTimeInput, startTime);
    }

    public void enterEndTime(String endTime) {
        type(endTimeInput, endTime);
    }

    public void enterTaskDescription(String task) {
        type(taskDescriptionInput, task);
    }

    public void uploadSpk(String filePath) {
        try {
            File f = new File(filePath);
            spkFileInput.sendKeys(f.getAbsolutePath());
        } catch (Exception e) {
            logger.warn("Could not upload SPK file: {}", e.getMessage());
        }
    }

    public void clickSubmitOvertime() {
        click(submitOvertimeButton);
    }

    public void submitOvertime(String date, String start, String end, String task, String spkPath) {
        clickOpenOvertimeModal();
        enterDate(date);
        enterStartTime(start);
        enterEndTime(end);
        enterTaskDescription(task);
        if (spkPath != null && !spkPath.isEmpty()) {
            uploadSpk(spkPath);
        }
        clickSubmitOvertime();
    }

    public int getOvertimeCount() {
        try {
            return overtimeCards.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void filterByMonth(String month) {
        try {
            click(monthFilterSelect);
            WebElement option = driver.findElement(By.xpath("//option[contains(text(), '" + month + "')]"));
            click(option);
        } catch (Exception ignored) {
        }
    }

    public void cancelPendingOvertime() {
        click(cancelOvertimeButton);
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

    public String getTaskValidationMessage() {
        try {
            return waitForVisibility(taskDescriptionInput).getAttribute("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }
}
