package com.dsalgo.base;

import java.io.File;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dsalgo.utils.ConfigReader;

import io.qameta.allure.Step;

public class TestBase {

	public static WebDriverWait wait;
	
	static WebDriver driver;
	static ThreadLocal<WebDriver> threadsafeDriver = new ThreadLocal<>();
	
	static List<String> filePaths = new ArrayList<>();
	public static final Logger logger = LogManager.getLogger();

	@Step("Initialise the driver for browser -> {0}")
	public static WebDriver InitialiseDriver(String browser) {
		String driverPath;
		if (browser == null) // When not executing as testng suite, parameters in xml cannot be read, so
								// browser = null
			browser = ConfigReader.GetConfigValue("browser");
		switch (browser) {
		case "chrome":
			driverPath = System.getProperty("user.dir") + "/src/main/resources/drivers/chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", driverPath);
			driver = new ChromeDriver();
			break;
		case "edge":
			driverPath = System.getProperty("user.dir") + "/src/main/resources/drivers/msedgedriver.exe";
			System.setProperty("webdriver.edge.driver", driverPath);
			driver = new EdgeDriver();
			break;
		default:
			System.out.println("Please pass the correct browser value: " + browser);
			break;
		}

		wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		threadsafeDriver.set(driver);

		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		return getDriver();

	}

	// Get the right driver from thread pool (cross browser parallel testing)
	public static synchronized WebDriver getDriver() {
		return threadsafeDriver.get();
	}

	@Step("Navigate to the Portal page (Url set in the config file)")
	public static void NavigateToDSAlgoPortal() {
		getDriver().get(ConfigReader.GetConfigValue("url"));
	}

	/* Method to take Screen shot */
	public static String TakeScreenshot(String methodName, WebDriver driver, boolean isFailed) {
		TakesScreenshot screenShot = (TakesScreenshot) driver;
		File src = screenShot.getScreenshotAs(OutputType.FILE);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String time = timestamp.toString().replace(":", "_").replace(" ", "_");
		String dir = System.getProperty("user.dir");
		String browserName = GetBrowserFromDriverInstance(driver);
		String destFilePath = dir + "/ScreenShots/" + browserName + "/" + methodName + time + ".png";
		try {
			FileUtils.copyFile(src, new File(destFilePath));
			filePaths.add(destFilePath);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return destFilePath;
	}

	/*
	 * Method to derive the browser name from current driver instance. In case of
	 * Parallel cross browser testing, log files generated using log4j cannot
	 * differentiate browser
	 */
	public static String GetBrowserFromDriverInstance(WebDriver driver) {
		Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = caps.getBrowserName();
		return browserName;
	}

	@Step("Capture the screenshot for failed test cases")
	public static byte[] TakeScreenshotAsBytes(String methodName, WebDriver driver) {
		TakesScreenshot screenShot = (TakesScreenshot) driver;
		byte[] destFilePath = screenShot.getScreenshotAs(OutputType.BYTES);

		return destFilePath;
	}

	public static void scrollTo(WebElement element) {
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static void waitforVisibility(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

}
