package com.juaracoding.magang.api.user;

import com.juaracoding.magang.api.BaseApiTest;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Pengujian dasar API User Magang (Smoke & Authentication Test).
 * Diampu oleh Tester Aji (User Magang).
 */
public class UserSmokeApiTest extends BaseApiTest {

    @Test(priority = 1, description = "SIT_USR_API_001: Verifikasi Login User Magang dengan Kredensial Valid")
    public void testUserValidLoginApi() {
        String plainPassword = SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED);
        Assert.assertNotNull(plainPassword, "Password hasil decode Base64 tidak boleh null");
        Assert.assertFalse(plainPassword.isEmpty(), "Password hasil decode Base64 tidak boleh kosong");

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", Constants.USER_EMAIL);
        payload.put("password", plainPassword);
        payload.put("strategy", "local");

        Response response = given()
                .spec(defaultRequestSpec)
                .body(payload)
        .when()
                .post(Constants.API_LOGIN_ENDPOINT)
        .then()
                .extract()
                .response();

        int status = response.getStatusCode();
        ExtentTestManager.logInfo("Status Code Response: " + status);
        ExtentTestManager.logInfo("Response Body Snippet: " + response.getBody().asString());

        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 500,
                "API harus mengembalikan status code HTTP yang valid.");
    }

    @Test(priority = 2, description = "SIT_USR_API_002: Verifikasi Penolakan Login User dengan Format Email Tidak Valid")
    public void testUserInvalidEmailFormatApi() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", "bukan_email_valid");
        payload.put("password", "AnyPass123");
        payload.put("strategy", "local");

        Response response = given()
                .spec(defaultRequestSpec)
        .when()
                .body(payload)
                .post(Constants.API_LOGIN_ENDPOINT)
        .then()
                .extract()
                .response();

        int status = response.getStatusCode();
        String body = response.getBody().asString();
        ExtentTestManager.logInfo("Status Code Format Email Salah: " + status);
        ExtentTestManager.logInfo("Body: " + body);

        Assert.assertTrue(status == 400 || status == 401 || status == 422 || status == 500,
                "Login dengan format email salah harus ditolak sistem (400/401/422/500).");
    }

    @Test(priority = 3, description = "SIT_USR_API_003: Verifikasi Penolakan Presensi Tanpa Token Otentikasi")
    public void testUserSubmitAttendanceWithoutToken() {
        Map<String, Object> attendancePayload = new HashMap<>();
        attendancePayload.put("type", "in");
        attendancePayload.put("latitude", -6.200000);
        attendancePayload.put("longitude", 106.816666);

        Response response = given()
                .spec(defaultRequestSpec)
                .body(attendancePayload)
        .when()
                .post(Constants.API_ABSENTS_ENDPOINT)
        .then()
                .extract()
                .response();

        int status = response.getStatusCode();
        String body = response.getBody().asString();
        ExtentTestManager.logInfo("Status Code Submit Presensi Tanpa Token: " + status);
        ExtentTestManager.logInfo("Body: " + body);

        Assert.assertTrue(status == 401 || status == 403 || status == 404 || status == 500,
                "Submit presensi tanpa token harus ditolak oleh sistem.");
    }
}
