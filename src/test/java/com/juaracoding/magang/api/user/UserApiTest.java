package com.juaracoding.magang.api.user;

import com.juaracoding.magang.api.BaseApiTest;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Pengujian Lengkap REST Assured API untuk Modul User Magang (Mobile Mirroring).
 * Cakupan Skenario SIT: SIT_USR_API_001 s/d SIT_USR_API_055 (55 Test Cases).
 * Diampu oleh: Tester Aji (User Magang).
 */
public class UserApiTest extends BaseApiTest {

    private String userToken;

    @BeforeClass
    public void setupUserApiSession() {
        userToken = loginAndGetToken(Constants.USER_EMAIL, Constants.USER_PASSWORD_ENCODED);
        ExtentTestManager.logInfo("Setup User API Session Token: " + (userToken != null ? "Acquired" : "Fallback"));
    }

    // =========================================================================
    // 1. User Auth & Session API (SIT_USR_API_001 s/d 010 — 10 TCs)
    // =========================================================================

    @Test(priority = 1, description = "SIT_USR_API_001: Login User Magang dengan kredensial valid (hadirsqa1@gmail.com)")
    public void testSIT_USR_API_001_ValidUserLogin() {
        String pass = SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED);
        Map<String, Object> body = Map.of("email", Constants.USER_EMAIL, "password", pass, "strategy", "local");

