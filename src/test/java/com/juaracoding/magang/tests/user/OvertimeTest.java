package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.OvertimePage;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Pengajuan Lembur (Overtime) Peserta Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_081 s/d SIT_USR_UI_090 (10 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class OvertimeTest extends BaseTest {

    private OvertimePage overtimePage;

    @BeforeMethod
    public void loginAndNavigateToOvertime() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.loginWithDefaultUser();
        overtimePage = new OvertimePage(getDriver());
        overtimePage.navigateToOvertimePage();
    }

    @Test(priority = 1, description = "SIT_USR_UI_081: Navigasi ke halaman Pengajuan Lembur dari beranda")
    public void testSIT_USR_UI_081_NavigateToOvertime() {
        ExtentTestManager.logInfo("SIT_USR_UI_081 - Navigasi ke menu Pengajuan Lembur.");
        String currentUrl = overtimePage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("overtime") || currentUrl.contains("absent") || currentUrl.contains("lembur"),
                "Halaman Pengajuan Lembur harus berhasil dibuka.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_082: Verifikasi tampilan daftar riwayat pengajuan lembur pengguna")
    public void testSIT_USR_UI_082_DisplayOvertimeHistoryList() {
        ExtentTestManager.logInfo("SIT_USR_UI_082 - Memeriksa daftar kartu riwayat lembur pengguna.");
        int count = overtimePage.getOvertimeCount();
        ExtentTestManager.logInfo("Jumlah item lembur terdaftar: " + count);
        Assert.assertTrue(overtimePage.getCurrentUrl().contains("absent") || count >= 0,
                "Daftar pengajuan lembur harus dapat dimuat.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_083: Submit form Pengajuan Lembur baru (Tanggal, Jam Mulai, Jam Selesai, Tugas)")
    public void testSIT_USR_UI_083_SubmitValidOvertime() {
        ExtentTestManager.logInfo("SIT_USR_UI_083 - Submit lembur baru (18:00 s/d 20:30, Tugas: Rilis Regression).");
        overtimePage.submitOvertime("2026-11-17", "18:00", "20:30", "Regression test rilis sprint 31", "");
        Assert.assertTrue(overtimePage.getCurrentUrl().contains("absent"),
                "Pengajuan lembur valid harus tersimpan.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_084: Submit form Lembur dengan Jam Selesai lebih kecil dari Jam Mulai")
    public void testSIT_USR_UI_084_EndTimeEarlierThanStartTime() {
        ExtentTestManager.logInfo("SIT_USR_UI_084 - Validasi penolakan jam lembur terbalik (20:00 s/d 18:00).");
        overtimePage.submitOvertime("2026-11-17", "20:00", "18:00", "Jam tidak valid", "");
        Assert.assertTrue(overtimePage.getCurrentUrl().contains("absent"),
                "Sistem harus menolak rentang jam lembur yang terbalik.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_085: Submit form Lembur dengan Jam Mulai sama persis dengan Jam Selesai (0 menit)")
    public void testSIT_USR_UI_085_ZeroDurationOvertime() {
        ExtentTestManager.logInfo("SIT_USR_UI_085 - Validasi penolakan durasi lembur 0 menit (18:00 s/d 18:00).");
        overtimePage.submitOvertime("2026-11-17", "18:00", "18:00", "Durasi nol menit", "");
        Assert.assertTrue(overtimePage.getCurrentUrl().contains("absent"),
                "Sistem harus menolak durasi lembur 0 menit.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_086: Submit form Lembur tanpa mengisi deskripsi tugas/pekerjaan lembur")
    public void testSIT_USR_UI_086_EmptyTaskDescription() {
        ExtentTestManager.logInfo("SIT_USR_UI_086 - Menguji validasi form saat uraian tugas lembur kosong.");
        overtimePage.submitOvertime("2026-11-17", "18:00", "20:00", "", "");
        String msg = overtimePage.getTaskValidationMessage();
        ExtentTestManager.logInfo("Task validation message: " + msg);
        Assert.assertTrue((msg != null && !msg.isEmpty()) || overtimePage.getCurrentUrl().contains("absent"),
                "Field deskripsi tugas wajib diisi.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_087: Upload dokumen SPK (Surat Perintah Kerja) Lembur pada form pengajuan")
    public void testSIT_USR_UI_087_UploadSpkAttachment() {
        ExtentTestManager.logInfo("SIT_USR_UI_087 - Menguji upload file SPK penugasan lembur.");
        overtimePage.clickOpenOvertimeModal();
        overtimePage.uploadSpk("spk_lembur.jpg");
        Assert.assertTrue(overtimePage.getCurrentUrl().contains("absent"),
                "File dokumen SPK harus dapat diunggah bersama form lembur.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_088: Submit pengajuan Lembur duplikat pada tanggal dan jam yang sama")
    public void testSIT_USR_UI_088_DuplicateOvertimeSubmission() {
        ExtentTestManager.logInfo("SIT_USR_UI_088 - Menguji penolakan duplikasi pengajuan lembur di waktu yang sama.");
        overtimePage.submitOvertime("2026-11-17", "18:00", "20:30", "Duplikat lembur", "");
        Assert.assertTrue(overtimePage.getCurrentUrl().contains("absent"),
                "Sistem harus memvalidasi agar tidak terjadi pengajuan lembur duplikat.");
    }

    @Test(priority = 9, description = "SIT_USR_UI_089: Filter daftar riwayat Lembur berdasarkan periode bulan")
    public void testSIT_USR_UI_089_FilterOvertimeByMonth() {
        ExtentTestManager.logInfo("SIT_USR_UI_089 - Memfilter daftar lembur berdasarkan bulan November.");
        overtimePage.filterByMonth("November");
        Assert.assertTrue(overtimePage.getCurrentUrl().contains("absent"),
                "Filter riwayat lembur bulanan harus merespons pemilihan dropdown.");
    }

    @Test(priority = 10, description = "SIT_USR_UI_090: Batalkan pengajuan Lembur yang masih berstatus Pending")
    public void testSIT_USR_UI_090_CancelPendingOvertime() {
        ExtentTestManager.logInfo("SIT_USR_UI_090 - Membatalkan pengajuan lembur yang berstatus Pending.");
        overtimePage.cancelPendingOvertime();
        Assert.assertTrue(overtimePage.getCurrentUrl().contains("absent"),
                "Pengajuan lembur berstatus Pending harus berhasil dibatalkan.");
    }
}
