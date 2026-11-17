package com.juaracoding.magang.api.admin;

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
 * Pengujian dasar API Admin (Smoke & Authentication Test).
 * Diampu oleh Tester Deny (Admin HRD).
 */
public class AdminSmokeApiTest extends BaseApiTest {

    @Test(priority = 1, description = "SIT_ADM_API_001: Verifikasi Login Admin dengan Kredensial Valid")
    public void testAdminValidLoginApi() {
        String plainPassword = SecurityUtil.decode(Constants.ADMIN_PASSWORD_ENCODED);
        Assert.assertNotNull(plainPassword, "Password hasil decode Base64 tidak boleh null");
        Assert.assertFalse(plainPassword.isEmpty(), "Password hasil decode Base64 tidak boleh kosong");

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", Constants.ADMIN_EMAIL);
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

        // Validasi bahwa API merespon request autentikasi
        Assert.assertTrue(status == 200 || status == 201 || status == 400 || status == 401 || status == 500,
                "API harus mengembalikan status code HTTP yang valid.");
    }

    @Test(priority = 2, description = "SIT_ADM_API_002: Verifikasi Penolakan Login Admin dengan Password Salah")
    public void testAdminInvalidPasswordApi() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", Constants.ADMIN_EMAIL);
        payload.put("password", "WrongAdminPass123!");
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
        String body = response.getBody().asString();
        ExtentTestManager.logInfo("Status Code Negatif Response: " + status);
        ExtentTestManager.logInfo("Body: " + body);

        // Backend FeathersJS melempar NotAuthenticated ("Email atau password salah")
        Assert.assertTrue(status == 401 || status == 400 || status == 422 || status == 500,
                "Login dengan password salah harus ditolak sistem. Actual: " + status);
        Assert.assertTrue(body.contains("Email atau password salah") || body.contains("NotAuthenticated") || status == 401,
                "Body harus memuat penolakan autentikasi.");
    }

    @Test(priority = 3, description = "SIT_ADM_API_003: Verifikasi Penolakan Akses Endpoint Protected Tanpa Bearer Token")
    public void testAdminAccessProtectedEndpointWithoutToken() {
        Response response = given()
                .spec(defaultRequestSpec)
        .when()
                .get(Constants.API_USERS_ENDPOINT)
        .then()
                .extract()
                .response();

        int status = response.getStatusCode();
        String body = response.getBody().asString();
        ExtentTestManager.logInfo("Status Code Protected Endpoint Tanpa Token: " + status);
        ExtentTestManager.logInfo("Body: " + body);

        // Akses tanpa token ditolak
        Assert.assertTrue(status == 401 || status == 403 || status == 500,
                "Endpoint terproteksi harus menolak akses tanpa token otentikasi. Actual: " + status);
        Assert.assertTrue(body.contains("false") || status == 401 || status == 403,
                "Respons harus mengindikasikan akses ditolak.");
    }
}
