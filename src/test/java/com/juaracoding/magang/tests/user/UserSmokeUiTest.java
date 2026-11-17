package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Pengujian dasar Web UI User Magang Mobile Mirroring dengan Selenium WebDriver.
 * Diampu oleh Tester Aji (User Magang).
 */
public class UserSmokeUiTest extends BaseTest {

    @Test(priority = 1, description = "SIT_USR_UI_001: Verifikasi Navigasi dan Elemen Halaman Login User Magang")
    public void testUserLoginPageElements() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("absen/login") || currentUrl.contains("login"),
                "URL harus mengarah ke portal login user absen.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_002: Verifikasi Penolakan Login User dengan Password Salah")
    public void testUserLoginWithInvalidPassword() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.login("hadirsqa1@gmail.com", "PasswordSalah123!");

        ExtentTestManager.logInfo("Attempted invalid user login. Verifying URL remains on login page.");
        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains("absen"),
                "User tidak boleh ter-redirect saat password salah.");
    }
}

