package com.juaracoding.magang.api;

import com.aventstack.extentreports.Status;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.Constants;
import com.juaracoding.magang.utils.SecurityUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import com.juaracoding.magang.listeners.TestListener;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Kelas induk untuk seluruh pengujian API (REST Assured).
 * Mengatur baseURI, RequestSpecification standar, filter logging,
 * serta helper method untuk otentikasi JWT token secara aman.
 */
@Listeners(TestListener.class)
public abstract class BaseApiTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected static RequestSpecification defaultRequestSpec;
    protected static ResponseSpecification defaultResponseSpec;

    @BeforeClass(alwaysRun = true)
    public void setupApiEngine() {
        RestAssured.baseURI = Constants.API_BASE_URI;

        defaultRequestSpec = new RequestSpecBuilder()
                .setBaseUri(Constants.API_BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.URI)
                .log(LogDetail.METHOD)
                .build();

        defaultResponseSpec = new ResponseSpecBuilder()
                .build();

        logger.info("REST Assured initialized with Base URI: {}", Constants.API_BASE_URI);
    }

    /**
     * Helper method untuk login dan memperoleh JWT accessToken secara terenkripsi.
     *
     * @param email           Email akun
     * @param encodedPassword Password terenkripsi Base64
     * @return JWT accessToken string
     */
    protected String loginAndGetToken(String email, String encodedPassword) {
        String plainPassword = SecurityUtil.decode(encodedPassword);

        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("email", email);
        loginPayload.put("password", plainPassword);
        loginPayload.put("strategy", "local");

        logger.info("Attempting API Login for user: [{}]", email);

        try {
            Response response = given()
                    .spec(defaultRequestSpec)
                    .body(loginPayload)
            .when()
                    .post(Constants.API_LOGIN_ENDPOINT)
            .then()
                    .extract()
                    .response();

            int statusCode = response.getStatusCode();
            logger.info("Login API response status code: {}", statusCode);

            if (statusCode == 200 || statusCode == 201) {
                String token = response.jsonPath().getString("accessToken");
                if (token == null) {
                    token = response.jsonPath().getString("token");
                }
                if (token == null) {
                    token = response.jsonPath().getString("data.token");
                }

                ExtentTestManager.log(Status.INFO, "API Authentication successful for: " + email);
                return token;
            } else {
                logger.warn("Login failed with status {}: {}", statusCode, response.getBody().asString());
                ExtentTestManager.log(Status.WARNING, "API Authentication returned status " + statusCode);
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception during API login: {}", e.getMessage());
            ExtentTestManager.log(Status.WARNING, "API Login error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Membentuk RequestSpecification yang sudah dilengkapi header Authorization Bearer Token.
     *
     * @param token JWT token
     * @return RequestSpecification dengan Header Auth
     */
    protected RequestSpecification getAuthenticatedSpec(String token) {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addRequestSpecification(defaultRequestSpec);
        if (token != null && !token.trim().isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
        return builder.build();
    }
}

