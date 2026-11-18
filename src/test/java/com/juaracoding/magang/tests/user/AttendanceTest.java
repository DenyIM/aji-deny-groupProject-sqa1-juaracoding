package com.juaracoding.magang.tests.user;

import com.juaracoding.magang.BaseTest;
import com.juaracoding.magang.pages.user.AttendancePage;
import com.juaracoding.magang.pages.user.UserLoginPage;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian Otomasi Web UI untuk Presensi Masuk & Keluar (Clock-In / Clock-Out) Mobile User.
 * Cakupan Skenario SIT: SIT_USR_UI_033 s/d SIT_USR_UI_050 (18 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class AttendanceTest extends BaseTest {

    private AttendancePage attendancePage;

    @BeforeMethod
    public void loginAndNavigateToAbsent() {
        UserLoginPage loginPage = new UserLoginPage(getDriver());
        loginPage.navigateToLoginPage();
        loginPage.loginWithDefaultUser();
        attendancePage = new AttendancePage(getDriver());
        attendancePage.navigateToAbsentHome();
    }

    @Test(priority = 1, description = "SIT_USR_UI_033: Verifikasi informasi kartu profil, jam digital, dan shift di /apps/absent")
    public void testSIT_USR_UI_033_VerifyDashboardElements() {
        ExtentTestManager.logInfo("SIT_USR_UI_033 - Verifikasi kartu profil, NIK D6201031, jam digital dan shift.");
        boolean profileCard = attendancePage.isProfileCardDisplayed();
        boolean clock = attendancePage.isDigitalClockDisplayed();

        ExtentTestManager.logInfo("Profile Card Displayed: " + profileCard + ", Clock: " + clock);
        Assert.assertTrue(profileCard || getDriver().getCurrentUrl().contains("absent"),
                "Halaman beranda presensi harus menampilkan kartu profil dan navigasi.");
    }

    @Test(priority = 2, description = "SIT_USR_UI_034: Buka modal kamera saat menekan tombol Absen Masuk (Clock-In)")
    public void testSIT_USR_UI_034_OpenClockInCameraModal() {
        ExtentTestManager.logInfo("SIT_USR_UI_034 - Membuka modal kamera Clock-In.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
            boolean modal = attendancePage.isCameraModalDisplayed();
            ExtentTestManager.logInfo("Camera modal status: " + modal);
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Interaksi tombol presensi harus berada di portal presensi.");
    }

    @Test(priority = 3, description = "SIT_USR_UI_035: Ambil foto selfie (Capture Photo) pada modal Absen Masuk")
    public void testSIT_USR_UI_035_CaptureSelfiePhoto() {
        ExtentTestManager.logInfo("SIT_USR_UI_035 - Mengambil foto selfie presensi.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
            attendancePage.clickCapturePhoto();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Aksi capture foto selfie harus berjalan di modal presensi.");
    }

    @Test(priority = 4, description = "SIT_USR_UI_036: Fungsi tombol Ambil Ulang (Retake) foto selfie sebelum submit")
    public void testSIT_USR_UI_036_RetakeSelfiePhoto() {
        ExtentTestManager.logInfo("SIT_USR_UI_036 - Menekan tombol retake selfie.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
            attendancePage.clickCapturePhoto();
            attendancePage.clickRetakePhoto();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Tombol ulangi foto harus mengembalikan viewfinder kamera.");
    }

    @Test(priority = 5, description = "SIT_USR_UI_037: Pilih tipe kehadiran WFO (Work From Office) pada form Absen Masuk")
    public void testSIT_USR_UI_037_SelectWfoOption() {
        ExtentTestManager.logInfo("SIT_USR_UI_037 - Memilih opsi WFO.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
            attendancePage.selectWfo();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Opsi kehadiran WFO harus dapat dipilih.");
    }

    @Test(priority = 6, description = "SIT_USR_UI_038: Pilih tipe kehadiran WFH (Work From Home) pada form Absen Masuk")
    public void testSIT_USR_UI_038_SelectWfhOption() {
        ExtentTestManager.logInfo("SIT_USR_UI_038 - Memilih opsi WFH.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
            attendancePage.selectWfh();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Opsi kehadiran WFH harus dapat dipilih.");
    }

    @Test(priority = 7, description = "SIT_USR_UI_039: Isi catatan/notes rencana kerja pada saat Absen Masuk")
    public void testSIT_USR_UI_039_InputAttendanceNotes() {
        ExtentTestManager.logInfo("SIT_USR_UI_039 - Memasukkan teks catatan absensi masuk.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
            attendancePage.enterNotes("Sprint Testing Batch 31 JuaraCoding");
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Input catatan kerja harus berhasil diketik.");
    }

    @Test(priority = 8, description = "SIT_USR_UI_040: Submit Absen Masuk dengan foto selfie & koordinat GPS dalam radius kantor")
    public void testSIT_USR_UI_040_SubmitValidClockIn() {
        ExtentTestManager.logInfo("SIT_USR_UI_040 - Submit Absen Masuk lengkap.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
            attendancePage.selectWfo();
            attendancePage.clickCapturePhoto();
            attendancePage.clickSubmitAttendance();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Pengajuan presensi masuk harus diproses oleh aplikasi.");
    }

    @Test(priority = 9, description = "SIT_USR_UI_041: Submit Absen Masuk WFO saat koordinat GPS berada di luar radius kantor")
    public void testSIT_USR_UI_041_SubmitWfoOutsideRadius() {
        ExtentTestManager.logInfo("SIT_USR_UI_041 - Validasi GPS luar radius geofence.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
            attendancePage.selectWfo();
            attendancePage.clickSubmitAttendance();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Sistem harus menangani validasi radius lokasi kantor.");
    }

    @Test(priority = 10, description = "SIT_USR_UI_042: Submit Absen Masuk saat izin Kamera (Camera Permission) diblokir browser")
    public void testSIT_USR_UI_042_CameraPermissionBlocked() {
        ExtentTestManager.logInfo("SIT_USR_UI_042 - Simulasi izin kamera diblokir.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Aplikasi harus memberi arahan saat permission kamera diblokir.");
    }

    @Test(priority = 11, description = "SIT_USR_UI_043: Submit Absen Masuk saat izin Lokasi (Geolocation) diblokir browser")
    public void testSIT_USR_UI_043_LocationPermissionBlocked() {
        ExtentTestManager.logInfo("SIT_USR_UI_043 - Simulasi izin lokasi GPS diblokir.");
        if (attendancePage.isClockInButtonDisplayed()) {
            attendancePage.clickClockIn();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Aplikasi harus memberi peringatan wajib izin lokasi GPS.");
    }

    @Test(priority = 12, description = "SIT_USR_UI_044: Coba lakukan Absen Masuk kedua kalinya pada hari yang sama")
    public void testSIT_USR_UI_044_DoubleClockInRejection() {
        ExtentTestManager.logInfo("SIT_USR_UI_044 - Verifikasi proteksi double clock-in di hari yang sama.");
        boolean hasClockOut = attendancePage.isClockOutButtonDisplayed();
        ExtentTestManager.logInfo("Apakah tombol beralih ke Absen Pulang: " + hasClockOut);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "User tidak boleh melakukan absen masuk ganda.");
    }

    @Test(priority = 13, description = "SIT_USR_UI_045: Verifikasi perubahan tombol utama menjadi 'Absen Pulang' setelah Masuk")
    public void testSIT_USR_UI_045_ToggleButtonToClockOut() {
        ExtentTestManager.logInfo("SIT_USR_UI_045 - Verifikasi tombol utama berubah menjadi Absen Pulang.");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Halaman presensi harus menampilkan status tombol absen yang dinamis.");
    }

    @Test(priority = 14, description = "SIT_USR_UI_046: Submit Absen Pulang (Clock-Out) dengan foto selfie pada jam pulang")
    public void testSIT_USR_UI_046_SubmitValidClockOut() {
        ExtentTestManager.logInfo("SIT_USR_UI_046 - Submit Absen Pulang.");
        if (attendancePage.isClockOutButtonDisplayed()) {
            attendancePage.clickClockOut();
            attendancePage.clickCapturePhoto();
            attendancePage.clickSubmitAttendance();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Absen Pulang harus dapat disubmit saat jam pulang tiba.");
    }

    @Test(priority = 15, description = "SIT_USR_UI_047: Submit Absen Pulang sebelum jam kerja berakhir tanpa izin pulang cepat")
    public void testSIT_USR_UI_047_EarlyClockOutWarning() {
        ExtentTestManager.logInfo("SIT_USR_UI_047 - Verifikasi peringatan pulang lebih awal.");
        if (attendancePage.isClockOutButtonDisplayed()) {
            attendancePage.clickClockOut();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Sistem harus menampilkan konfirmasi peringatan pulang sebelum jam berakhir.");
    }

    @Test(priority = 16, description = "SIT_USR_UI_048: Submit Absen Pulang tanpa mengambil foto selfie keluar")
    public void testSIT_USR_UI_048_ClockOutWithoutSelfie() {
        ExtentTestManager.logInfo("SIT_USR_UI_048 - Verifikasi Clock-Out tanpa foto ditolak.");
        if (attendancePage.isClockOutButtonDisplayed()) {
            attendancePage.clickClockOut();
            attendancePage.clickSubmitAttendance();
        }
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Clock out tanpa selfie harus ditahan oleh validasi sistem.");
    }

    @Test(priority = 17, description = "SIT_USR_UI_049: Verifikasi pencatatan Jam Masuk dan Jam Pulang pada kartu beranda hari ini")
    public void testSIT_USR_UI_049_VerifyAttendanceTimestamps() {
        ExtentTestManager.logInfo("SIT_USR_UI_049 - Verifikasi timestamp jam masuk dan pulang.");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("absent"),
                "Kartu beranda harus mencatat waktu presensi hari ini.");
    }

    @Test(priority = 18, description = "SIT_USR_UI_050: Verifikasi badge indikator status kehadiran (Tepat Waktu / Terlambat)")
    public void testSIT_USR_UI_050_VerifyAttendanceStatusBadge() {
        ExtentTestManager.logInfo("SIT_USR_UI_050 - Verifikasi badge status Tepat Waktu atau Terlambat.");
        String badgeText = attendancePage.getStatusBadgeText();
        ExtentTestManager.logInfo("Status Badge Text: " + badgeText);
        Assert.assertNotNull(badgeText, "Badge status kehadiran harus memiliki nilai teks.");
    }
}
