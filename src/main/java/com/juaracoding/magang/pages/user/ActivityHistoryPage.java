package com.juaracoding.magang.pages.user;

import com.juaracoding.magang.pages.BasePage;
import com.juaracoding.magang.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object Model untuk Halaman Riwayat & Jurnal Aktivitas User (https://magang.dikahadir.com/apps/absent/activity).
 * Mencakup skenario SIT_USR_UI_051 s/d SIT_USR_UI_060.
 * Diampu oleh Tester Aji (User Magang).
 */
public class ActivityHistoryPage extends BasePage {

    public static final String ACTIVITY_URL = Constants.BASE_URL + "/apps/absent/activity";

    @FindBy(css = "input[name='startDate'], input#startDate, input[placeholder*='Mulai']")
    private WebElement startDateInput;

    @FindBy(css = "input[name='endDate'], input#endDate, input[placeholder*='Sampai']")
    private WebElement endDateInput;

    @FindBy(xpath = "//button[contains(text(), 'Filter') or contains(text(), 'Terapkan')]")
    private WebElement filterButton;

    @FindBy(css = "div.activity-item, div.history-card, div[class*='ActivityCard'], div.MuiListItem-root")
    private List<WebElement> activityCards;

    @FindBy(xpath = "//button[contains(., 'Tambah Aktivitas') or contains(., 'Tambah') or contains(., '+')]")
    private WebElement addActivityButton;

    @FindBy(css = "textarea[name='description'], textarea#description, textarea")
    private WebElement activityDescriptionInput;

    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Simpan') or contains(text(), 'Kirim')]")
    private WebElement saveActivityButton;

    @FindBy(xpath = "//button[contains(., 'Edit') or contains(@aria-label, 'edit')]")
    private WebElement editActivityButton;

    @FindBy(xpath = "//button[contains(., 'Hapus') or contains(@aria-label, 'delete')]")
    private WebElement deleteActivityButton;

    @FindBy(xpath = "//button[contains(., 'Konfirmasi') or contains(., 'Ya') or contains(., 'Hapus')]")
    private WebElement confirmDeleteButton;

    @FindBy(xpath = "//*[contains(text(), 'Belum ada') or contains(text(), 'Tidak ada data') or contains(@class, 'empty-state')]")
    private WebElement emptyStateNotice;

    @FindBy(css = "div[role='dialog'], div.modal, div.detail-modal")
    private WebElement detailModal;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message') or contains(@class, 'alert') or contains(@role, 'alert')]")
    private WebElement alertMessage;

    public ActivityHistoryPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToActivityPage() {
        driver.get(ACTIVITY_URL);
        try {
            waitForVisibility(filterButton);
        } catch (Exception ignored) {
        }
        logger.info("Navigated to Activity History Page: {}", ACTIVITY_URL);
    }

    public void filterByDateRange(String startDate, String endDate) {
        type(startDateInput, startDate);
        type(endDateInput, endDate);
        click(filterButton);
    }

    public int getActivityCount() {
        try {
            return activityCards.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isEmptyStateDisplayed() {
        try {
            return isDisplayed(emptyStateNotice) || getActivityCount() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickFirstActivityDetail() {
        try {
            if (!activityCards.isEmpty()) {
                click(activityCards.get(0));
            }
        } catch (Exception e) {
            logger.warn("Could not click activity card: {}", e.getMessage());
        }
    }

    public boolean isDetailModalDisplayed() {
        try {
            return isDisplayed(detailModal);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddActivity() {
        click(addActivityButton);
    }

    public void enterActivityDescription(String description) {
        type(activityDescriptionInput, description);
    }

    public void clickSaveActivity() {
        click(saveActivityButton);
    }

    public void addDailyActivity(String description) {
        clickAddActivity();
        enterActivityDescription(description);
        clickSaveActivity();
    }

    public void clickEditActivity() {
        click(editActivityButton);
    }

    public void clickDeleteActivity() {
        click(deleteActivityButton);
        try {
            waitForClickability(confirmDeleteButton);
            click(confirmDeleteButton);
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
}
