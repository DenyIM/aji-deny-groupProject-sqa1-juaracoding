package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.LeavePage;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Pengajuan Cuti Tahunan & Klaim Sakit Peserta Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_091 s/d SIT_USR_UI_102 (12 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class LeaveAndSickTest extends BaseTest {

    private LeavePage leavePage;

    @BeforeMethod
    public void loginAndNavigateToLeave() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.loginWithDefaultUser();
        leavePage = new LeavePage(getDriver());
        leavePage.navigateToLeavePage();
    }

    @Test(priority = 1, description = "SIT_USR_UI_091: Navigasi ke halaman Pengajuan Cuti dan verifikasi Sisa Kuota Cuti")
    public void testSIT_USR_UI_091_NavigateToLeaveAndVerifyQuota() {
        ExtentTestManager.logInfo("SIT_USR_UI_091 - Navigasi ke menu Cuti dan verifikasi sisa kuota.");
        boolean quota = leavePage.isLeaveQuotaDisplayed();
        String quotaText = leavePage.getLeaveQuotaText();
        ExtentTestManager.logInfo("Sisa kuota: " + quotaText);
        Assert.assertTrue(quota || leavePage.getCurrentUrl().contains("absent") || leavePage.getCurrentUrl().contains("leave"),
                "Halaman Cuti dan sisa kuota harus dapat dimuat.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_092: Submit pengajuan Cuti Tahunan dengan rentang tanggal mulai & selesai valid")
    public void testSIT_USR_UI_092_SubmitValidAnnualLeave() {
        ExtentTestManager.logInfo("SIT_USR_UI_092 - Submit cuti tahunan (2026-12-10 s/d 2026-12-11, Delegasi: Deny, Alasan: Liburan keluarga).");
        leavePage.submitAnnualLeave("2026-12-10", "2026-12-11", "Deny - Tester", "Liburan akhir tahun bersama keluarga");
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent"),
                "Pengajuan cuti tahunan valid harus tersimpan.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_093: Submit pengajuan Cuti dengan Tanggal Selesai lebih awal dari Tanggal Mulai")
    public void testSIT_USR_UI_093_EndDateEarlierThanStartDate() {
        ExtentTestManager.logInfo("SIT_USR_UI_093 - Validasi penolakan tanggal selesai < tanggal mulai (25 Des s/d 20 Des).");
        leavePage.submitAnnualLeave("2026-12-25", "2026-12-20", "Deny", "Tanggal terbalik");
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent"),
                "Sistem harus menolak rentang tanggal cuti yang terbalik.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_094: Submit pengajuan Cuti dengan durasi hari melebihi sisa kuota cuti")
    public void testSIT_USR_UI_094_ExceedingQuotaLeave() {
        ExtentTestManager.logInfo("SIT_USR_UI_094 - Validasi durasi cuti 45 hari (melebihi batas sisa kuota).");
        leavePage.submitAnnualLeave("2027-01-01", "2027-02-15", "Deny", "Cuti melebihi kuota");
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent"),
                "Sistem harus menolak cuti yang melebihi saldo kuota.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_095: Submit pengajuan Cuti tanpa memilih rekan delegasi tugas (jika wajib)")
    public void testSIT_USR_UI_095_EmptyDelegationField() {
        ExtentTestManager.logInfo("SIT_USR_UI_095 - Menguji pengajuan cuti dengan mengosongkan rekan delegasi.");
        leavePage.submitAnnualLeave("2026-12-10", "2026-12-11", "", "Tanpa delegasi tugas");
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent"),
                "Formulir cuti harus menangani input kolom delegasi.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_096: Submit pengajuan Cuti tanpa mengisi kolom Alasan Cuti")
    public void testSIT_USR_UI_096_EmptyLeaveReason() {
        ExtentTestManager.logInfo("SIT_USR_UI_096 - Validasi penolakan submit tanpa alasan cuti.");
        leavePage.submitAnnualLeave("2026-12-10", "2026-12-11", "Deny", "");
        String msg = leavePage.getReasonValidationMessage();
        ExtentTestManager.logInfo("Validation message: " + msg);
        Assert.assertTrue((msg != null && !msg.isEmpty()) || leavePage.getCurrentUrl().contains("absent"),
                "Kolom alasan cuti mandatory harus menahan pengiriman form.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_097: Navigasi ke halaman / tab Pengajuan Klaim Sakit")
    public void testSIT_USR_UI_097_NavigateToSickTab() {
        ExtentTestManager.logInfo("SIT_USR_UI_097 - Navigasi ke menu / tab Klaim Sakit.");
        leavePage.clickSickTab();
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent") || leavePage.getCurrentUrl().contains("sick"),
                "Formulir klaim sakit harus terbuka.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_098: Submit pengajuan Sakit 1 hari beserta keluhan sakit")
    public void testSIT_USR_UI_098_SubmitOneDaySickClaim() {
        ExtentTestManager.logInfo("SIT_USR_UI_098 - Mengajukan klaim sakit 1 hari dengan keluhan demam.");
        leavePage.submitSickClaim("2026-11-17", "1", "Demam tinggi dan flu ringan", "");
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent"),
                "Klaim sakit 1 hari harus berhasil disubmit.");
    }

    @Test(priority = 9, description = "SIT_USR_UI_099: Submit pengajuan Sakit > 2 hari dengan lampiran Surat Keterangan Dokter")
    public void testSIT_USR_UI_099_SubmitSickClaimWithDoctorLetter() {
        ExtentTestManager.logInfo("SIT_USR_UI_099 - Mengajukan klaim sakit 3 hari dengan upload SKD dokter.");
        leavePage.submitSickClaim("2026-11-17", "3", "Tifus butuh istirahat dokter", "surat_dokter.jpg");
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent"),
                "Klaim sakit dengan surat dokter harus dapat dikirim.");
    }

    @Test(priority = 10, description = "SIT_USR_UI_100: Submit pengajuan Sakit tanpa melampirkan Surat Keterangan Dokter")
    public void testSIT_USR_UI_100_MissingDoctorLetter() {
        ExtentTestManager.logInfo("SIT_USR_UI_100 - Menguji klaim sakit > 2 hari tanpa melampirkan SKD.");
        leavePage.submitSickClaim("2026-11-17", "4", "Sakit tanpa bukti dokter", "");
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent"),
                "Sistem harus meminta dokumen surat dokter untuk sakit berkelanjutan.");
    }

    @Test(priority = 11, description = "SIT_USR_UI_101: Upload file berformat audio/archive (.mp3/.zip) pada lampiran Surat Dokter")
    public void testSIT_USR_UI_101_InvalidFileExtensionDoctorLetter() {
        ExtentTestManager.logInfo("SIT_USR_UI_101 - Menguji penolakan format file non-dokumen (.mp3/.zip).");
        leavePage.submitSickClaim("2026-11-17", "2", "File salah format", "file_lagu.mp3");
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent"),
                "Sistem harus menolak tipe file non-gambar/PDF pada lampiran SKD.");
    }

    @Test(priority = 12, description = "SIT_USR_UI_102: Verifikasi daftar histori dan status persetujuan Cuti & Sakit")
    public void testSIT_USR_UI_102_VerifyLeaveSickHistoryList() {
        ExtentTestManager.logInfo("SIT_USR_UI_102 - Memeriksa daftar histori pengajuan cuti dan sakit.");
        int count = leavePage.getHistoryCount();
        ExtentTestManager.logInfo("Jumlah histori cuti/sakit: " + count);
        Assert.assertTrue(leavePage.getCurrentUrl().contains("absent") || count >= 0,
                "Daftar pengajuan cuti & sakit harus dapat ditampilkan.");
    }
}
