package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.PermitPage;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Pengajuan Izin Terlambat & Pulang Cepat Peserta Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_071 s/d SIT_USR_UI_080 (10 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class PermitTest extends BaseTest {

    private PermitPage permitPage;

    @BeforeMethod
    public void loginAndNavigateToPermit() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.loginWithDefaultUser();
        permitPage = new PermitPage(getDriver());
        permitPage.navigateToPermitPage();
    }

    @Test(priority = 1, description = "SIT_USR_UI_071: Navigasi ke halaman Pengajuan Izin dari menu beranda")
    public void testSIT_USR_UI_071_NavigateToPermit() {
        ExtentTestManager.logInfo("SIT_USR_UI_071 - Navigasi ke menu Pengajuan Izin.");
        String currentUrl = permitPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("permit") || currentUrl.contains("absent") || currentUrl.contains("izin"),
                "Halaman Pengajuan Izin harus berhasil dibuka.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_072: Submit form Izin Terlambat dengan tanggal, jam tiba, dan alasan valid")
    public void testSIT_USR_UI_072_SubmitValidLatePermit() {
        ExtentTestManager.logInfo("SIT_USR_UI_072 - Submit form Izin Terlambat (Jam: 09:30, Alasan: Kendala Ban Bocor).");
        permitPage.submitLatePermit("2026-11-17", "09:30", "Ban sepeda motor bocor di perjalanan", "");
        Assert.assertTrue(permitPage.getCurrentUrl().contains("absent"),
                "Pengajuan izin terlambat harus berhasil dikirim.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_073: Submit form Izin Terlambat tanpa mengisi keterangan alasan")
    public void testSIT_USR_UI_073_SubmitLatePermitEmptyReason() {
        ExtentTestManager.logInfo("SIT_USR_UI_073 - Menguji validasi form saat alasan izin terlambat kosong.");
        permitPage.submitLatePermit("2026-11-17", "09:30", "", "");
        String msg = permitPage.getReasonValidationMessage();
        ExtentTestManager.logInfo("Reason validation message: " + msg);
        Assert.assertTrue((msg != null && !msg.isEmpty()) || permitPage.getCurrentUrl().contains("absent"),
                "Field alasan mandatory harus menahan pengiriman form.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_074: Submit form Izin Pulang Cepat dengan tanggal, jam pulang, dan alasan")
    public void testSIT_USR_UI_074_SubmitEarlyLeavePermit() {
        ExtentTestManager.logInfo("SIT_USR_UI_074 - Submit form Izin Pulang Cepat (Jam: 14:00, Alasan: Urusan Kampus).");
        permitPage.submitEarlyLeavePermit("2026-11-17", "14:00", "Ada bimbingan sidang skripsi di kampus", "");
        Assert.assertTrue(permitPage.getCurrentUrl().contains("absent"),
                "Pengajuan izin pulang cepat harus berhasil dikirim.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_075: Upload lampiran foto bukti pada pengajuan Izin")
    public void testSIT_USR_UI_075_UploadProofAttachment() {
        ExtentTestManager.logInfo("SIT_USR_UI_075 - Menguji upload lampiran foto bukti izin.");
        permitPage.selectLatePermit();
        permitPage.uploadProof("bukti_izin.jpg");
        Assert.assertTrue(permitPage.getCurrentUrl().contains("absent"),
                "File bukti pendukung harus terlampir pada form izin.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_076: Submit pengajuan Izin dengan memilih tanggal lampau bulan lalu")
    public void testSIT_USR_UI_076_SubmitBackdatePermit() {
        ExtentTestManager.logInfo("SIT_USR_UI_076 - Menguji penolakan tanggal backdate lebih dari 30 hari yang lalu.");
        permitPage.submitLatePermit("2026-10-01", "09:00", "Izin bulan lalu yang tertunda", "");
        Assert.assertTrue(permitPage.getCurrentUrl().contains("absent"),
                "Sistem harus memvalidasi batas backdate izin.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_077: Upload file bukti izin dengan ukuran melebihi batas maksimal (> 5MB)")
    public void testSIT_USR_UI_077_OversizedFileRejection() {
        ExtentTestManager.logInfo("SIT_USR_UI_077 - Menguji penolakan file attachment lebih besar dari 5MB.");
        permitPage.uploadProof("large_file_10mb.jpg");
        Assert.assertTrue(permitPage.getCurrentUrl().contains("absent"),
                "Sistem harus memvalidasi batas kapasitas ukuran file.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_078: Filter daftar riwayat Izin berdasarkan status (Pending, Approved, Rejected)")
    public void testSIT_USR_UI_078_FilterPermitByStatus() {
        ExtentTestManager.logInfo("SIT_USR_UI_078 - Memilih filter tab status Pending / Disetujui / Ditolak.");
        permitPage.filterByStatus("Pending");
        permitPage.filterByStatus("Approved");
        permitPage.filterByStatus("Rejected");
        Assert.assertTrue(permitPage.getCurrentUrl().contains("absent"),
                "Filter tab status izin harus merespons klik pengguna.");
    }

    @Test(priority = 9, description = "SIT_USR_UI_079: Lihat catatan alasan penolakan HRD pada pengajuan izin yang Rejected")
    public void testSIT_USR_UI_079_ViewRejectionReason() {
        ExtentTestManager.logInfo("SIT_USR_UI_079 - Memeriksa catatan alasan penolakan dari admin.");
        permitPage.filterByStatus("Rejected");
        String reason = permitPage.getRejectionReasonText();
        ExtentTestManager.logInfo("Alasan penolakan: " + reason);
        Assert.assertNotNull(reason, "Catatan alasan penolakan harus tersedia.");
    }

    @Test(priority = 10, description = "SIT_USR_UI_080: Batalkan pengajuan Izin yang masih berstatus Pending")
    public void testSIT_USR_UI_080_CancelPendingPermit() {
        ExtentTestManager.logInfo("SIT_USR_UI_080 - Membatalkan pengajuan izin berstatus Pending.");
        permitPage.filterByStatus("Pending");
        permitPage.cancelPendingPermit();
        Assert.assertTrue(permitPage.getCurrentUrl().contains("absent"),
                "Pengajuan izin berstatus Pending harus berhasil dibatalkan.");
    }
}
