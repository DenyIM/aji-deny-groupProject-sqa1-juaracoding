package com.juaracoding.magang.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

/**
 * ThreadLocal manager untuk instance ExtentTest.
 * Menjamin isolasi logging antar thread saat pengujian berjalan paralel.
 */
public class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private ExtentTestManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Mengambil instance ExtentTest aktif milik thread saat ini.
     *
     * @return ExtentTest instance
     */
    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    /**
     * Memulai node pengujian baru pada ExtentReports.
     *
     * @param testName    Nama test case
     * @param description Deskripsi skenario pengujian
     * @return ExtentTest yang baru dibuat
     */
    public static synchronized ExtentTest startTest(String testName, String description) {
        ExtentTest test = ExtentManager.getInstance().createTest(testName, description);
        extentTestThreadLocal.set(test);
        return test;
    }

    /**
     * Menghapus instance ExtentTest dari ThreadLocal setelah test selesai.
     */
    public static void unload() {
        extentTestThreadLocal.remove();
    }

    public static void log(Status status, String message) {
        if (getTest() != null) {
            getTest().log(status, message);
        }
    }

    public static void logInfo(String message) {
        log(Status.INFO, message);
    }

    public static void logPass(String message) {
        log(Status.PASS, message);
    }

    public static void logFail(String message) {
        log(Status.FAIL, message);
    }

    public static void logSkip(String message) {
        log(Status.SKIP, message);
    }
}

