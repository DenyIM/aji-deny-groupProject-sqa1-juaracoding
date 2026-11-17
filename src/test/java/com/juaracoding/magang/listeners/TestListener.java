package com.juaracoding.magang.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.juaracoding.magang.drivers.DriverFactory;
import com.juaracoding.magang.reports.ExtentManager;
import com.juaracoding.magang.reports.ExtentTestManager;
import com.juaracoding.magang.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG ITestListener yang menghubungkan seluruh siklus hidup pengujian ke ExtentReports 5.
 * Menyediakan pengelompokan otomatis kategori tester (Deny/Aji), tipe test (UI/API),
 * dan tangkapan screenshot Base64 otomatis saat test gagal.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite started: [{}]", context.getName());
        ExtentManager.getInstance();
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite finished: [{}]", context.getName());
        ExtentManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.trim().isEmpty()) {
            description = "Execution of " + methodName;
        }

        logger.info("--- Starting Test: [{}] - [{}] ---", methodName, description);
        var test = ExtentTestManager.startTest(methodName, description);

        // Otomatisasi Tag Kategori berdasarkan Package & Class Name
        String className = result.getTestClass().getName();
        if (className.contains(".admin.")) {
            test.assignCategory("Tester Deny (Admin HRD)");
        } else if (className.contains(".user.")) {
            test.assignCategory("Tester Aji (User Magang)");
        }

        if (className.contains(".api.")) {
            test.assignCategory("REST Assured API");
        } else if (className.contains(".tests.")) {
            test.assignCategory("Selenium Web UI");
        }

        // Assign TestNG Groups jika ada
        String[] groups = result.getMethod().getGroups();
        if (groups != null && groups.length > 0) {
            test.assignCategory(groups);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        logger.info("Test Passed: [{}]", methodName);
        ExtentTestManager.logPass("Test case passed successfully: " + methodName);
        ExtentTestManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        logger.error("Test Failed: [{}] - Reason: {}", methodName, throwable != null ? throwable.getMessage() : "Unknown");

        ExtentTestManager.logFail("Test case failed: " + (throwable != null ? throwable.getMessage() : ""));
        if (throwable != null && ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().fail(throwable);
        }

        // Ambil screenshot jika test memiliki sesi WebDriver aktif (Web UI Test)
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            String base64Screenshot = ScreenshotUtil.captureBase64(driver);
            if (!base64Screenshot.isEmpty() && ExtentTestManager.getTest() != null) {
                ExtentTestManager.getTest().fail(
                        "Failure Evidence Screenshot:",
                        MediaEntityBuilder.createScreenCaptureFromBase64String("data:image/png;base64," + base64Screenshot).build()
                );
            }
        }

        ExtentTestManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        logger.warn("Test Skipped: [{}]", methodName);
        ExtentTestManager.logSkip("Test case skipped: " + methodName);
        ExtentTestManager.unload();
    }
}

