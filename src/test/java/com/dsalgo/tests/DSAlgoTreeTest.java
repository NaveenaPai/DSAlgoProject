package com.dsalgo.tests;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.dsalgo.base.TestBase;
import com.dsalgo.pages.DataStructurePage;
import com.dsalgo.pages.GetStartedPage;
import com.dsalgo.pages.HomePage;
import com.dsalgo.utils.ConfigReader;
import com.dsalgo.utils.ExcelUtility;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class DSAlgoTreeTest extends TestBase {

	HomePage homePage;
	DataStructurePage dsPage;
	String expectedPageTitle, actualPageTitle;
	String dataStructureName ="Tree";
	
	public WebDriver driver;

	String browserName;

	@BeforeClass
	@Severity(SeverityLevel.BLOCKER)
	@Description("Before class method to navigate to data structure page -> Tree")
	public void setup() {
		driver = getDriver();
		browserName=GetBrowserFromDriverInstance(driver)+": ";	
		String homePageUrl = ConfigReader.GetConfigValue("homeUrl");
		GetStartedPage getStartedPage = new GetStartedPage(driver);
		homePage = getStartedPage.NavigateToHomePage(homePageUrl);
		dsPage = homePage.GetStarted(dataStructureName);
	}

	@Test(dependsOnGroups = "successfullRegistration")
	@Description("Test case method to verify if user is on data structure page -> Tree  ")
	@Severity(SeverityLevel.NORMAL)
	public void VerifyTreePageTitle() {

		// Validate if landed on right page
		expectedPageTitle =dataStructureName;
		actualPageTitle = dsPage.ValidatePageTitle();
		Assert.assertEquals(expectedPageTitle, actualPageTitle);
	}

	@Test(dependsOnMethods = "VerifyTreePageTitle")
	@Description("Test case method to verify links for different topics")
	@Severity(SeverityLevel.CRITICAL)
	public void VerifyTreeTopicsLinks() {

		String filePath = System.getProperty("user.dir") + "/src/main/resources/testdata/TestData_DSTopics.xlsx";
		String sheetName = dataStructureName;
		
		List<String> expectedLinks = dsPage.GetTopics(filePath, sheetName);	
		List<String> actualLinks = dsPage.GetTopicsList();
		Assert.assertEquals(actualLinks, expectedLinks);
		logger.info(browserName+"Verified the topics links. Available links for " + sheetName + " page are -> "
				+ actualLinks.toString());
	}

	@Test(dependsOnMethods = "VerifyTreeTopicsLinks", dataProvider = "GetPythonScripts")
	@Description("Test case method to execute python scripts for different topics")
	@Severity(SeverityLevel.CRITICAL)
	public void ValidateScriptExecution(String topic, String script) {
		dsPage.ExecuteScript(topic,script);
		logger.info(browserName+"Executed the scripts -> " + script);
	}

	@DataProvider(name = "GetPythonScripts")
	public Object[][] GetPythonScripts() throws Exception {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/testdata/TestData_PythonScripts.xlsx";
		String sheetName = dataStructureName;

		Object[][] testData = ExcelUtility.ReadDataFromExcel(filePath, sheetName);
		logger.info(browserName+"Successfully extracted scripts from the excel sheet for " + sheetName);
		return testData;
	}	
}
