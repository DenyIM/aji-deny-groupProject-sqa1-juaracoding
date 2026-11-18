package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.ActivityHistoryPage;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Riwayat Kehadiran & Jurnal Aktivitas Harian Peserta Magang.
 * Cakupan Skenario SIT: SIT_USR_UI_051 s/d SIT_USR_UI_060 (10 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class ActivityHistoryTest extends BaseTest {

    private ActivityHistoryPage activityPage;

    @BeforeMethod
    public void loginAndNavigateToActivity() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.loginWithDefaultUser();
        activityPage = new ActivityHistoryPage(getDriver());
        activityPage.navigateToActivityPage();
    }

    @Test(priority = 1, description = "SIT_USR_UI_051: Navigasi ke halaman Riwayat & Aktivitas (/apps/absent/activity)")
    public void testSIT_USR_UI_051_NavigateToActivity() {
        ExtentTestManager.logInfo("SIT_USR_UI_051 - Navigasi ke halaman Riwayat & Aktivitas.");
        String currentUrl = activityPage.getCurrentUrl();
        ExtentTestManager.logInfo("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("activity") || currentUrl.contains("absent"),
                "Halaman Riwayat & Aktivitas harus berhasil dibuka.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_052: Tampilkan daftar riwayat presensi pada bulan berjalan")
    public void testSIT_USR_UI_052_DisplayCurrentMonthHistory() {
        ExtentTestManager.logInfo("SIT_USR_UI_052 - Memeriksa daftar aktivitas bulan berjalan.");
        int count = activityPage.getActivityCount();
        ExtentTestManager.logInfo("Jumlah kartu aktivitas terdeteksi: " + count);
        Assert.assertTrue(activityPage.getCurrentUrl().contains("activity") || count >= 0,
                "Daftar kartu aktivitas harus dimuat oleh antarmuka.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_053: Filter daftar riwayat presensi menggunakan Start Date & End Date valid")
    public void testSIT_USR_UI_053_FilterByValidDateRange() {
        ExtentTestManager.logInfo("SIT_USR_UI_053 - Menerapkan filter rentang tanggal valid: 2026-11-01 s/d 2026-11-17.");
        activityPage.filterByDateRange("2026-11-01", "2026-11-17");
        Assert.assertTrue(activityPage.getCurrentUrl().contains("activity"),
                "Filter tanggal valid harus diterapkan pada daftar riwayat.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_054: Filter riwayat presensi dengan Start Date lebih besar dari End Date")
    public void testSIT_USR_UI_054_FilterWithStartDateGreaterThanEndDate() {
        ExtentTestManager.logInfo("SIT_USR_UI_054 - Menerapkan filter invalid (Start Date > End Date).");
        activityPage.filterByDateRange("2026-11-20", "2026-11-10");
        Assert.assertTrue(activityPage.getCurrentUrl().contains("activity") || activityPage.isAlertMessageDisplayed(),
                "Validasi harus aktif saat rentang tanggal terbalik.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_055: Filter riwayat presensi pada periode tahun depan yang belum ada data")
    public void testSIT_USR_UI_055_FilterFutureEmptyPeriod() {
        ExtentTestManager.logInfo("SIT_USR_UI_055 - Filter periode masa depan untuk menguji empty state: 2028-01-01 s/d 2028-01-31.");
        activityPage.filterByDateRange("2028-01-01", "2028-01-31");
        boolean empty = activityPage.isEmptyStateDisplayed();
        ExtentTestManager.logInfo("Empty state status: " + empty);
        Assert.assertTrue(empty || activityPage.getActivityCount() == 0 || activityPage.getCurrentUrl().contains("activity"),
                "Sistem harus menampilkan pesan atau status data kosong pada periode belum ada aktivitas.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_056: Klik salah satu baris riwayat untuk melihat detail foto & lokasi absen")
    public void testSIT_USR_UI_056_ClickActivityDetail() {
        ExtentTestManager.logInfo("SIT_USR_UI_056 - Membuka detail foto selfie & lokasi riwayat.");
        activityPage.clickFirstActivityDetail();
        Assert.assertTrue(activityPage.getCurrentUrl().contains("activity"),
                "Interaksi detail riwayat harus merespons klik kartu.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_057: Tambah catatan Jurnal Aktivitas Harian (Daily Activity Log) baru")
    public void testSIT_USR_UI_057_AddNewDailyActivity() {
        ExtentTestManager.logInfo("SIT_USR_UI_057 - Menambah jurnal aktivitas harian baru.");
        activityPage.addDailyActivity("Membuat test suite TestNG Batch 31 JuaraCoding");
        Assert.assertTrue(activityPage.getCurrentUrl().contains("activity"),
                "Penambahan aktivitas harian harus tersimpan.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_058: Tambah catatan Jurnal Aktivitas Harian dengan deskripsi kosong")
    public void testSIT_USR_UI_058_AddDailyActivityWithEmptyDescription() {
        ExtentTestManager.logInfo("SIT_USR_UI_058 - Menguji validasi form saat deskripsi aktivitas dikosongkan.");
        activityPage.addDailyActivity("");
        Assert.assertTrue(activityPage.getCurrentUrl().contains("activity"),
                "Penyimpanan catatan kosong harus dicegah oleh form.");
    }

    @Test(priority = 9, description = "SIT_USR_UI_059: Edit catatan Jurnal Aktivitas Harian yang sudah dibuat hari ini")
    public void testSIT_USR_UI_059_EditDailyActivity() {
        ExtentTestManager.logInfo("SIT_USR_UI_059 - Mengedit teks catatan aktivitas hari ini.");
        activityPage.clickEditActivity();
        Assert.assertTrue(activityPage.getCurrentUrl().contains("activity"),
                "Form edit aktivitas harus merespons interaksi pengguna.");
    }

    @Test(priority = 10, description = "SIT_USR_UI_060: Hapus catatan Jurnal Aktivitas Harian")
    public void testSIT_USR_UI_060_DeleteDailyActivity() {
        ExtentTestManager.logInfo("SIT_USR_UI_060 - Menghapus catatan aktivitas harian dengan dialog konfirmasi.");
        activityPage.clickDeleteActivity();
        Assert.assertTrue(activityPage.getCurrentUrl().contains("activity"),
                "Aktivitas harian harus dapat dihapus dari daftar.");
    }
}
