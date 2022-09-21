package com.dsalgo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.dsalgo.base.TestBase;

import io.qameta.allure.Step;

public class RegisterPage extends TestBase{
	WebDriver driver;
		
	@FindBy(name = "username")
	WebElement txtUserName;
	
	@FindBy(name = "password1")
	WebElement txtPassword;

	@FindBy(name = "password2")
	WebElement txtPassword2;
	
	@FindBy(css = ".alert.alert-primary")
	WebElement alertMsg;
	
	@FindBy(xpath = "//input[@value='Register']")
	WebElement btnRegister;
	
	public RegisterPage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}


	@Step("Register user with username : {0} and Password {1}")
	public HomePage RegisterUser(String userName, String password) {
		 
		txtUserName.sendKeys(userName); 
		txtPassword.sendKeys(password);
		txtPassword2.sendKeys(password);
		btnRegister.click();		
		return new HomePage(driver);
	}

	@Step("Negative testing -> Register user with different passwords")
	public String RegisterWithDiffPassword(String userName, String password) {
		txtUserName.sendKeys(userName);
		txtPassword.sendKeys(password);
		txtPassword2.sendKeys(password+"diff");
		btnRegister.click();		
		return alertMsg.getText();
	}

	
}
