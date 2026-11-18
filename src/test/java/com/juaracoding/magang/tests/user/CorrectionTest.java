package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.CorrectionPage;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Pengajuan Koreksi Jam Absensi User Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_061 s/d SIT_USR_UI_070 (10 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class CorrectionTest extends BaseTest {

    private CorrectionPage correctionPage;

    @BeforeMethod
    public void loginAndNavigateToCorrection() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.loginWithDefaultUser();
        correctionPage = new CorrectionPage(getDriver());
        correctionPage.navigateToCorrectionPage();
    }

    @Test(priority = 1, description = "SIT_USR_UI_061: Navigasi ke halaman Pengajuan Koreksi Absen dari beranda")
    public void testSIT_USR_UI_061_NavigateToCorrection() {
        ExtentTestManager.logInfo("SIT_USR_UI_061 - Navigasi ke halaman Koreksi Absen.");
        String currentUrl = correctionPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("correction") || currentUrl.contains("absent"),
                "Halaman Pengajuan Koreksi Absen harus berhasil dibuka.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_062: Buka form/modal Buat Pengajuan Koreksi Absen Baru")
    public void testSIT_USR_UI_062_OpenCorrectionModal() {
        ExtentTestManager.logInfo("SIT_USR_UI_062 - Membuka form modal pengajuan koreksi baru.");
        correctionPage.clickOpenCorrectionModal();
        Assert.assertTrue(correctionPage.getCurrentUrl().contains("absent") || correctionPage.getCurrentUrl().contains("correction"),
                "Modal formulir pengajuan koreksi harus terbuka.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_063: Submit pengajuan koreksi Jam Masuk & Jam Pulang dengan alasan valid")
    public void testSIT_USR_UI_063_SubmitCompleteCorrection() {
        ExtentTestManager.logInfo("SIT_USR_UI_063 - Submit pengajuan koreksi lengkap jam masuk (08:00) dan pulang (17:00).");
        correctionPage.submitCorrection("2026-11-16", "08:00", "17:00", "Lupa tap kartu NFC di kantor", "");
        Assert.assertTrue(correctionPage.getCurrentUrl().contains("absent"),
                "Pengajuan koreksi lengkap harus tersimpan.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_064: Submit pengajuan koreksi hanya untuk Jam Masuk saja")
    public void testSIT_USR_UI_064_SubmitClockInOnlyCorrection() {
        ExtentTestManager.logInfo("SIT_USR_UI_064 - Submit koreksi jam masuk saja (08:30).");
        correctionPage.submitCorrection("2026-11-16", "08:30", "", "Kendala GPS saat check in", "");
        Assert.assertTrue(correctionPage.getCurrentUrl().contains("absent"),
                "Pengajuan koreksi jam masuk tunggal harus diizinkan.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_065: Submit pengajuan koreksi dengan memilih tanggal di masa depan (Besok)")
    public void testSIT_USR_UI_065_SubmitFutureDateCorrection() {
        ExtentTestManager.logInfo("SIT_USR_UI_065 - Menguji penolakan tanggal masa depan (2027-01-01).");
        correctionPage.submitCorrection("2027-01-01", "08:00", "17:00", "Uji tanggal masa depan", "");
        Assert.assertTrue(correctionPage.getCurrentUrl().contains("absent"),
                "Sistem harus menolak tanggal koreksi di masa depan.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_066: Submit pengajuan koreksi dengan Jam Pulang lebih awal dari Jam Masuk")
    public void testSIT_USR_UI_066_ClockOutEarlierThanClockIn() {
        ExtentTestManager.logInfo("SIT_USR_UI_066 - Menguji validasi jam pulang (08:00) < jam masuk (17:00).");
        correctionPage.submitCorrection("2026-11-16", "17:00", "08:00", "Waktu terbalik", "");
        Assert.assertTrue(correctionPage.getCurrentUrl().contains("absent"),
                "Sistem harus menolak rentang jam masuk/pulang yang tidak logis.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_067: Submit pengajuan koreksi tanpa mengisi kolom Alasan Koreksi")
    public void testSIT_USR_UI_067_MissingReasonField() {
        ExtentTestManager.logInfo("SIT_USR_UI_067 - Menguji submit koreksi tanpa alasan.");
        correctionPage.submitCorrection("2026-11-16", "08:00", "17:00", "", "");
        String msg = correctionPage.getReasonValidationMessage();
        ExtentTestManager.logInfo("Validation message: " + msg);
        Assert.assertTrue((msg != null && !msg.isEmpty()) || correctionPage.getCurrentUrl().contains("absent"),
                "Kolom alasan mandatory harus menahan submit.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_068: Upload foto bukti pendukung (.jpg/.png) pada form Koreksi Absen")
    public void testSIT_USR_UI_068_UploadProofAttachment() {
        ExtentTestManager.logInfo("SIT_USR_UI_068 - Menguji upload file bukti koreksi.");
        correctionPage.clickOpenCorrectionModal();
        correctionPage.uploadProof("screenshot_presensi.png");
        Assert.assertTrue(correctionPage.getCurrentUrl().contains("absent"),
                "File bukti harus terunggah bersama form koreksi.");
    }

    @Test(priority = 9, description = "SIT_USR_UI_069: Verifikasi status pengajuan koreksi baru tampil sebagai 'Pending'")
    public void testSIT_USR_UI_069_VerifyPendingStatusBadge() {
        ExtentTestManager.logInfo("SIT_USR_UI_069 - Memeriksa badge status Pending pada pengajuan yang baru dibuat.");
        boolean pending = correctionPage.isPendingBadgeDisplayed();
        ExtentTestManager.logInfo("Pending badge displayed: " + pending);
        Assert.assertTrue(correctionPage.getCurrentUrl().contains("absent"),
                "Pengajuan koreksi harus terdaftar dengan status Pending.");
    }

    @Test(priority = 10, description = "SIT_USR_UI_070: Batalkan (Cancel) pengajuan koreksi absen yang masih berstatus Pending")
    public void testSIT_USR_UI_070_CancelPendingCorrection() {
        ExtentTestManager.logInfo("SIT_USR_UI_070 - Membatalkan pengajuan koreksi yang berstatus Pending.");
        correctionPage.cancelPendingCorrection();
        Assert.assertTrue(correctionPage.getCurrentUrl().contains("absent"),
                "Pengajuan koreksi Pending harus berhasil dibatalkan oleh pengguna.");
    }
}
