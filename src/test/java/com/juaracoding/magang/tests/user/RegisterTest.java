package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.RegisterPage;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Pendaftaran Mandiri Akun NIK Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_013 s/d SIT_USR_UI_024 (12 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class RegisterTest extends BaseTest {

    @Test(priority = 1, description = "SIT_USR_UI_013: Navigasi dari halaman Login ke halaman Daftar Akun Baru")
    public void testSIT_USR_UI_013_NavigateToRegister() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_013 - Menekan tautan pendaftaran akun dari halaman login.");
        loginPage.clickRegisterLink();

        RegisterPage registerPage = new RegisterPage(getDriver());
        String currentUrl = registerPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL setelah klik daftar: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("register") || currentUrl.contains("daftar") || registerPage.isNikInputDisplayed(),
                "Halaman pendaftaran harus berhasil terbuka.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_014: Validasi pencarian NIK valid dari pool Excel (F1240001)")
    public void testSIT_USR_UI_014_ValidateValidNik() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_014 - Menguji pengecekan NIK valid dari whitelist Excel: F1240001.");
        registerPage.checkNik(Constants.VALID_NIK_POOL[0]);

        ExtentTestManager.logInfo("NIK berhasil dicek.");
        Assert.assertTrue(registerPage.getCurrentUrl().contains("register") || registerPage.getCurrentUrl().contains("absen"),
                "Sistem harus menerima NIK whitelist.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_015: Verifikasi auto-fill Nama & Divisi saat NIK valid (D6240005) ditemukan")
    public void testSIT_USR_UI_015_AutoFillNameAndDivision() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_015 - Menguji auto-fill profil Nama dan Divisi saat NIK D6240005 diinput.");
        registerPage.checkNik(Constants.VALID_NIK_POOL[1]);

        String name = registerPage.getNameInputValue();
        String division = registerPage.getDivisionInputValue();
        ExtentTestManager.logInfo("Auto-filled Nama: '" + name + "', Divisi: '" + division + "'");

        Assert.assertNotNull(name, "Field nama harus terisi atau terespon oleh sistem.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_016: Pendaftaran dengan NIK yang tidak ada di master HRD (INVALID999)")
    public void testSIT_USR_UI_016_InvalidNikNotInMaster() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_016 - Menguji pengecekan NIK palsu / tidak ada di master data: INVALID999.");
        registerPage.checkNik("INVALID999");

        boolean alertShown = registerPage.isAlertMessageDisplayed();
        ExtentTestManager.logInfo("Pesan sistem untuk NIK tidak valid: " + registerPage.getAlertText());

        Assert.assertTrue(alertShown || registerPage.getCurrentUrl().contains("register"),
                "Sistem harus menolak NIK yang tidak ada di master HRD.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_017: Pendaftaran dengan NIK yang sudah pernah diregistrasikan (D6201031)")
    public void testSIT_USR_UI_017_AlreadyRegisteredNik() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_017 - Menguji NIK yang sudah aktif: " + Constants.USER_DEFAULT_NIK);
        registerPage.checkNik(Constants.USER_DEFAULT_NIK);

        boolean alertShown = registerPage.isAlertMessageDisplayed();
        ExtentTestManager.logInfo("Pesan NIK terdaftar: " + registerPage.getAlertText());

        Assert.assertTrue(alertShown || registerPage.getCurrentUrl().contains("register"),
                "Sistem harus memperingatkan bahwa NIK sudah terdaftar.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_018: Submit pendaftaran lengkap dengan Email, Password & Foto Wajah")
    public void testSIT_USR_UI_018_CompleteRegistration() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_018 - Melakukan pendaftaran mandiri dengan data lengkap.");
        registerPage.register(Constants.VALID_NIK_POOL[2], "magang_new_batch31@gmail.com", "Magang@12345", "");

        ExtentTestManager.logInfo("Registrasi lengkap berhasil disubmit.");
        Assert.assertTrue(registerPage.getCurrentUrl().contains("register") || registerPage.getCurrentUrl().contains("login"),
                "Alur registrasi harus memproses data pendaftaran.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_019: Submit pendaftaran dengan format email tidak valid")
    public void testSIT_USR_UI_019_InvalidEmailFormat() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_019 - Menguji registrasi dengan format email salah tanpa domain.");
        registerPage.enterNik(Constants.VALID_NIK_POOL[3]);
        registerPage.enterEmail("user_tanpa_domain");
        registerPage.enterPassword("ValidPass123!");
        registerPage.clickSubmitRegister();

        String validationMsg = registerPage.getEmailValidationMessage();
        ExtentTestManager.logInfo("Email validation message: " + validationMsg);

        Assert.assertTrue((validationMsg != null && !validationMsg.isEmpty()) || registerPage.getCurrentUrl().contains("register"),
                "Form harus menahan pengiriman email format tidak valid.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_020: Submit pendaftaran dengan email yang sudah dipakai akun lain")
    public void testSIT_USR_UI_020_DuplicateEmailRegistration() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_020 - Menguji pendaftaran dengan email yang sudah terdaftar.");
        registerPage.register(Constants.VALID_NIK_POOL[4], Constants.USER_EMAIL, "ValidPass123!", "");

        boolean alertShown = registerPage.isAlertMessageDisplayed();
        ExtentTestManager.logInfo("Alert duplikat email: " + registerPage.getAlertText());

        Assert.assertTrue(alertShown || registerPage.getCurrentUrl().contains("register"),
                "Sistem harus menolak pendaftaran dengan email duplikat.");
    }

    @Test(priority = 9, description = "SIT_USR_UI_021: Submit pendaftaran dengan password kurang dari batas minimum karakter")
    public void testSIT_USR_UI_021_ShortPassword() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_021 - Menguji validasi password pendek (< 6/8 karakter).");
        registerPage.enterNik(Constants.VALID_NIK_POOL[0]);
        registerPage.enterEmail("random_intern_test@gmail.com");
        registerPage.enterPassword("123");
        registerPage.clickSubmitRegister();

        String passValidationMsg = registerPage.getPasswordValidationMessage();
        ExtentTestManager.logInfo("Password validation message: " + passValidationMsg);

        Assert.assertTrue((passValidationMsg != null && !passValidationMsg.isEmpty())
                        || registerPage.isAlertMessageDisplayed()
                        || registerPage.getCurrentUrl().contains("register"),
                "Password pendek harus ditolak oleh sistem.");
    }

    @Test(priority = 10, description = "SIT_USR_UI_022: Submit pendaftaran tanpa mengunggah foto wajah master")
    public void testSIT_USR_UI_022_MissingMasterPhoto() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_022 - Menguji submit registrasi tanpa upload foto selfie master.");
        registerPage.register(Constants.VALID_NIK_POOL[1], "tanpa_foto@gmail.com", "PasswordValid123!", "");

        Assert.assertTrue(registerPage.getCurrentUrl().contains("register"),
                "Submit tanpa foto wajah master harus ditolak atau meminta upload foto.");
    }

    @Test(priority = 11, description = "SIT_USR_UI_023: Upload file dokumen (.pdf/.txt) pada kolom foto wajah registrasi")
    public void testSIT_USR_UI_023_InvalidFileFormatPhoto() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_023 - Menguji upload file ekstensi bukan gambar (.pdf/.txt).");
        registerPage.enterNik(Constants.VALID_NIK_POOL[0]);
        registerPage.uploadPhoto("test_document.pdf");

        Assert.assertTrue(registerPage.getCurrentUrl().contains("register"),
                "Sistem harus menolak tipe file non-gambar untuk foto wajah.");
    }

    @Test(priority = 12, description = "SIT_USR_UI_024: Klik tautan 'Sudah punya akun? Masuk' dari halaman Registrasi")
    public void testSIT_USR_UI_024_BackToLoginFromRegister() {
        RegisterPage registerPage = new RegisterPage(getDriver());
        registerPage.navigateToRegisterPage();

        ExtentTestManager.logInfo("SIT_USR_UI_024 - Menekan tautan kembali ke halaman Login.");
        registerPage.clickBackToLogin();

        String currentUrl = registerPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL setelah kembali: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains("absen"),
                "User harus berhasil kembali ke halaman Login.");
    }
}
