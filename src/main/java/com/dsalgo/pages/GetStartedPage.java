package com.dsalgo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import io.qameta.allure.Step;

public class GetStartedPage {

	WebDriver driver;
	
	@FindBy(className = "btn")
	WebElement btnGetStarted;
	
	public GetStartedPage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}

	@Step("Navigate to Home Page by clicking on Get Started button")
	public HomePage NavigateToHomePage()
	{
		btnGetStarted.click();
		return new HomePage(driver);
		
	}
	
	@Step("Navigate to Home Page -> {0}")
	public HomePage NavigateToHomePage(String url)
	{
		driver.get(url);
		return new HomePage(driver);
		
	}
}
