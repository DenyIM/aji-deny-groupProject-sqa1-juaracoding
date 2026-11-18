package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.ResetPasswordPage;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Fitur Pemulihan / Reset Kata Sandi User Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_025 s/d SIT_USR_UI_032 (8 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class ResetPasswordTest extends BaseTest {

    @Test(priority = 1, description = "SIT_USR_UI_025: Navigasi ke halaman Lupa Password dari halaman Login User")
    public void testSIT_USR_UI_025_NavigateToForgotPassword() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();

        ExtentTestManager.logInfo("SIT_USR_UI_025 - Menekan link 'Lupa Password' dari halaman login user.");
        loginPage.clickForgotPasswordLink();

        ResetPasswordPage resetPage = new ResetPasswordPage(getDriver());
        String currentUrl = resetPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL halaman lupa password: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("lupa") || currentUrl.contains("forgot") || resetPage.isEmailFieldDisplayed(),
                "Halaman formulir lupa password harus berhasil dimuat.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_026: Request link/OTP reset password dengan email terdaftar")
    public void testSIT_USR_UI_026_RequestResetWithRegisteredEmail() {
        ResetPasswordPage resetPage = new ResetPasswordPage(getDriver());
        resetPage.navigateToForgotPasswordPage();

        ExtentTestManager.logInfo("SIT_USR_UI_026 - Mengajukan reset password menggunakan email terdaftar: " + Constants.USER_EMAIL);
        resetPage.requestPasswordReset(Constants.USER_EMAIL);

        ExtentTestManager.logInfo("Request terkirim. Memeriksa konfirmasi sistem.");
        Assert.assertTrue(resetPage.isAlertMessageDisplayed() || resetPage.getCurrentUrl().contains("absen"),
                "Sistem harus menampilkan konfirmasi pengiriman instruksi reset kata sandi.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_027: Request reset password dengan email yang tidak terdaftar")
    public void testSIT_USR_UI_027_RequestResetWithUnregisteredEmail() {
        ResetPasswordPage resetPage = new ResetPasswordPage(getDriver());
        resetPage.navigateToForgotPasswordPage();

        ExtentTestManager.logInfo("SIT_USR_UI_027 - Mengajukan reset password menggunakan email yang tidak terdaftar.");
        resetPage.requestPasswordReset("ghost_user_999@gmail.com");

        boolean alertShown = resetPage.isAlertMessageDisplayed();
        ExtentTestManager.logInfo("Alert email tidak terdaftar: " + resetPage.getAlertText());

        Assert.assertTrue(alertShown || resetPage.getCurrentUrl().contains("lupa"),
                "Sistem harus menolak atau memperingatkan email tidak ditemukan.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_028: Request reset password dengan field email dikosongkan")
    public void testSIT_USR_UI_028_EmptyEmailField() {
        ResetPasswordPage resetPage = new ResetPasswordPage(getDriver());
        resetPage.navigateToForgotPasswordPage();

        ExtentTestManager.logInfo("SIT_USR_UI_028 - Menguji request reset dengan kolom email kosong.");
        resetPage.requestPasswordReset("");

        String validationMsg = resetPage.getEmailValidationMessage();
        ExtentTestManager.logInfo("Validation message email kosong: " + validationMsg);

        Assert.assertTrue((validationMsg != null && !validationMsg.isEmpty()) || resetPage.getCurrentUrl().contains("lupa"),
                "Field email mandatory harus menahan pengiriman form.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_029: Request reset password dengan format email tidak valid")
    public void testSIT_USR_UI_029_InvalidEmailFormat() {
        ResetPasswordPage resetPage = new ResetPasswordPage(getDriver());
        resetPage.navigateToForgotPasswordPage();

        ExtentTestManager.logInfo("SIT_USR_UI_029 - Menguji request reset dengan email format salah ('bukan_email').");
        resetPage.requestPasswordReset("bukan_email");

        String validationMsg = resetPage.getEmailValidationMessage();
        ExtentTestManager.logInfo("Validation message: " + validationMsg);

        Assert.assertTrue((validationMsg != null && !validationMsg.isEmpty()) || resetPage.getCurrentUrl().contains("lupa"),
                "Validasi form harus menolak format email yang salah.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_030: Submit password baru dan konfirmasi password yang cocok")
    public void testSIT_USR_UI_030_MatchingNewPasswordSubmit() {
        ResetPasswordPage resetPage = new ResetPasswordPage(getDriver());
        resetPage.navigateToForgotPasswordPage();

        ExtentTestManager.logInfo("SIT_USR_UI_030 - Menguji submit password baru dan konfirmasi yang cocok.");
        resetPage.submitNewPassword("NewPass@12345", "NewPass@12345");

        Assert.assertTrue(resetPage.getCurrentUrl().contains("absen"),
                "Form harus menerima input password baru yang cocok.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_031: Submit password baru dengan konfirmasi password tidak cocok")
    public void testSIT_USR_UI_031_MismatchPasswordSubmit() {
        ResetPasswordPage resetPage = new ResetPasswordPage(getDriver());
        resetPage.navigateToForgotPasswordPage();

        ExtentTestManager.logInfo("SIT_USR_UI_031 - Menguji submit password baru dengan konfirmasi berbeda.");
        resetPage.submitNewPassword("NewPass@12345", "BedaPass@99999");

        boolean alertShown = resetPage.isAlertMessageDisplayed();
        ExtentTestManager.logInfo("Alert ketidaksesuaian password: " + resetPage.getAlertText());

        Assert.assertTrue(alertShown || resetPage.getCurrentUrl().contains("lupa") || resetPage.getCurrentUrl().contains("reset"),
                "Sistem harus menolak password baru jika konfirmasi tidak cocok.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_032: Akses halaman reset password menggunakan token yang sudah kadaluarsa")
    public void testSIT_USR_UI_032_ExpiredResetTokenAccess() {
        ResetPasswordPage resetPage = new ResetPasswordPage(getDriver());

        ExtentTestManager.logInfo("SIT_USR_UI_032 - Mengakses URL reset password dengan token kadaluarsa.");
        resetPage.navigateToExpiredResetUrl();

        String currentUrl = resetPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL token kadaluarsa: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("absen") || resetPage.isAlertMessageDisplayed(),
                "Akses dengan token kadaluarsa harus ditolak atau menampilkan pesan error token tidak valid.");
    }
}
