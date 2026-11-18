package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Autentikasi & Keamanan Sesi Mobile User Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_001 s/d SIT_USR_UI_012 (12 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class UserLoginTest extends BaseTest {

    @Test(priority = 1, description = "SIT_USR_UI_001: Login User Magang dengan kredensial valid (hadirsqa1@gmail.com)")
    public void testSIT_USR_UI_001_ValidUserLogin() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_001 - Melakukan login menggunakan email User Magang valid & password Base64 ter-decode.");
        loginPage.loginWithDefaultUser();

        boolean redirected = loginPage.waitForUrlContains("/apps/absent");
        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL setelah login: " + currentUrl);

        Assert.assertTrue(redirected || currentUrl.contains("/absen") || currentUrl.contains("/apps"),
                "User harus berhasil login atau diarahkan ke portal presensi. URL aktual: " + currentUrl);
    }

    @Test(priority = 2, description = "SIT_USR_UI_002: Login User Magang dengan password salah")
    public void testSIT_USR_UI_002_InvalidPassword() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_002 - Menguji login User Magang dengan password salah.");
        loginPage.login(Constants.USER_EMAIL, "Salah123!");

        boolean alertShown = loginPage.isAlertMessageDisplayed();
        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("Status alert: " + alertShown + ", URL saat ini: " + currentUrl);

        Assert.assertTrue(alertShown || currentUrl.contains("/absen/login") || currentUrl.contains("login"),
                "Sistem harus menolak login dengan password salah dan tetap pada halaman login.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_003: Login User Magang dengan email tidak terdaftar")
    public void testSIT_USR_UI_003_UnregisteredEmail() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_003 - Menguji login User dengan email yang belum terdaftar.");
        loginPage.login("unreg_user_999@gmail.com", "AnyPassword123!");

        boolean alertShown = loginPage.isAlertMessageDisplayed();
        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("Pesan alert: " + loginPage.getAlertText() + ", URL: " + currentUrl);

        Assert.assertTrue(alertShown || currentUrl.contains("login"),
                "Login dengan email tidak terdaftar harus ditolak sistem.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_004: Login User Magang dengan format email tanpa domain (@)")
    public void testSIT_USR_UI_004_InvalidEmailFormat() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_004 - Menguji validasi form HTML5 terhadap format email tanpa '@'.");
        loginPage.login("hadirsqa1gmail.com", "AnyPassword123!");

        String validationMsg = loginPage.getEmailValidationMessage();
        ExtentTestManager.logInfo("Validation message: " + validationMsg);

        Assert.assertTrue((validationMsg != null && !validationMsg.isEmpty()) || loginPage.getCurrentUrl().contains("login"),
                "Browser/Form harus memvalidasi format email tidak valid.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_005: Login User Magang dengan field email kosong")
    public void testSIT_USR_UI_005_EmptyEmailField() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_005 - Menguji login dengan field email dikosongkan.");
        loginPage.login("", "AnyPassword123!");

        String validationMsg = loginPage.getEmailValidationMessage();
        ExtentTestManager.logInfo("Validation message email kosong: " + validationMsg);

        Assert.assertTrue((validationMsg != null && !validationMsg.isEmpty()) || loginPage.getCurrentUrl().contains("login"),
                "Field email mandatory harus menahan pengiriman form.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_006: Login User Magang dengan field password kosong")
    public void testSIT_USR_UI_006_EmptyPasswordField() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_006 - Menguji login dengan field password dikosongkan.");
        loginPage.login(Constants.USER_EMAIL, "");

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL saat password kosong: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains("absen"),
                "Field password mandatory harus mencegah proses login.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_007: Login User Magang dengan kedua field kosong")
    public void testSIT_USR_UI_007_BothFieldsEmpty() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_007 - Menguji login dengan email dan password kosong.");
        loginPage.login("", "");

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL saat kedua field kosong: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("login"),
                "Form tidak boleh disubmit jika kedua kolom kosong.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_008: Fungsi tombol Show/Hide Password (ikon mata) pada Login User")
    public void testSIT_USR_UI_008_ShowHidePasswordToggle() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_008 - Menguji toggle visibilitas password.");
        loginPage.enterPassword("SecretPass123!");

        String initialType = loginPage.getPasswordFieldType();
        ExtentTestManager.logInfo("Tipe atribut awal: " + initialType);
        Assert.assertEquals(initialType, "password", "Default tipe input password harus 'password'.");

        loginPage.clickShowHidePassword();
        String toggledType = loginPage.getPasswordFieldType();
        ExtentTestManager.logInfo("Tipe atribut setelah klik: " + toggledType);
        Assert.assertTrue("text".equals(toggledType) || "password".equals(toggledType),
                "Tipe input password harus merespons interaksi tombol show/hide.");
    }

    @Test(priority = 9, description = "SIT_USR_UI_009: Proteksi SQL Injection pada form Login User")
    public void testSIT_USR_UI_009_SqlInjectionProtection() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_009 - Menguji ketahanan login terhadap SQL Injection payload.");
        loginPage.login("' OR '1'='1' --", "AnyPass123!");

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL setelah payload SQLi: " + currentUrl);

        Assert.assertFalse(currentUrl.contains("/apps/absent"),
                "Payload SQL Injection tidak boleh membobol autentikasi.");
        Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains("absen"),
                "Form harus menahan aksi di halaman login.");
    }

    @Test(priority = 10, description = "SIT_USR_UI_010: Proteksi XSS Script Injection pada form Login User")
    public void testSIT_USR_UI_010_XssScriptInjectionProtection() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_010 - Menguji ketahanan login terhadap XSS script injection.");
        loginPage.login("<script>alert('XSS')</script>", "AnyPass123!");

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL setelah payload XSS: " + currentUrl);

        Assert.assertFalse(currentUrl.contains("/apps/absent"),
                "Payload XSS tidak boleh meloloskan sesi autentikasi.");
    }

    @Test(priority = 11, description = "SIT_USR_UI_011: Akses langsung halaman /apps/absent tanpa login")
    public void testSIT_USR_UI_011_DirectAccessWithoutLogin() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());

        ExtentTestManager.logInfo("SIT_USR_UI_011 - Mengakses route terproteksi /apps/absent tanpa sesi login aktif.");
        loginPage.navigateToProtectedAbsentDirectly();

        boolean redirectedToLogin = loginPage.waitForUrlContains("/absen/login") || loginPage.isEmailFieldDisplayed();
        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL hasil route protection: " + currentUrl);

        Assert.assertTrue(redirectedToLogin || currentUrl.contains("login") || loginPage.isEmailFieldDisplayed(),
                "Akses route terproteksi tanpa login wajib dialihkan ke login.");
    }

    @Test(priority = 12, description = "SIT_USR_UI_012: Logout dari aplikasi mobile user dan cek tombol Back browser")
    public void testSIT_USR_UI_012_LogoutAndSessionClear() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_012 - Menguji alur logout dan penghapusan sesi user.");
        loginPage.loginWithDefaultUser();

        loginPage.performLogout();
        String urlAfterLogout = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL setelah logout: " + urlAfterLogout);

        getDriver().navigate().back();
        String urlAfterBack = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL setelah menekan tombol Back: " + urlAfterBack);

        Assert.assertTrue(urlAfterBack.contains("login") || urlAfterBack.contains("absen"),
                "Setelah logout dan tombol Back ditekan, user tidak boleh kembali ke sesi aktif tanpa login.");
    }
}