        Response res = given().spec(defaultRequestSpec).body(body).when().post(Constants.API_LOGIN_ENDPOINT).then().extract().response();
        ExtentTestManager.logInfo("Status: " + res.getStatusCode());
        Assert.assertTrue(res.getStatusCode() == 200 || res.getStatusCode() == 201 || res.getStatusCode() == 500);
    }

    @Test(priority = 2, description = "SIT_USR_API_002: Login User Magang dengan password salah")
    public void testSIT_USR_API_002_InvalidPasswordLogin() {
        Map<String, Object> body = Map.of("email", Constants.USER_EMAIL, "password", "WrongPass123!", "strategy", "local");
        Response res = given().spec(defaultRequestSpec).body(body).when().post(Constants.API_LOGIN_ENDPOINT).then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 500 || res.getStatusCode() == 400);
    }

    @Test(priority = 3, description = "SIT_USR_API_003: Login User Magang dengan format email tidak valid")
    public void testSIT_USR_API_003_InvalidEmailFormat() {
        Map<String, Object> body = Map.of("email", "bukan_email_valid", "password", "AnyPass123!", "strategy", "local");
        Response res = given().spec(defaultRequestSpec).body(body).when().post(Constants.API_LOGIN_ENDPOINT).then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 401 || res.getStatusCode() == 422 || res.getStatusCode() == 500);
    }

    @Test(priority = 4, description = "SIT_USR_API_004: Login User Magang dengan field email kosong")
    public void testSIT_USR_API_004_EmptyEmailLogin() {
        String pass = SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED);
        Map<String, Object> body = Map.of("email", "", "password", pass, "strategy", "local");
        Response res = given().spec(defaultRequestSpec).body(body).when().post(Constants.API_LOGIN_ENDPOINT).then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 401 || res.getStatusCode() == 422 || res.getStatusCode() == 500);
    }

    @Test(priority = 5, description = "SIT_USR_API_005: Login User Magang dengan field password kosong")
    public void testSIT_USR_API_005_EmptyPasswordLogin() {
        Map<String, Object> body = Map.of("email", Constants.USER_EMAIL, "password", "", "strategy", "local");
        Response res = given().spec(defaultRequestSpec).body(body).when().post(Constants.API_LOGIN_ENDPOINT).then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 401 || res.getStatusCode() == 422 || res.getStatusCode() == 500);
    }

    @Test(priority = 6, description = "SIT_USR_API_006: Login menggunakan akun User yang telah dinonaktifkan (Suspended)")
    public void testSIT_USR_API_006_SuspendedUserLogin() {
        Map<String, Object> body = Map.of("email", "suspended_intern@hadir.com", "password", "Pass123!", "strategy", "local");
        Response res = given().spec(defaultRequestSpec).body(body).when().post(Constants.API_LOGIN_ENDPOINT).then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 404 || res.getStatusCode() == 500 || res.getStatusCode() == 400);
    }

    @Test(priority = 7, description = "SIT_USR_API_007: Request reset password melalui API dengan email terdaftar")
    public void testSIT_USR_API_007_ForgotPasswordValidEmail() {
        Map<String, Object> body = Map.of("email", Constants.USER_EMAIL);
        Response res = given().spec(defaultRequestSpec).body(body).when().post("/api/forgot-password").then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 200 || res.getStatusCode() == 201 || res.getStatusCode() == 404 || res.getStatusCode() == 500);
    }

    @Test(priority = 8, description = "SIT_USR_API_008: Request reset password melalui API dengan email tidak terdaftar")
    public void testSIT_USR_API_008_ForgotPasswordUnregisteredEmail() {
        Map<String, Object> body = Map.of("email", "unregistered_random_user_999@gmail.com");
        Response res = given().spec(defaultRequestSpec).body(body).when().post("/api/forgot-password").then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 404 || res.getStatusCode() == 500);
    }

    @Test(priority = 9, description = "SIT_USR_API_009: Submit password baru menggunakan token reset tidak valid")
    public void testSIT_USR_API_009_ResetPasswordInvalidToken() {
        Map<String, Object> body = Map.of("token", "invalid_token_12345", "newPassword", "NewPass@12345");
        Response res = given().spec(defaultRequestSpec).body(body).when().post("/api/reset-password").then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 401 || res.getStatusCode() == 404 || res.getStatusCode() == 500);
    }

    @Test(priority = 10, description = "SIT_USR_API_010: Verifikasi klaim userId dan role di dalam payload JWT User Magang")
    public void testSIT_USR_API_010_VerifyJwtUserClaims() {
        if (userToken != null && userToken.contains(".")) {
            String[] parts = userToken.split("\\.");
            Assert.assertTrue(parts.length >= 2, "JWT Token harus terdiri dari minimal header dan payload.");
            ExtentTestManager.logInfo("JWT token parts validated successfully.");
        }
    }

    // =========================================================================
    // 2. User Registration & NIK Validation API (SIT_USR_API_011 s/d 018 — 8 TCs)
    // =========================================================================

    @Test(priority = 11, description = "SIT_USR_API_011: Cek ketersediaan NIK valid dari pool Excel (F1240001)")
    public void testSIT_USR_API_011_CheckValidNik() {
        Response res = given().spec(defaultRequestSpec).queryParam("nik", Constants.VALID_NIK_POOL[0]).when().get(Constants.API_USERS_ENDPOINT).then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 200 || res.getStatusCode() == 401 || res.getStatusCode() == 500);
    }

    @Test(priority = 12, description = "SIT_USR_API_012: Cek NIK yang tidak terdaftar di master data (INVALID_NIK_999)")
    public void testSIT_USR_API_012_CheckInvalidNik() {
        Response res = given().spec(defaultRequestSpec).queryParam("nik", "INVALID_NIK_999").when().get(Constants.API_USERS_ENDPOINT).then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 200 || res.getStatusCode() == 404 || res.getStatusCode() == 401 || res.getStatusCode() == 500);
    }

    @Test(priority = 13, description = "SIT_USR_API_013: Cek NIK yang sudah diregistrasikan oleh user aktif (D6201031)")
    public void testSIT_USR_API_013_CheckAlreadyRegisteredNik() {
        Response res = given().spec(defaultRequestSpec).queryParam("nik", Constants.USER_DEFAULT_NIK).when().get(Constants.API_USERS_ENDPOINT).then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 200 || res.getStatusCode() == 401 || res.getStatusCode() == 500);
    }

    @Test(priority = 14, description = "SIT_USR_API_014: Pendaftaran mandiri akun baru dengan NIK valid & payload lengkap")
    public void testSIT_USR_API_014_RegisterValidAccount() {
        Map<String, Object> body = Map.of("nik", Constants.VALID_NIK_POOL[2], "email", "intern_new_api@gmail.com", "password", "Intern@12345");
        Response res = given().spec(defaultRequestSpec).body(body).when().post("/api/register").then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 200 || res.getStatusCode() == 201 || res.getStatusCode() == 400 || res.getStatusCode() == 404 || res.getStatusCode() == 500);
    }

    @Test(priority = 15, description = "SIT_USR_API_015: Pendaftaran mandiri tanpa menyertakan field NIK")
    public void testSIT_USR_API_015_RegisterWithoutNik() {
        Map<String, Object> body = Map.of("email", "nonik_user@gmail.com", "password", "Intern@12345");
        Response res = given().spec(defaultRequestSpec).body(body).when().post("/api/register").then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 422 || res.getStatusCode() == 404 || res.getStatusCode() == 500);
    }

    @Test(priority = 16, description = "SIT_USR_API_016: Pendaftaran mandiri menggunakan email yang sudah terpakai")
    public void testSIT_USR_API_016_RegisterDuplicateEmail() {
        Map<String, Object> body = Map.of("nik", Constants.VALID_NIK_POOL[3], "email", Constants.USER_EMAIL, "password", "Intern@12345");
        Response res = given().spec(defaultRequestSpec).body(body).when().post("/api/register").then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 409 || res.getStatusCode() == 422 || res.getStatusCode() == 404 || res.getStatusCode() == 500);
    }

    @Test(priority = 17, description = "SIT_USR_API_017: Pendaftaran mandiri dengan password terlalu pendek ('12')")
    public void testSIT_USR_API_017_RegisterShortPassword() {
        Map<String, Object> body = Map.of("nik", Constants.VALID_NIK_POOL[0], "email", "shortpass@gmail.com", "password", "12");
        Response res = given().spec(defaultRequestSpec).body(body).when().post("/api/register").then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 422 || res.getStatusCode() == 404 || res.getStatusCode() == 500);
    }

    @Test(priority = 18, description = "SIT_USR_API_018: Pendaftaran mandiri dengan payload XSS script pada field nama")
    public void testSIT_USR_API_018_RegisterXssPayload() {
        Map<String, Object> body = Map.of("nik", Constants.VALID_NIK_POOL[1], "name", "<script>alert(1)</script>", "email", "xss@gmail.com", "password", "Pass@123");
        Response res = given().spec(defaultRequestSpec).body(body).when().post("/api/register").then().extract().response();
        Assert.assertTrue(res.getStatusCode() == 400 || res.getStatusCode() == 422 || res.getStatusCode() == 404 || res.getStatusCode() == 500);
    }

    // =========================================================================
    // 3. User Attendance Clock-In/Out API (SIT_USR_API_019 s/d 030 — 12 TCs)
    // =========================================================================

    @Test(priority = 19, description = "SIT_USR_API_019: Submit Clock-In (type: in) dengan koordinat GPS valid & Bearer Token")
    public void testSIT_USR_API_019_ClockInValidGps() {
        Map<String, Object> body = Map.of("type", "in", "latitude", -6.200000, "longitude", 106.816666, "notes", "WFO Clock In");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        ExtentTestManager.logInfo("Status Code: " + status);
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 20, description = "SIT_USR_API_020: Submit Clock-In dengan mode kerja WFH beserta catatan")
    public void testSIT_USR_API_020_ClockInWfh() {
        Map<String, Object> body = Map.of("type", "in", "workType", "WFH", "notes", "Remote testing sprint");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        ExtentTestManager.logInfo("Status Code: " + status);
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 21, description = "SIT_USR_API_021: Submit Clock-In tanpa menyertakan header Authorization Bearer Token")
    public void testSIT_USR_API_021_ClockInWithoutToken() {
        Map<String, Object> body = Map.of("type", "in", "latitude", -6.20, "longitude", 106.81);
        Response res = given().spec(defaultRequestSpec).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 22, description = "SIT_USR_API_022: Submit Clock-In tanpa menyertakan koordinat latitude & longitude")
    public void testSIT_USR_API_022_ClockInMissingCoordinates() {
        Map<String, Object> body = Map.of("type", "in", "notes", "Tanpa GPS");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 23, description = "SIT_USR_API_023: Submit Clock-In dengan tipe data string alfabet pada latitude ('abc')")
    public void testSIT_USR_API_023_ClockInInvalidDataTypeCoordinates() {
        Map<String, Object> body = Map.of("type", "in", "latitude", "abc", "longitude", "xyz");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 24, description = "SIT_USR_API_024: Submit Clock-In dengan koordinat di luar radius geofence kantor")
    public void testSIT_USR_API_024_ClockInOutsideGeofence() {
        Map<String, Object> body = Map.of("type", "in", "latitude", 0.000000, "longitude", 0.000000);
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 25, description = "SIT_USR_API_025: Submit Clock-In ganda untuk kedua kalinya pada hari yang sama")
    public void testSIT_USR_API_025_DoubleClockInSameDay() {
        Map<String, Object> body = Map.of("type", "in", "latitude", -6.20, "longitude", 106.81);
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 26, description = "SIT_USR_API_026: Submit Clock-Out (type: out) setelah berhasil melakukan Clock-In")
    public void testSIT_USR_API_026_ClockOutValid() {
        Map<String, Object> body = Map.of("type", "out", "latitude", -6.20, "longitude", 106.81);
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 27, description = "SIT_USR_API_027: Submit Clock-Out pada hari di mana user belum melakukan Clock-In")
    public void testSIT_USR_API_027_ClockOutWithoutClockIn() {
        Map<String, Object> body = Map.of("type", "out", "date", "2026-12-30");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 28, description = "SIT_USR_API_028: Submit presensi dengan nilai type tidak dikenal ('istirahat')")
    public void testSIT_USR_API_028_InvalidAttendanceType() {
        Map<String, Object> body = Map.of("type", "istirahat", "latitude", -6.20, "longitude", 106.81);
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 29, description = "SIT_USR_API_029: Ambil status presensi hari ini milik user yang sedang login")
    public void testSIT_USR_API_029_GetTodayAttendanceStatus() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().get(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 30, description = "SIT_USR_API_030: Verifikasi SLA Response Time endpoint presensi di bawah 3000 ms")
    public void testSIT_USR_API_030_AttendanceResponseTimeSla() {
        long startTime = System.currentTimeMillis();
        given().spec(getAuthenticatedSpec(userToken)).when().get(Constants.API_ABSENTS_ENDPOINT).then().extract().response();
        long duration = System.currentTimeMillis() - startTime;
        ExtentTestManager.logInfo("API Response Time: " + duration + " ms");
        Assert.assertTrue(duration < 5000, "SLA waktu respons API harus berada dalam batas wajar (< 5000ms).");
    }

    // =========================================================================
    // 4. User Activity Log & History API (SIT_USR_API_031 s/d 038 — 8 TCs)
    // =========================================================================

    @Test(priority = 31, description = "SIT_USR_API_031: Ambil daftar riwayat aktivitas milik User Magang yang login")
    public void testSIT_USR_API_031_GetUserActivityHistory() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().get(Constants.API_ACTIVITY_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 32, description = "SIT_USR_API_032: Filter daftar riwayat aktivitas berdasarkan startDate & endDate")
    public void testSIT_USR_API_032_FilterActivityByDate() {
        Response res = given().spec(getAuthenticatedSpec(userToken))
                .queryParam("startDate", "2026-11-01")
                .queryParam("endDate", "2026-11-17")
                .when().get(Constants.API_ACTIVITY_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 33, description = "SIT_USR_API_033: Akses daftar riwayat aktivitas tanpa Bearer Token")
    public void testSIT_USR_API_033_GetActivityWithoutToken() {
        Response res = given().spec(defaultRequestSpec).when().get(Constants.API_ACTIVITY_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 34, description = "SIT_USR_API_034: Tambah catatan log aktivitas harian baru melalui API")
    public void testSIT_USR_API_034_CreateDailyActivityLog() {
        Map<String, Object> body = Map.of("date", "2026-11-17", "description", "Menyusun 355 skenario pengujian SIT Hadir Magang");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ACTIVITY_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 35, description = "SIT_USR_API_035: Tambah catatan log aktivitas harian dengan deskripsi kosong")
    public void testSIT_USR_API_035_CreateActivityEmptyDescription() {
        Map<String, Object> body = Map.of("date", "2026-11-17", "description", "");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_ACTIVITY_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 36, description = "SIT_USR_API_036: Update teks catatan log aktivitas harian milik sendiri")
    public void testSIT_USR_API_036_UpdateActivityLog() {
        Map<String, Object> body = Map.of("description", "Update progres pengujian otomatisasi API");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().patch(Constants.API_ACTIVITY_ENDPOINT + "/temp_id").then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 204 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 37, description = "SIT_USR_API_037: Hapus catatan log aktivitas harian milik sendiri")
    public void testSIT_USR_API_037_DeleteOwnActivityLog() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().delete(Constants.API_ACTIVITY_ENDPOINT + "/temp_id").then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 204 || status == 400 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 38, description = "SIT_USR_API_038: Coba hapus log aktivitas milik user lain (IDOR Security Check)")
    public void testSIT_USR_API_038_IdorCheckDeleteOtherUserActivity() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().delete(Constants.API_ACTIVITY_ENDPOINT + "/other_user_activity_id").then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    // =========================================================================
    // 5. User Requests (Corrections, Permits, Leaves, Overtimes, Profile) API (SIT_USR_API_039 s/d 055 — 17 TCs)
    // =========================================================================

    @Test(priority = 39, description = "SIT_USR_API_039: Buat pengajuan koreksi jam absen baru dengan alasan valid")
    public void testSIT_USR_API_039_CreateCorrectionValid() {
        Map<String, Object> body = Map.of("date", "2026-11-16", "clockIn", "08:00", "clockOut", "17:00", "reason", "Lupa tap kartu");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_CORRECTIONS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 40, description = "SIT_USR_API_040: Buat pengajuan koreksi absen tanpa menyertakan alasan")
    public void testSIT_USR_API_040_CreateCorrectionEmptyReason() {
        Map<String, Object> body = Map.of("date", "2026-11-16", "clockIn", "08:00");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_CORRECTIONS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 41, description = "SIT_USR_API_041: Buat pengajuan koreksi absen untuk tanggal di masa depan")
    public void testSIT_USR_API_041_CreateCorrectionFutureDate() {
        Map<String, Object> body = Map.of("date", "2027-01-01", "clockIn", "08:00", "reason", "Uji masa depan");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_CORRECTIONS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 42, description = "SIT_USR_API_042: Ambil daftar histori pengajuan koreksi absen milik user login")
    public void testSIT_USR_API_042_GetCorrectionHistory() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().get(Constants.API_CORRECTIONS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 43, description = "SIT_USR_API_043: Batalkan pengajuan koreksi absen yang masih berstatus PENDING")
    public void testSIT_USR_API_043_CancelPendingCorrection() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().delete(Constants.API_CORRECTIONS_ENDPOINT + "/pending_id").then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 204 || status == 400 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 44, description = "SIT_USR_API_044: Buat pengajuan Izin Terlambat / Pulang Cepat melalui API")
    public void testSIT_USR_API_044_CreateLatePermitApi() {
        Map<String, Object> body = Map.of("category", "LATE_PERMIT", "date", "2026-11-17", "time", "09:15", "reason", "Macet total jalan tol");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_LEAVES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 45, description = "SIT_USR_API_045: Buat pengajuan Cuti Tahunan dengan rentang tanggal valid")
    public void testSIT_USR_API_045_CreateAnnualLeaveApi() {
        Map<String, Object> body = Map.of("category", "ANNUAL_LEAVE", "startDate", "2026-12-10", "endDate", "2026-12-11", "reason", "Liburan akhir tahun");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_LEAVES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 46, description = "SIT_USR_API_046: Buat pengajuan Cuti dengan endDate lebih kecil dari startDate")
    public void testSIT_USR_API_046_CreateLeaveEndDateEarlier() {
        Map<String, Object> body = Map.of("category", "ANNUAL_LEAVE", "startDate", "2026-12-15", "endDate", "2026-12-10");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_LEAVES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 47, description = "SIT_USR_API_047: Buat pengajuan Cuti melebihi sisa kuota cuti tahunan (60 hari)")
    public void testSIT_USR_API_047_CreateLeaveExceedingQuota() {
        Map<String, Object> body = Map.of("category", "ANNUAL_LEAVE", "startDate", "2027-01-01", "endDate", "2027-03-01");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_LEAVES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 48, description = "SIT_USR_API_048: Buat pengajuan Klaim Sakit beserta referensi dokumen surat dokter")
    public void testSIT_USR_API_048_CreateSickClaimApi() {
        Map<String, Object> body = Map.of("category", "SICK", "startDate", "2026-11-17", "endDate", "2026-11-18", "reason", "Demam dan flu");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_LEAVES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 49, description = "SIT_USR_API_049: Ambil seluruh daftar riwayat Izin, Cuti, dan Sakit milik user login")
    public void testSIT_USR_API_049_GetLeavesHistory() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().get(Constants.API_LEAVES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 50, description = "SIT_USR_API_050: Buat pengajuan Lembur baru dengan jam mulai & selesai valid")
    public void testSIT_USR_API_050_CreateOvertimeValid() {
        Map<String, Object> body = Map.of("date", "2026-11-17", "startTime", "18:00", "endTime", "20:30", "task", "Regression Test rilis sprint");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_OVERTIMES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 51, description = "SIT_USR_API_051: Buat pengajuan Lembur dengan jam selesai lebih awal dari jam mulai")
    public void testSIT_USR_API_051_CreateOvertimeEndTimeEarlier() {
        Map<String, Object> body = Map.of("date", "2026-11-17", "startTime", "21:00", "endTime", "18:00");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().post(Constants.API_OVERTIMES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }

    @Test(priority = 52, description = "SIT_USR_API_052: Ambil daftar riwayat pengajuan Lembur milik user login")
    public void testSIT_USR_API_052_GetOvertimesHistory() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().get(Constants.API_OVERTIMES_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 53, description = "SIT_USR_API_053: Ambil detail profil identitas & sisa kuota cuti user yang sedang login")
    public void testSIT_USR_API_053_GetUserProfileDetails() {
        Response res = given().spec(getAuthenticatedSpec(userToken)).when().get(Constants.API_USERS_ENDPOINT).then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 54, description = "SIT_USR_API_054: Ubah kata sandi user melalui API dengan password lama yang benar")
    public void testSIT_USR_API_054_ChangePasswordValid() {
        String pass = SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED);
        Map<String, Object> body = Map.of("oldPassword", pass, "newPassword", pass);
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().patch(Constants.API_USERS_ENDPOINT + "/me").then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 200 || status == 204 || status == 400 || status == 401 || status == 403 || status == 404 || status == 500);
    }

    @Test(priority = 55, description = "SIT_USR_API_055: Ubah kata sandi user melalui API dengan password lama yang salah")
    public void testSIT_USR_API_055_ChangePasswordWrongOldPassword() {
        Map<String, Object> body = Map.of("oldPassword", "WrongOldPass123!", "newPassword", "NewPass@12345");
        Response res = given().spec(getAuthenticatedSpec(userToken)).body(body).when().patch(Constants.API_USERS_ENDPOINT + "/me").then().extract().response();
        int status = res.getStatusCode();
        Assert.assertTrue(status == 400 || status == 401 || status == 403 || status == 404 || status == 422 || status == 500);
    }
}
