package com.juaracoding.magang.tests.admin;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.admin.AdminLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Kelas Pengujian Otomasi Web UI untuk Fitur Autentikasi & Keamanan Sesi Admin HRD.
 * Cakupan Skenario SIT: SIT_ADM_UI_001 s/d SIT_ADM_UI_012 (12 Test Cases).
 * Diampu oleh: Tester Deny (Admin HRD) - Branch: feature/deny-admin-login.
 */
public class AdminLoginTest extends BaseTest {

    @Test(priority = 1, description = "SIT_ADM_UI_001: Login Admin HRD dengan kredensial valid (admin@hadir.com)")
    public void testSIT_ADM_UI_001_ValidAdminLogin() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_001 - Melakukan login menggunakan email Admin valid & password Base64 ter-decode.");
        loginPage.loginWithDefaultAdmin();

        boolean redirected = loginPage.waitForUrlContains("/dashboards/pending");
        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL setelah login: " + currentUrl);

        Assert.assertTrue(redirected || currentUrl.contains("/dashboards/"),
                "Admin harus berhasil diarahkan ke halaman /dashboards/pending. URL aktual: " + currentUrl);
    }

    @Test(priority = 2, description = "SIT_ADM_UI_002: Login Admin HRD dengan password salah")
    public void testSIT_ADM_UI_002_InvalidPassword() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_002 - Menguji login Admin dengan password salah.");
        loginPage.login(Constants.ADMIN_EMAIL, "WrongPassword123!");

        boolean alertShown = loginPage.isAlertMessageDisplayed();
        String alertText = loginPage.getAlertText();
        ExtentTestManager.logInfo("Pesan alert yang tampil: " + alertText);

        Assert.assertTrue(alertShown || loginPage.getCurrentUrl().contains("/authentication/login"),
                "Sistem harus menolak login dan tetap berada di halaman login.");
        Assert.assertFalse(loginPage.getCurrentUrl().contains("/dashboards/pending"),
                "Tidak boleh redirect ke dashboard saat password salah.");
    }

    @Test(priority = 3, description = "SIT_ADM_UI_003: Login Admin HRD dengan email tidak terdaftar")
    public void testSIT_ADM_UI_003_UnregisteredEmail() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_003 - Menguji login Admin dengan email tidak terdaftar.");
        loginPage.login("unknown_admin_999@hadir.com", "AnyPass123!");

        boolean alertShown = loginPage.isAlertMessageDisplayed();
        String alertText = loginPage.getAlertText();
        ExtentTestManager.logInfo("Pesan alert yang tampil: " + alertText);

        Assert.assertTrue(alertShown || loginPage.getCurrentUrl().contains("/authentication/login"),
                "Sistem harus menampilkan pesan error untuk email tidak terdaftar.");
        Assert.assertFalse(loginPage.getCurrentUrl().contains("/dashboards/pending"),
                "Tidak boleh masuk ke dashboard dengan email tidak terdaftar.");
    }

    @Test(priority = 4, description = "SIT_ADM_UI_004: Login Admin HRD dengan format email tanpa karakter @")
    public void testSIT_ADM_UI_004_InvalidEmailFormatWithoutAt() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_004 - Menguji validasi HTML5 untuk format email tanpa '@'.");
        loginPage.login("adminhadir.com", "AnyPass123!");

        String validationMsg = loginPage.getEmailValidationMessage();
        ExtentTestManager.logInfo("HTML5 Validation Message: " + validationMsg);

        Assert.assertTrue((validationMsg != null && !validationMsg.isEmpty())
                        || loginPage.getCurrentUrl().contains("/authentication/login"),
                "Browser/Form harus memvalidasi format email tanpa '@' dan menahan pengiriman form.");
    }

    @Test(priority = 5, description = "SIT_ADM_UI_005: Login Admin HRD dengan field email dikosongkan")
    public void testSIT_ADM_UI_005_EmptyEmailField() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_005 - Menguji login dengan mengosongkan field email.");
        String decodedPassword = SecurityUtil.decode(Constants.ADMIN_PASSWORD_ENCODED);
        loginPage.login("", decodedPassword);

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL setelah submit email kosong: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("/authentication/login"),
                "User harus tetap di halaman login ketika email kosong.");
    }

    @Test(priority = 6, description = "SIT_ADM_UI_006: Login Admin HRD dengan field password dikosongkan")
    public void testSIT_ADM_UI_006_EmptyPasswordField() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_006 - Menguji login dengan mengosongkan field password.");
        loginPage.login(Constants.ADMIN_EMAIL, "");

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL setelah submit password kosong: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("/authentication/login"),
                "User harus tetap di halaman login ketika password kosong.");
    }

    @Test(priority = 7, description = "SIT_ADM_UI_007: Login Admin HRD dengan seluruh field kosong")
    public void testSIT_ADM_UI_007_BothFieldsEmpty() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_007 - Menguji klik tombol Masuk ketika email dan password kosong.");
        loginPage.login("", "");

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL setelah submit form kosong: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("/authentication/login"),
                "Form kosong tidak boleh mengizinkan akses ke dashboard.");
    }

    @Test(priority = 8, description = "SIT_ADM_UI_008: Verifikasi fungsi tombol ikon mata (Show/Hide Password)")
    public void testSIT_ADM_UI_008_ShowHidePasswordToggle() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_008 - Menguji toggle ikon mata pada field password.");
        loginPage.enterPassword("SecretTest123!");

        String initialType = loginPage.getPasswordFieldType();
        ExtentTestManager.logInfo("Tipe atribut awal field password: " + initialType);
        Assert.assertEquals(initialType, "password", "Default tipe input password harus 'password' (tersembunyi).");

        loginPage.clickShowHidePassword();
        String toggledType = loginPage.getPasswordFieldType();
        ExtentTestManager.logInfo("Tipe atribut setelah klik ikon mata pertama: " + toggledType);
        Assert.assertEquals(toggledType, "text", "Setelah klik ikon mata, tipe input harus berubah menjadi 'text'.");

        loginPage.clickShowHidePassword();
        String revertedType = loginPage.getPasswordFieldType();
        ExtentTestManager.logInfo("Tipe atribut setelah klik ikon mata kedua: " + revertedType);
        Assert.assertEquals(revertedType, "password", "Setelah klik kembali, tipe input harus kembali ke 'password'.");
    }

    @Test(priority = 9, description = "SIT_ADM_UI_009: Uji keamanan SQL Injection pada field email Login Admin")
    public void testSIT_ADM_UI_009_SqlInjectionOnEmail() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_009 - Menguji ketahanan form login terhadap SQL Injection payload.");
        loginPage.login("' OR '1'='1' --", "Anything123!");

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL setelah percobaan SQL Injection: " + currentUrl);

        Assert.assertFalse(currentUrl.contains("/dashboards/pending"),
                "Payload SQL Injection tidak boleh membobol autentikasi Admin.");
        Assert.assertTrue(currentUrl.contains("/authentication/login"),
                "Halaman harus tetap tertahan di /authentication/login.");
    }

    @Test(priority = 10, description = "SIT_ADM_UI_010: Uji keamanan XSS Injection pada field email Login Admin")
    public void testSIT_ADM_UI_010_XssInjectionOnEmail() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_010 - Menguji ketahanan form login terhadap XSS script payload.");
        loginPage.login("<script>alert('XSS')</script>", "Anything123!");

        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL setelah percobaan XSS Injection: " + currentUrl);

        Assert.assertFalse(currentUrl.contains("/dashboards/pending"),
                "Payload XSS tidak boleh mengizinkan login.");
        Assert.assertTrue(currentUrl.contains("/authentication/login"),
                "Halaman harus tetap aman berada di /authentication/login.");
    }

    @Test(priority = 11, description = "SIT_ADM_UI_011: Akses langsung URL /dashboards/pending tanpa login")
    public void testSIT_ADM_UI_011_UnauthenticatedDirectDashboardAccess() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());

        ExtentTestManager.logInfo("SIT_ADM_UI_011 - Mengakses langsung URL terproteksi /dashboards/pending tanpa sesi login.");
        loginPage.navigateToProtectedDashboardDirectly();

        boolean redirectedToLogin = loginPage.waitForUrlContains("/authentication/login");
        String currentUrl = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL hasil proteksi route: " + currentUrl);

        Assert.assertTrue(redirectedToLogin || loginPage.isEmailFieldDisplayed(),
                "Akses tanpa sesi login wajib diarahkan kembali ke halaman login Admin.");
    }

    @Test(priority = 12, description = "SIT_ADM_UI_012: Logout Admin HRD dan verifikasi penghapusan session")
    public void testSIT_ADM_UI_012_AdminLogoutAndSessionClear() {
        AdminLoginPage loginPage = new AdminLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_ADM_UI_012 - Login sebagai Admin lalu melakukan proses Logout.");
        loginPage.loginWithDefaultAdmin();
        boolean loggedIn = loginPage.waitForUrlContains("/dashboards/pending");
        Assert.assertTrue(loggedIn, "Prasyarat login Admin harus berhasil.");

        loginPage.performLogout();
        boolean backToLogin = loginPage.waitForUrlContains("/authentication/login");
        String urlAfterLogout = loginPage.getCurrentUrl();
        ExtentTestManager.logInfo("URL setelah Logout: " + urlAfterLogout);

        Assert.assertTrue(backToLogin || urlAfterLogout.contains("/login"),
                "Setelah logout, Admin harus kembali ke halaman /authentication/login.");
    }
}

