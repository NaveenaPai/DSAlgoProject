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



public class DSAlgoGraphTest extends TestBase {

	HomePage homePage;
	DataStructurePage dsPage;
	String expectedPageTitle, actualPageTitle;
	GetStartedPage getStartedPage;
	String homePageUrl;
	public WebDriver driver;	
	String dataStructureName ="Graph";
	
	String browserName;

	@BeforeClass
	@Severity(SeverityLevel.BLOCKER)
	@Description("Before class method to navigate to data structure page -> Graph")
	public void setup() {
		driver = getDriver();
		browserName=GetBrowserFromDriverInstance(driver)+": ";
		homePageUrl = ConfigReader.GetConfigValue("homeUrl");
		getStartedPage = new GetStartedPage(driver);
		homePage = getStartedPage.NavigateToHomePage(homePageUrl);
		dsPage = homePage.GetStarted(dataStructureName);
	}

	@Test(dependsOnGroups = "successfullRegistration")	
	@Description("Test case method to verify if user is on data structure page -> Graph")
	@Severity(SeverityLevel.NORMAL)
	public void VerifyGraphPageTitle() {

		// Validate if landed on right page
		expectedPageTitle =dataStructureName;
		actualPageTitle = dsPage.ValidatePageTitle();
		Assert.assertEquals(expectedPageTitle, actualPageTitle);
		logger.info(browserName+"Successfully landed on Data Structure page with title -> " + actualPageTitle);
	}

	@Test(dependsOnMethods = "VerifyGraphPageTitle")
	@Description("Test case method to verify links for different topics")
	@Severity(SeverityLevel.CRITICAL)
	public void VerifyGraphTopicsLinks() {

		String filePath = System.getProperty("user.dir") + "/src/main/resources/testdata/TestData_DSTopics.xlsx";
		String sheetName = dataStructureName;

		List<String> expectedLinks = dsPage.GetTopics(filePath, sheetName);
		List<String> actualLinks = dsPage.GetTopicsList();
		Assert.assertEquals(actualLinks, expectedLinks);
		logger.info(browserName+"Verified the topics links. Available links for " + sheetName + " page are -> "
				+ actualLinks.toString());
	}

	@Test(dependsOnMethods = "VerifyGraphTopicsLinks", dataProvider = "GetPythonScripts")
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
