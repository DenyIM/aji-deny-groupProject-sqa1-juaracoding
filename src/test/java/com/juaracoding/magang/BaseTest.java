package com.juaracoding.magang;

import com.juaracoding.magang.drivers.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Listeners;
import com.juaracoding.magang.listeners.TestListener;

/**
 * Kelas induk untuk seluruh pengujian Web UI Selenium.
 * Mengelola siklus hidup WebDriver per-metode uji.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "headless"})
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("true") String headless
    ) {
        // Cek apakah parameter headless dilewatkan via system property Maven (-Dheadless=false)
        String sysHeadless = System.getProperty("headless");
        boolean isHeadless = (sysHeadless != null && !sysHeadless.trim().isEmpty())
                ? Boolean.parseBoolean(sysHeadless)
                : Boolean.parseBoolean(headless);

        logger.info("Setting up Test: Browser = [{}], Headless = [{}]", browser, isHeadless);
        DriverFactory.initDriver(browser, isHeadless);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down Test: Closing WebDriver session.");
        DriverFactory.quitDriver();
    }

    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}

