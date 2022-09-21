package com.dsalgo.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.dsalgo.base.TestBase;
import com.dsalgo.utils.ExcelUtility;

import io.qameta.allure.Step;

/* Common class created for all Data Structure landing/navigation pages*/
public class DataStructurePage extends TestBase {

	WebDriver driver;

	@FindBy(xpath = "//p[text()='Topics Covered']/..//a")
	List<WebElement> topics;

	@FindBy(xpath = "//a[text()='Try here>>>']")
	WebElement lnktry;

	@FindBy(xpath = "//pre[@class=' CodeMirror-line ']//span/span")
	WebElement codeArea;

	@FindBy(xpath = "//button[text()='Run']")
	WebElement btnRun;

	public DataStructurePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public String ValidatePageTitle() {
		return driver.getTitle();
	}

	@Step("Get the list of all topics displayed on the data structure page")
	public List<String> GetTopicsList() {
		List<String> list = topics.stream().map(element -> element.getAttribute("textContent"))
				.collect(Collectors.toList());
		return list;
	}

	@Step("Click on the topic link for \"{0}\" and navigate to the script execution page")
	public boolean NavigateToTopic(String topic) {

		boolean successfullNavigation = false;

		// Search for the specified topic link - using java streams 
		//NOTE: Alternately for loop can be used to loop through topics and match the gettext()
		Optional<WebElement> dataStructureTopic = topics.stream()
				.filter(x -> x.getAttribute("textContent").equalsIgnoreCase(topic)).findAny();

		if (dataStructureTopic.isPresent()) {
			scrollTo(dataStructureTopic.get());
			dataStructureTopic.get().click();
			scrollTo(lnktry);
			lnktry.click();
			successfullNavigation = true;
		}

		return successfullNavigation;
	}

	@Step("Execute the python script -> {0}")
	public void ExecuteScript(String topic, String script) {

		if (NavigateToTopic(topic)) {

			Actions action = new Actions(driver);
			action.moveToElement(codeArea).click().sendKeys(script).build().perform();

			scrollTo(btnRun);
			btnRun.click();

			// Navigate back to the main data structure page with topics
			driver.navigate().back();
			driver.navigate().back();
		}
	}

	@Step("Get the list of topics from {1} sheet of the excel")
	public List<String> GetTopics(String filepath, String sheetName) {
		List<String> topics = new ArrayList<String>();
		try {
			Object[][] dataFromExcel = ExcelUtility.ReadDataFromExcel(filepath, sheetName);
			for (Object[] topic : dataFromExcel) 
			{
				topics.add(topic[0].toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topics;

	}
}
