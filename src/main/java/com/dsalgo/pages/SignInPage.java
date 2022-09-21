package com.dsalgo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import io.qameta.allure.Step;

public class SignInPage {

	WebDriver driver;

	@FindBy(name = "username")
	WebElement txtUserName;

	@FindBy(name = "password")
	WebElement txtPassword;

	@FindBy(xpath = "//input[@value='Login']")
	WebElement btnLogin;

	@FindBy(css = ".alert.alert-primary")
	WebElement alertMsg;

	@FindBy(linkText = "Register!")
	WebElement lnkRegister;

	public SignInPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	//Below Sign In methods are create as part of Negative testing to validate error messages
	@Step("Sign in without entering any user credntials - Negative scenario 1")
	public String SignIn() {
		btnLogin.click();
		String error = txtUserName.getAttribute("validationMessage");
		return error;
	}

	@Step("Sign in by entering only user name but no password - Negative scenario 2")
	public String SignIn(String userName) {
		txtUserName.sendKeys(userName);
		btnLogin.click();
		String error = txtPassword.getAttribute("validationMessage");
		txtUserName.clear();
		return error;
	}

	@Step("Sign in by entering incorrect user name & password - Negative scenario 3")
	public String SignIn(String userName, String password) {
		txtUserName.sendKeys(userName);
		txtPassword.sendKeys(password);
		btnLogin.click();
		String error = alertMsg.getText();
		return error;
	}
	
	@Step("Click on Register link to register new user")
	public RegisterPage RegisterNewUser()
	{
		lnkRegister.click();
		return new RegisterPage(driver);
	}
}
