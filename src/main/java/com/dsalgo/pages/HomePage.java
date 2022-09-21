package com.dsalgo.pages;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import io.qameta.allure.Step;

public class HomePage {

	WebDriver driver;

	@FindBy(linkText = " Register ")
	WebElement lnkRegister;

	@FindBy(linkText = "Sign in")
	WebElement lnkSignIn;

	@FindBy(linkText = "Data Structures")
	WebElement selectDataStructure;

	@FindBy(css = ".alert.alert-primary")
	WebElement alertMessage;

	@FindBy(css = ".dropdown-item")
	List<WebElement> dataStructures;

	@FindBy(xpath = "//h5")
	List<WebElement> dataStructureHeaders;

	@FindBy(linkText = "Sign out")
	WebElement lnkSignOut;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Step("Navigate to Registration Page")
	public RegisterPage RegisterUser() {
		return new RegisterPage(driver);
	}

	@Step("Navigate to Sign-in Page")
	public SignInPage SignIn() {

		lnkSignIn.click();
		return new SignInPage(driver);
	}

	@Step("Navigate to Registration Page")
	public RegisterPage Register() {
		lnkRegister.click();
		return new RegisterPage(driver);
	}

	@Step("Verify alert message displayed for successful login ")
	public String VerifySuccessfullLogin() {
		return alertMessage.getText();
	}

	@Step("Get the list of all data structures available")
	public List<String> GetDataStructureList() {
		List<String> list = dataStructures.stream().map(element -> element.getAttribute("textContent"))
				.collect(Collectors.toList());
		return list;
	}

	@Step("Navigate to {0} page")
	public DataStructurePage GetStarted(String dataStructure) {
		ClickGetStarted(dataStructure);
		return new DataStructurePage(driver);
	}

	@Step("Sign out from the application")
	public String SignOut() {
		lnkSignOut.click();
		return alertMessage.getText();
	}

	@Step(" Select \"{0}\" from DataStructure Dropdown")
	public String SelectDataStructure(String expectedDataStructure) {

		Optional<WebElement> dataStructure = dataStructures.stream()
				.filter(x -> x.getAttribute("textContent").equalsIgnoreCase(expectedDataStructure)).findAny();

		if (dataStructure.isPresent()) {
			selectDataStructure.click();
			dataStructure.get().click();
		}

		return alertMessage.getText();
	}

	public String GetStartedWithDataStructure(String dataStructure) {
		ClickGetStarted(dataStructure);
		return alertMessage.getText();
	}

	@Step("Get started with \"{0}\" data structure")
	private void ClickGetStarted(String expectedDataStructure) {

		Optional<WebElement> getStartedButton = dataStructureHeaders.stream()
				.filter(x -> x.getAttribute("textContent").equalsIgnoreCase(expectedDataStructure))
				.map(x -> x.findElement(By.xpath("//h5[text()='" + expectedDataStructure + "']/following-sibling::a")))
				.findAny();

		if (getStartedButton.isPresent()) {
			getStartedButton.get().click();
		}

	}

	public String GetTitle() {
		return driver.getTitle();
	}
}
