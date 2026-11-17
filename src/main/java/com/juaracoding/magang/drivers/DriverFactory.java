package com.juaracoding.magang.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Pengelola driver browser berbasis ThreadLocal untuk memastikan eksekusi thread-safe.
 * Mendukung mode Headless (default) dan Headed untuk demonstrasi visual.
 */
public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Menginisialisasi WebDriver sesuai nama browser dan konfigurasi headless.
     *
     * @param browser  Nama browser (chrome, firefox, edge)
     * @param headless true untuk mode headless (CI/CD background), false untuk mode visual
     * @return WebDriver yang telah diinisialisasi
     */
    public static WebDriver initDriver(String browser, boolean headless) {
        String browserName = (browser != null && !browser.trim().isEmpty()) ? browser.toLowerCase().trim() : "chrome";
        logger.info("Initializing WebDriver: Browser = [{}], Headless = [{}]", browserName, headless);

        WebDriver driver;
        switch (browserName) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--remote-allow-origins=*");
                edgeOptions.addArguments("--disable-gpu");
                edgeOptions.addArguments("--no-sandbox");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");

                // Disable automation banner & credential leak detection bubbles
                chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                chromeOptions.setExperimentalOption("useAutomationExtension", false);

                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.default_content_setting_values.geolocation", 1); // Allow geolocation for attendance
                chromeOptions.setExperimentalOption("prefs", prefs);

                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();

        setDriver(driver);
        return driver;
    }

    /**
     * Mengambil instance WebDriver milik thread saat ini.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Menyimpan instance WebDriver ke dalam ThreadLocal.
     *
     * @param driver WebDriver instance
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    /**
     * Menutup sesi WebDriver dan menghapus referensi dari ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver successfully terminated for current thread.");
            } catch (Exception e) {
                logger.error("Error occurred while quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}

