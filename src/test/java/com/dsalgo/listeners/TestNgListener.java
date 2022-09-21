package com.dsalgo.listeners;

import java.io.ByteArrayInputStream;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.dsalgo.base.TestBase;

import io.qameta.allure.Allure;

//Listener class to handle attaching screenshot functionality on test failure
public class TestNgListener extends TestBase implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult result) {

	}

	@Override
	public void onTestFailure(ITestResult result) {

		String methodName = result.getMethod().getMethodName();
		logger.fatal("Test failed -> " + methodName);

		WebDriver driver;
		try {
			driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driver")
					.get(result.getInstance());
			
			byte[] destFile = TakeScreenshotAsBytes(methodName, driver);
			
			Allure.addAttachment(methodName + "_FailedScreenshot", new ByteArrayInputStream(destFile));
			
		} catch (Exception e) {
			logger.fatal("Fatal error occured for " + methodName +". Error: "+result.getThrowable());
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		logger.info("Test skipped for-> " + methodName);

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {

	}

	@Override
	public void onStart(ITestContext context) {

	}

	@Override
	public void onFinish(ITestContext context) {
	}
	
}
