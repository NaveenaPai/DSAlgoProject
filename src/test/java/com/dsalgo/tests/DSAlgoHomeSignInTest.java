package com.dsalgo.tests;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.dsalgo.base.TestBase;
import com.dsalgo.pages.GetStartedPage;
import com.dsalgo.pages.HomePage;
import com.dsalgo.pages.RegisterPage;
import com.dsalgo.pages.SignInPage;
import com.dsalgo.utils.ConfigReader;
import com.github.javafaker.Faker;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class DSAlgoHomeSignInTest extends TestBase {

	String userName, password;
	String expectedHTMLValidationMsg = "Please fill out this field.";
	String expectedPageTitle, actualPageTitle;

	GetStartedPage getStartedPage;
	HomePage homePage;
	SignInPage signInPage;
	RegisterPage registerPage;
	Faker faker = new Faker();
	public WebDriver driver;
	String browserName;

	/*
	 * Method called before the test to 1. Initialise the driver and get an instance
	 * of landing page for the application 2. Get the browser from driver instance
	 * to log into the files specifying the browser (cross browser testing) 3. Fetch
	 * the user credentials from config file
	 */

	@BeforeTest()
	@Severity(SeverityLevel.BLOCKER)
	@Description("Before test method to initialize the driver and navigate to DS Algo portal page")
	@Parameters("browser")
	public void setup(@Optional String browser) {
		driver = InitialiseDriver(browser);
		browserName = browser + ": ";

		NavigateToDSAlgoPortal();
		logger.info(browserName + "Sucessfully navigated to DS Algo Portal Page");
		getStartedPage = new GetStartedPage(driver);

		/* Fetch User Credentials */
		userName = ConfigReader.GetConfigValue("username");
		password = ConfigReader.GetConfigValue("password");
	}

	@Test()
	@Severity(SeverityLevel.BLOCKER)
	public void NavigateToHomePage() {
		homePage = getStartedPage.NavigateToHomePage();
		logger.info(browserName + "Sucessfully navigated to DS Algo Home Page ");
	}

	@Test(dependsOnMethods = "NavigateToHomePage")
	@Description("Test case to validate if user landed on home page")
	@Severity(SeverityLevel.BLOCKER)
	public void ValidateHomePageTitle() {

		String expectedTitle = "Numpy Ninja";
		String actualTitle;

		// Validate if landed on home page
		actualTitle = homePage.GetTitle();
		Assert.assertEquals(actualTitle, expectedTitle);
		logger.info(browserName + "Sucessfully navigated to DS Algo Home Page with title -> " + actualTitle);
	}

	@Test(dependsOnMethods = "NavigateToHomePage", groups = "BeforeSignIn")
	@Description("Test case method to validate the data structure dropdown values before user is signed in")
	@Severity(SeverityLevel.CRITICAL)
	public void VerifyDSValuesinDropDownBeforeSignIn() {
		List<String> expectedDataStructures = Arrays.asList("Arrays", "Linked List", "Stack", "Queue", "Tree", "Graph");
		List<String> actualDataStructures = homePage.GetDataStructureList();
		Assert.assertEquals(actualDataStructures, expectedDataStructures);
		logger.info(browserName	+ "Sucessfully verified the dropdown values for Data Structure dropdown. Values displayed are -> "
				+ actualDataStructures.toString());
	}

	@Test(dependsOnMethods = "NavigateToHomePage", groups = "BeforeSignIn")
	@Description("Test case method to validate the data structure selection in dropdown before user is signed in")
	@Severity(SeverityLevel.NORMAL)
	public void ValidateDropdownSelectBeforeSignIn() {
		String expectedErrorMsg = "You are not logged in";
		String actualErrorMsg;

		// Select from DataStructure Dropdown
		actualErrorMsg = homePage.SelectDataStructure("Arrays");
		Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
		logger.info(browserName	+ "Verified the alert message before sign in for Data Structure Dropdown. Message displayed -> "
				+ actualErrorMsg);
	}

	@Test(dependsOnMethods = "NavigateToHomePage", groups = "BeforeSignIn")
	@Severity(SeverityLevel.NORMAL)
	public void ValidateGetStartedBeforeSignIn() {
		String expectedErrorMsg = "You are not logged in";
		String actualErrorMsg;

		// Click on GetStarted for DataStructure
		actualErrorMsg = homePage.GetStartedWithDataStructure("Array");
		Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
		logger.info(browserName + "Verified the alert message before sign in for Get Started Click. Message displayed -> "
						+ actualErrorMsg);
	}

	@Test(dependsOnGroups = "BeforeSignIn")
	@Description("Test case method to validate different scenarios of invalid sign in")
	@Severity(SeverityLevel.NORMAL)
	public void ValidateFailedSignInScenarios() {

		signInPage = homePage.SignIn(); // Takes control to the Sign In page

		String actualErrorMsg;

		// Validate Sign in Error with no user details entered
		actualErrorMsg = signInPage.SignIn();
		Assert.assertEquals(actualErrorMsg, expectedHTMLValidationMsg);

		// Validate Sign in Error with only username and no password
		actualErrorMsg = signInPage.SignIn(userName);
		Assert.assertEquals(actualErrorMsg, expectedHTMLValidationMsg);

		// Validate error message for incorrect credentials - sending username as password and vice-versa
		String expectedValidationMsg = "Invalid Username and Password";
		actualErrorMsg = signInPage.SignIn(password, userName);
		Assert.assertEquals(actualErrorMsg, expectedValidationMsg);

		// Register new user - Navigate to registration page
		registerPage = signInPage.RegisterNewUser();
		logger.info(
				browserName + "Sucessfully validated all the negative test scenarios for invalid login credentials");
	}

	@Test(dependsOnMethods = "ValidateFailedSignInScenarios", groups = "successfullRegistration")
	@Description("Test case method to validate negative scenarios for user registration followed by registering the new user")
	@Severity(SeverityLevel.BLOCKER)
	public void RegisterUser() {
		String actualErrorMsg;
		String expectedValidationMsg = "password_mismatch:The two password fields didn’t match.";

		// Validate error message for password mismatch
		actualErrorMsg = registerPage.RegisterWithDiffPassword(userName, password);
		Assert.assertEquals(actualErrorMsg, expectedValidationMsg);

		userName = userName + faker.number().digits(5); // Generate unique id for every login using faker api
		String expectedMsg = "New Account Created. You are logged in as " + userName;
		
		// User is navigated to home page on successful registration
		homePage = registerPage.RegisterUser(userName, password); 

		// Validate the successful login message
		String welcomeMsg = homePage.VerifySuccessfullLogin();
		Assert.assertEquals(expectedMsg, welcomeMsg);
		logger.info(browserName + "Registered a new user and signed in -> user: " + userName);

	}

	
	/*
	 * Method called after all the tests have been executed to quit the current
	 * driver instance
	 */
	@AfterTest
	@Description("After test method to sign out and quit the browser")
	@Severity(SeverityLevel.CRITICAL)
	public void tearDown() {
		homePage = getStartedPage.NavigateToHomePage(ConfigReader.GetConfigValue("homeUrl"));
		String expectedMsg = "Logged out successfully";
		String actualMsg = homePage.SignOut();
		logger.info(browserName + "Logged out of the Portal !!!");

		getDriver().quit();
		Assert.assertEquals(actualMsg, expectedMsg);
	}
}
