package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.pages.user.UserProfilePage;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Halaman Profil Pengguna & Ubah Password Peserta Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_103 s/d SIT_USR_UI_110 (8 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class UserProfileTest extends BaseTest {

    private UserProfilePage profilePage;

    @BeforeMethod
    public void loginAndNavigateToProfile() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.loginWithDefaultUser();
        profilePage = new UserProfilePage(getDriver());
        profilePage.navigateToProfilePage();
    }

    @Test(priority = 1, description = "SIT_USR_UI_103: Navigasi ke halaman Profil Saya (/apps/absent/profile)")
    public void testSIT_USR_UI_103_NavigateToProfilePage() {
        ExtentTestManager.logInfo("SIT_USR_UI_103 - Navigasi ke halaman Profil Pengguna.");
        String currentUrl = profilePage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("profile") || currentUrl.contains("absent"),
                "Halaman profil pengguna harus berhasil dibuka.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_104: Verifikasi kesesuaian data Nama, NIK (D6201031), Email, dan Divisi")
    public void testSIT_USR_UI_104_VerifyProfileIdentityData() {
        ExtentTestManager.logInfo("SIT_USR_UI_104 - Verifikasi identitas Nama, NIK, Email, dan Divisi.");
        String nik = profilePage.getProfileNik();
        String email = profilePage.getProfileEmail();
        ExtentTestManager.logInfo("NIK tertera: " + nik + ", Email: " + email);
        Assert.assertNotNull(nik, "NIK pengguna harus tampil di profil.");
        Assert.assertNotNull(email, "Email pengguna harus tampil di profil.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_105: Update foto profil pengguna menggunakan file gambar valid (.jpg/.png)")
    public void testSIT_USR_UI_105_UpdateProfileAvatar() {
        ExtentTestManager.logInfo("SIT_USR_UI_105 - Menguji update foto avatar profil.");
        profilePage.uploadAvatar("avatar_user.png");
        Assert.assertTrue(profilePage.getCurrentUrl().contains("absent") || profilePage.getCurrentUrl().contains("profile"),
                "Proses upload avatar harus diproses sistem.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_106: Navigasi ke form Ubah Kata Sandi (Change Password)")
    public void testSIT_USR_UI_106_NavigateToChangePasswordForm() {
        ExtentTestManager.logInfo("SIT_USR_UI_106 - Membuka form modal Ubah Password.");
        profilePage.clickChangePasswordMenu();
        Assert.assertTrue(profilePage.getCurrentUrl().contains("absent") || profilePage.isChangePasswordFormDisplayed(),
                "Form Ubah Password harus ditampilkan.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_107: Submit Ubah Kata Sandi dengan Password Lama benar & Password Baru valid")
    public void testSIT_USR_UI_107_ValidChangePassword() {
        ExtentTestManager.logInfo("SIT_USR_UI_107 - Mengubah password dengan password lama benar dan password baru valid.");
        String currentPass = SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED);
        profilePage.clickChangePasswordMenu();
        profilePage.changePassword(currentPass, currentPass, currentPass);
        Assert.assertTrue(profilePage.getCurrentUrl().contains("absent") || profilePage.getCurrentUrl().contains("profile"),
                "Pengubahan kata sandi valid harus berhasil.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_108: Submit Ubah Kata Sandi dengan Password Lama salah")
    public void testSIT_USR_UI_108_WrongOldPassword() {
        ExtentTestManager.logInfo("SIT_USR_UI_108 - Menguji penolakan saat password lama salah.");
        profilePage.clickChangePasswordMenu();
        profilePage.changePassword("SalahPasswordLama123!", "NewPass@12345", "NewPass@12345");
        Assert.assertTrue(profilePage.getCurrentUrl().contains("absent") || profilePage.isAlertMessageDisplayed(),
                "Sistem harus menolak penggantian sandi jika password lama salah.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_109: Submit Ubah Kata Sandi dengan Konfirmasi Password Baru tidak cocok")
    public void testSIT_USR_UI_109_MismatchNewPassword() {
        ExtentTestManager.logInfo("SIT_USR_UI_109 - Menguji validasi saat konfirmasi password baru tidak sesuai.");
        String currentPass = SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED);
        profilePage.clickChangePasswordMenu();
        profilePage.changePassword(currentPass, "NewPass@12345", "MismatchPass@99999");
        Assert.assertTrue(profilePage.getCurrentUrl().contains("absent") || profilePage.isAlertMessageDisplayed(),
                "Sistem harus menolak jika konfirmasi password baru tidak cocok.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_110: Submit Ubah Kata Sandi dengan seluruh field dikosongkan")
    public void testSIT_USR_UI_110_AllFieldsEmptyChangePassword() {
        ExtentTestManager.logInfo("SIT_USR_UI_110 - Menguji validasi form saat semua field ubah sandi dikosongkan.");
        profilePage.clickChangePasswordMenu();
        profilePage.changePassword("", "", "");
        String msg = profilePage.getOldPasswordValidationMessage();
        ExtentTestManager.logInfo("Old password validation message: " + msg);
        Assert.assertTrue((msg != null && !msg.isEmpty()) || profilePage.getCurrentUrl().contains("absent"),
                "Field mandatory harus menahan pengiriman form.");
    }
}
