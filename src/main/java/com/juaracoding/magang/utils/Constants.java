package com.juaracoding.magang.utils;

/**
 * Sentralisasi seluruh data statis dan konfigurasi URL aplikasi HADIR Magang.
 */
public class Constants {

    private Constants() {
        // Private constructor to prevent instantiation
    }

    // ==========================================
    // 🌐 URL & ENDPOINTS
    // ==========================================
    public static final String BASE_URL = "https://magang.dikahadir.com";
    public static final String ADMIN_LOGIN_URL = BASE_URL + "/authentication/login";
    public static final String USER_LOGIN_URL = BASE_URL + "/absen/login";
    public static final String API_BASE_URI = BASE_URL;

    // API Endpoint Paths
    public static final String API_LOGIN_ENDPOINT = "/api/login";
    public static final String API_USERS_ENDPOINT = "/api/users";
    public static final String API_ACTIVITY_ENDPOINT = "/api/activity";
    public static final String API_ABSENTS_ENDPOINT = "/api/absents";
    public static final String API_CORRECTIONS_ENDPOINT = "/api/corrections";
    public static final String API_LEAVES_ENDPOINT = "/api/leaves";
    public static final String API_OVERTIMES_ENDPOINT = "/api/overtimes";
    public static final String API_DEPARTMENTS_ENDPOINT = "/api/departments";
    public static final String API_POSITIONS_ENDPOINT = "/api/positions";

    // ==========================================
    // 🔐 CREDENTIALS (ENCODED IN BASE64)
    // ==========================================
    // Admin (Tester Deny)
    public static final String ADMIN_EMAIL = "admin@hadir.com";
    public static final String ADMIN_PASSWORD_ENCODED = "TWFnYW5nU1FBX0pDQDEyMw=="; // MagangSQA_JC@123

    // User Magang (Tester Aji)
    public static final String USER_EMAIL = "hadirsqa1@gmail.com";
    public static final String USER_PASSWORD_ENCODED = "U1FBQEhhZGlyMTIzNDU="; // SQA@Hadir12345
    public static final String USER_DEFAULT_NIK = "D6201031";

    // Valid NIK pool for registration tests
    public static final String[] VALID_NIK_POOL = {
        "F1240001", "D6240005", "F1240002", "D6240006", "D6240007"
    };

    // ==========================================
    // ⏱️ TIMEOUT CONFIGURATION
    // ==========================================
    public static final int EXPLICIT_WAIT_TIMEOUT = 15; // Detik
    public static final int PAGE_LOAD_TIMEOUT = 30; // Detik

    // ==========================================
    // 📁 DIRECTORIES & REPORTS
    // ==========================================
    public static final String EXTENT_REPORT_DIR = "reports/";
    public static final String EXTENT_REPORT_FILE = EXTENT_REPORT_DIR + "ExtentReport.html";
    public static final String SCREENSHOTS_DIR = "reports/screenshots/";
    public static final String SIT_EXCEL_PATH = "docs/SIT_Scenario_Magang_Hadir.xlsx";
}

