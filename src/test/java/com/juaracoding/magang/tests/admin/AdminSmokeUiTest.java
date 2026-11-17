package com.juaracoding.magang.tests.admin;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.admin.AdminLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Pengujian dasar Web UI Admin HRD dengan Selenium WebDriver.
 * Diampu oleh Tester Deny (Admin HRD).
 */
public class AdminSmokeUiTest extends BaseTest {

    @Test(priority = 1, description = "SIT_ADM_UI_001: Verifikasi Navigasi dan Elemen Halaman Login Admin HRD")
    public void testAdminLoginPageElements() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains("authentication"),
                "URL harus mengarah ke halaman login admin.");
    }

    @Test(priority = 2, description = "SIT_ADM_UI_002: Verifikasi Penolakan Login Admin dengan Password Salah")
    public void testAdminLoginWithInvalidPassword() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.login("admin@hadir.com", "PasswordSalah123!");

        ExtentTestManager.logInfo("Attempted invalid login. Verifying URL remains on login page.");
        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains("authentication"),
                "User tidak boleh ter-redirect ke dashboard saat password salah.");
    }
}

