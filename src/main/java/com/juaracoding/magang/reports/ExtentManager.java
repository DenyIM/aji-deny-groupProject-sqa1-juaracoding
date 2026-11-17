package com.juaracoding.magang.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.juaracoding.magang.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Singleton konfigurasi ExtentReports 5 dengan ExtentSparkReporter bertema Dark.
 */
public class ExtentManager {

    private static final Logger logger = LoggerFactory.getLogger(ExtentManager.class);
    private static ExtentReports extentReports;

    private ExtentManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Mengambil instance tunggal ExtentReports (Singleton).
     *
     * @return ExtentReports instance
     */
    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            createInstance();
        }
        return extentReports;
    }

    private static void createInstance() {
        File reportDir = new File(Constants.EXTENT_REPORT_DIR);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(Constants.EXTENT_REPORT_FILE);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("SIT Report - HADIR Magang Automation");
        sparkReporter.config().setReportName("Web UI (Selenium) & API (REST Assured) Test Execution");
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");
        sparkReporter.config().setEncoding("UTF-8");

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        // Metadata Sistem & Framework
        extentReports.setSystemInfo("Application", "HADIR Magang (magang.dikahadir.com)");
        extentReports.setSystemInfo("Environment", "Staging / Production SIT");
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReports.setSystemInfo("SDET Admin Scope", "Tester Deny (Admin HRD Web)");
        extentReports.setSystemInfo("SDET User Scope", "Tester Aji (User Mobile Mirroring)");
        extentReports.setSystemInfo("Total Scenarios", "100 Test Cases (70 UI + 30 API)");

        logger.info("ExtentReports 5 initialized successfully with Dark Theme.");
    }

    /**
     * Menyimpan dan menulis seluruh log ke dalam file HTML ExtentReport.
     */
    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReport flushed to: {}", Constants.EXTENT_REPORT_FILE);
        }
    }
}

