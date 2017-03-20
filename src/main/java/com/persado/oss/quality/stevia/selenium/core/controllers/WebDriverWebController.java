package com.persado.oss.quality.stevia.selenium.core.controllers;

/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2014 Persado
 * %%
 * Copyright (c) Persado Intellectual Property Limited. All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *  
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  
 * * Neither the name of the Persado Intellectual Property Limited nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */


import com.persado.oss.quality.stevia.network.http.HttpCookie;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.commonapi.KeyInfo;
import com.persado.oss.quality.stevia.selenium.core.controllers.webdriverapi.ByExtended;
import com.persado.oss.quality.stevia.selenium.listeners.ReportingWebDriverEventListener;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
//import com.thoughtworks.selenium.Selenium;
//import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;


// TODO: Auto-generated Javadoc
/**
 * The Class WebDriverMethods.
 */
public class WebDriverWebController extends WebControllerBase implements WebController {

	/** The Constant LOG. */
	private static final Logger WEBDRIVER_LOG = LoggerFactory.getLogger(WebDriverWebController.class);

	/** The driver. */
	private WebDriver driver;
	
	/** The Constant TO_MILLIS. */
	private static final int TO_MILLIS = 1000;
	
	/** The Constant THREAD_SLEEP. */
	private static final long THREAD_SLEEP = 100;
	
	/** The Constant XPATH. */
	private static final String XPATH = "xpath";
	
	/** The Constant CSS. */
	private static final String CSS = "css";
	
	/** The Constant NAME. */
	private static final String NAME = "name";
	
	/** The Constant LINK. */
	private static final String LINK = "link";
	
	/** The Constant ID. */
	private static final String ID = "id";
	
	/**
	 * Gets the driver.
	 * 
	 * @return the driver
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Sets the driver.
	 * 
	 * @param driver
	 *            the new driver
	 */
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Gets the selenium instance.
	 * 
	 * @param //baseUrl
	 *            the base url
	 * @return the selenium instance
	 */
	//public Selenium getSeleniumInstance(String baseUrl) {
	//	return new WebDriverBackedSelenium(driver, baseUrl);
	//}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * enableActionsLogging()
	 */
	@Override
	public void enableActionsLogging() {
//		this.setDriver(new EventFiringWebDriver(driver).register(new ReportingWebDriverEventListener()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * disableActionsLogging()
	 */
	@Override
	public void disableActionsLogging() {
//		this.setDriver(new EventFiringWebDriver(driver).unregister(new ReportingWebDriverEventListener()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForPageLoading(java.lang.String)
	 */
	@Override
	public void waitForCondition(String jscondition) {
		waitForCondition(jscondition, SteviaContext.getWaitForElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForCondition(java.lang.String, long)
	 */
	@Override
	public void waitForCondition(String jscondition, long waitSeconds) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean conditionResult;
		long startTime = System.currentTimeMillis();
		do {
			conditionResult = ((Boolean) js.executeScript(jscondition)).booleanValue();
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
				WEBDRIVER_LOG.error(e.getMessage());
			}
		} while (!conditionResult && System.currentTimeMillis() - startTime <= waitSeconds * TO_MILLIS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#waitForElement
	 * (org.openqa.selenium.String)
	 */

	/**
	 * Find locator substring.
	 *
	 * @param locator the element locator
	 * @return the string after the character '='
	 */
	private String findLocatorSubstring(String locator){
		return locator.substring(locator.indexOf('=')+1);
	}
	
	/**
	 * Determine locator.
	 * 
	 * @param locator
	 *            the locator
	 * @return the by
	 */
	public By determineLocator(String locator) {
		if (locator.startsWith(XPATH)) {
			return By.xpath(findLocatorSubstring(locator));
		} else if (locator.startsWith("//")) {
			return By.xpath(locator);
		} else if (locator.startsWith(CSS)) {
			return ByExtended.cssSelector(findLocatorSubstring(locator));
		} else if (locator.startsWith(NAME)) {
			return By.name(findLocatorSubstring(locator));
		} else if (locator.startsWith(LINK)) {
			return By.linkText(findLocatorSubstring(locator));
		} else if (locator.startsWith(ID)) {
			return By.id(findLocatorSubstring(locator));
		}
		else {
			return By.id(locator);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#waitForElement
	 * (java.lang.String)
	 */
	@Override
	public WebElement waitForElement(String locator) {
		return waitForElement(locator, SteviaContext.getWaitForElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#waitForElement
	 * (java.lang.String, long)
	 */
	@Override
	public WebElement waitForElement(String locator, long waitSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, waitSeconds,THREAD_SLEEP);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(determineLocator(locator)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForElementInvisibility(java.lang.String)
	 */
	@Override
	public void waitForElementInvisibility(String locator) {
		waitForElementInvisibility(locator, SteviaContext.getWaitForElementInvisibility());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForElementInvisibility(java.lang.String, long)
	 */
	@Override
	public void waitForElementInvisibility(String locator, long waitSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, waitSeconds);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(determineLocator(locator)));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForElementPresence(java.lang.String)
	 */
	public WebElement waitForElementPresence(String locator) {
		return waitForElementPresence(locator, SteviaContext.getWaitForElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#waitForElement
	 * (java.lang.String)
	 */

	public WebElement waitForElementPresence(String locator, long waitSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, waitSeconds,THREAD_SLEEP);
		return wait.until(ExpectedConditions.presenceOfElementLocated(determineLocator(locator)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#findElements
	 * (org.openqa.selenium.String)
	 */
	@Override
	public List<WebElement> findElements(String locator) {
		WebDriverWait wait = new WebDriverWait(driver, SteviaContext.getWaitForElement(),THREAD_SLEEP);
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(determineLocator(locator)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#input(
	 * org.openqa.selenium.String, java.lang.String)
	 */
	@Override
	public void input(String locator, String value) {
		WebElement element = waitForElement(locator);
		element.clear();
		element.sendKeys(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#press(
	 * org.openqa.selenium.String)
	 */
	@Override
	public void press(String locator) {
		waitForElement(locator).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressAndWaitForPageToLoad(java.lang.String)
	 */
	@Override
	public void pressAndWaitForPageToLoad(String locator) {
		press(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressAndWaitForElement(java.lang.String, java.lang.String, long)
	 */
	@Override
	public void pressAndWaitForElement(String pressLocator, String elementToWaitLocator, long waitSeconds) {
		press(pressLocator);
		waitForElement(elementToWaitLocator, waitSeconds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressAndWaitForElement(java.lang.String, java.lang.String)
	 */
	@Override
	public void pressAndWaitForElement(String pressLocator, String elementToWaitLocator) {
		pressAndWaitForElement(pressLocator, elementToWaitLocator, SteviaContext.getWaitForElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressAndClickOkInAlert(java.lang.String)
	 */
	@Override
	public void pressAndClickOkInAlert(String locator) {
		press(locator);
		clickOkInAlert();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressAndClickOkInAlertNoPageLoad(java.lang.String)
	 */
	@Override
	public void pressAndClickOkInAlertNoPageLoad(String locator) {
		pressAndClickOkInAlert(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressAndClickCancelInAlert(java.lang.String)
	 */
	@Override
	public void pressAndClickCancelInAlert(String locator) {
		press(locator);
		clickCancelInAlert();
	}

	/**
	 * Gets the select object.
	 * 
	 * @param locator
	 *            the locator
	 * @return the select object
	 */
	public Select getSelectObject(String locator) {
		return new Select(waitForElementPresence(locator));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#select
	 * (org.openqa.selenium.String, java.lang.String)
	 */
	@Override
	public void select(String locator, String option) {
		getSelectObject(locator).selectByVisibleText(option);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#selectByValue
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void selectByValue(String locator, String value) {
		getSelectObject(locator).selectByValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#multiSelectAdd
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void multiSelectAdd(String locator, String option) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#clear(
	 * org.openqa.selenium.String, java.lang.String)
	 */
	@Override
	public void clear(String locator) {
		WebElement element = waitForElement(locator);
		element.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getBuilder
	 * ()
	 */
	@Override
	public Actions getBuilder() {
		return new Actions(driver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#hover(
	 * org.openqa.selenium.String)
	 */
	@Override
	public void mouseOver(String locator) {
		getBuilder().moveToElement(waitForElement(locator)).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#mouseUp
	 * (java.lang.String)
	 */
	@Override
	public void mouseUp(String locator) {
		getBuilder().release(waitForElement(locator)).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#mouseDown
	 * (java.lang.String)
	 */
	@Override
	public void mouseDown(String locator) {
		getBuilder().clickAndHold(waitForElement(locator)).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#doubleClick
	 * (org.openqa.selenium.String)
	 */
	@Override
	public void doubleClick(String locator) {
		getBuilder().doubleClick(waitForElement(locator)).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#click(
	 * java.lang.String)
	 */
	public void click(String locator) {
		waitForElement(locator).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#typeKeys
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void typeKeys(String locator, String value) {
		waitForElement(locator).sendKeys(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyDown
	 * (java.lang.String, org.openqa.selenium.Keys)
	 */
	@Override
	public void keyDown(String locator, KeyInfo thekey) {
		getBuilder().keyDown(waitForElement(locator), thekey.getKey()).perform();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyUp(
	 * java.lang.String, org.openqa.selenium.Keys)
	 */
	@Override
	public void keyUp(String locator, KeyInfo thekey) {
		getBuilder().keyUp(waitForElement(locator), thekey.getKey()).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyDown
	 * (org.openqa.selenium.Keys)
	 */
	@Override
	public void keyDown(KeyInfo thekey) {
		getBuilder().keyDown(thekey.getKey()).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyUp(
	 * org.openqa.selenium.Keys)
	 */
	@Override
	public void keyUp(KeyInfo thekey) {
		getBuilder().keyUp(thekey.getKey()).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyPress
	 * (org.openqa.selenium.Keys)
	 */
	@Override
	public void keyPress(KeyInfo thekey) {
		getBuilder().sendKeys(thekey.getKey()).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyPress
	 * (java.lang.String, org.openqa.selenium.Keys)
	 */
	@Override
	public void keyPress(String locator, KeyInfo thekey) {
		waitForElement(locator).sendKeys(thekey.getKey());

	}

	/**
	 * Highlight.
	 * 
	 * @param element
	 *            the element
	 */
	public void highlight(WebElement element) {
		executeJavascript("arguments[0].style.backgroundColor = 'rgb(255, 255, 0)'", element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#highlight
	 * (org.openqa.selenium.String)
	 */
	@Override
	public void highlight(String locator) {
		executeJavascript("arguments[0].style.backgroundColor = 'rgb(255, 255, 0)'", waitForElement(locator));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#highlight
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void highlight(String locator, String color) {
		executeJavascript("arguments[0].style.backgroundColor = arguments[1]", waitForElement(locator), color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#takeScreenShot
	 * (java.io.File, java.lang.String)
	 */
	@Override
	public void takeScreenShot() throws IOException {

		File scrFile = null;
		try {
			scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		} catch (Exception e) {
			WEBDRIVER_LOG.error("Failed to generate screenshot, problem with driver: {} ", e.getMessage());
		}

		if (scrFile != null) {
			File file = createScreenshotFile();
			FileUtils.copyFile(scrFile, file);

			reportLogScreenshot(file);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getText
	 * (org.openqa.selenium.String)
	 */
	@Override
	public String getText(String locator) {
		return waitForElement(locator).getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getFocus
	 * (java.lang.String)
	 */
	@Override
	public void getFocus(String locator) {
		WebElement element = waitForElement(locator);
		if ("input".equals(element.getTagName())) {
			element.sendKeys("");
		} else {
			new Actions(driver).moveToElement(element).perform();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getSelectedLabel(org.openqa.selenium.String)
	 */
	@Override
	public String getSelectedOption(String locator) {
		return getSelectObject(locator).getFirstSelectedOption().getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getSelectOptions(org.openqa.selenium.String)
	 */
	@Override
	public List<String> getSelectedOptions(String locator) {
		List<String> optionValues = new ArrayList<String>();
		Select menuList = getSelectObject(locator);
		List<WebElement> options = menuList.getAllSelectedOptions();
		for (WebElement option : options) {
			optionValues.add(option.getText());
		}
		return optionValues;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getInputValue
	 * (org.openqa.selenium.String)
	 */
	@Override
	public String getInputValue(String locator) {
		return waitForElement(locator).getAttribute("value");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#isAlertPresent
	 * ()
	 */
	@Override
	public boolean isAlertPresent() {
		WebDriverWait wait = new WebDriverWait(driver, 0);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#isTextPresent
	 * (java.lang.String)
	 */
	@Override
	public boolean isTextPresent(String value) {
		return driver.getPageSource().contains(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isTextNotPresent(java.lang.String)
	 */
	@Override
	public boolean isTextNotPresent(String value) {
		return !driver.getPageSource().contains(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#refresh()
	 */
	@Override
	public String getPageSource() {
		return driver.getPageSource();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentEditable(org.openqa.selenium.String)
	 */
	@Override
	public boolean isComponentEditable(String locator) {
		return waitForElement(locator).isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentDisabled(org.openqa.selenium.String)
	 */
	@Override
	public boolean isComponentDisabled(String locator) {
		return !waitForElement(locator).isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentPresent(org.openqa.selenium.String)
	 */
	@Override
	public boolean isComponentPresent(String locator) {
		return driver.findElements(determineLocator(locator)).size() != 0;
	}

	/**
	 * Switch to window.
	 */
	public void switchToWindow() {
		Set<String> availableWindows = driver.getWindowHandles();
		Iterator<String> itr = availableWindows.iterator();
		String lastElement = itr.next();
		while (itr.hasNext()) {
			lastElement = itr.next();
		}
		driver.switchTo().window(lastElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentPresent(java.lang.String, long)
	 */
	@Override
	public boolean isComponentPresent(String locator, long seconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.presenceOfElementLocated(determineLocator(locator)));
			return true;
		} catch (Exception e) {
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentNotPresent(org.openqa.selenium.String)
	 */
	@Override
	public boolean isComponentNotPresent(String locator) {
		return driver.findElements(determineLocator(locator)).size() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentVisible(org.openqa.selenium.String)
	 */
	@Override
	public boolean isComponentVisible(String locator) {
		return isComponentPresent(locator) && driver.findElement(determineLocator(locator)).isDisplayed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentVisible(java.lang.String, long)
	 */
	@Override
	public boolean isComponentVisible(String locator, long seconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(determineLocator(locator)));
			return true;
		} catch (TimeoutException e) {
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentNotVisible(org.openqa.selenium.String)
	 */
	@Override
	public boolean isComponentNotVisible(final String locator) {
		return !isComponentVisible(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentNotVisible(java.lang.String, long)
	 */
	@Override
	public boolean isComponentNotVisible(String locator, long seconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(determineLocator(locator)));
			return true;
		} catch (TimeoutException e) {
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentSelected(org.openqa.selenium.String)
	 */
	@Override
	public boolean isComponentSelected(String locator) {
		return waitForElement(locator).isSelected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentNotSelected(org.openqa.selenium.String)
	 */
	@Override
	public boolean isComponentNotSelected(String locator) {
		return !waitForElement(locator).isSelected();
	}

	/**
	 * Wait for alert.
	 * 
	 * @return the alert
	 */
	public Alert waitForAlert() {
		WebDriverWait wait = new WebDriverWait(driver, SteviaContext.getWaitForElement());
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#clickOkInAlert
	 * ()
	 */
	public void clickOkInAlert() {

		waitForAlert().accept();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * clickCancelInAlert()
	 */
	public void clickCancelInAlert() {

		waitForAlert().dismiss();
	}

	/**
	 * Gets the full xpath.
	 * 
	 * @param locator
	 *            the locator
	 * @return the full xpath
	 */
	public String getFullXpath(String locator) {
		WebElement element = waitForElement(locator);
		String js = "gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase()";
		return "//" + executeJavascript(js, element);
	}

	/**
	 * Gets the full xpath.
	 * 
	 * @param element
	 *            the Webelement
	 * @return the full xpath
	 */
	public String getFullXpath(WebElement element) {
		String js = "gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase()";
		return "//" + executeJavascript(js, element);
	}

	/**
	 * Gets the table header position.
	 * 
	 * @param locator
	 *            the locator
	 * @param headerName
	 *            the header name
	 * @return the table header position
	 */
	public String getTableHeaderPosition(String locator, String headerName) {
		List<WebElement> columnHeaders = null;

		WebElement element = waitForElement(locator);

		columnHeaders = element.findElements(By.cssSelector("th"));

		int position = 1;
		for (WebElement record : columnHeaders) {
			if (record.getText().equals(headerName)) {
				return String.valueOf(position);
			}
			position++;
		}

		throw new WebDriverException("Header name not Found");
	}

	/* (non-Javadoc)
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#getTableElementColumnPosition(java.lang.String, java.lang.String)
	 */
	@Override
	public String getTableElementColumnPosition(String locator,	String elementName) {
		List<WebElement> tableRows = null;
		List<WebElement> tableColumnsPerRow = null;
		WebElement element = waitForElement(locator);
		tableRows = element.findElements(By.cssSelector("tbody tr"));
     
		int position = 1;
		for (WebElement row : tableRows) {
			tableColumnsPerRow = row.findElements(By.cssSelector("td"));
			for (WebElement column:tableColumnsPerRow){	
			if (column.getText().equals(elementName)) {
				return String.valueOf(position);
			 }
			 position++;
			}
			position = 1;
		}
		throw new WebDriverException("Column name not Found");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElementRowPosition(java.lang.String, java.lang.String)
	 */
	public String getTableElementRowPosition(String locator, String elementName) {

		List<WebElement> tableRows = null;
		List<WebElement> tableColumnsPerRow = null;
		WebElement element = waitForElement(locator);
		tableRows = element.findElements(By.cssSelector("tbody tr"));

		int position = 1;
		for (WebElement row : tableRows) {
			tableColumnsPerRow = row.findElements(By.cssSelector("td"));
			for (WebElement column : tableColumnsPerRow) {
				if (column.getText().equals(elementName)) {
					return String.valueOf(position);
				}
			}
			position++;
		}
		throw new WebDriverException("Element not Found");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElementTextForSpecificHeader(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	public String getTableElementTextUnderHeader(String locator, String elementName, String headerName) {
		WebElement element = waitForElement(locator);
		return element.findElement(By.cssSelector("tbody tr:nth-child(" + getTableElementRowPosition(locator, elementName) + ") td:nth-child(" + getTableHeaderPosition(locator, headerName) + ")"))
				.getText();

	}

    /* (non-Javadoc)
     * @see com.persado.oss.quality.stevia.selenium.core.WebController#getTableRecordsUnderHeader(java.lang.String, java.lang.String)
     */
    public List<String> getTableRecordsUnderHeader(String locator,String headerName) {		
		List<String> records = new ArrayList<String>(); 
		WebElement element = waitForElement(locator);
		String headerPosition = getTableHeaderPosition(locator,headerName);	
		List<WebElement> rows = element.findElements(By.cssSelector("tbody tr")); 
		for(WebElement row:rows){
			records.add(row.findElement(By.cssSelector("th:nth-child("+ headerPosition + "),td:nth-child("+ headerPosition + ")")).getText());
		}
        return records;
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElementTextForRowAndColumn(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getTableElementTextForRowAndColumn(String locator, String row, String column) {
		WebElement element = waitForElement(locator);
		return element.findElement(By.cssSelector("tr:nth-child(" + row + ") td:nth-child(" + column + ")")).getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElements2DArray(java.lang.String)
	 */
	@Override
	public String[][] getTableElements2DArray(String locator) {

		WebElement element = waitForElement(locator);
		List<WebElement> tableRows = element.findElements(By.cssSelector("tbody tr"));
		int numberOrRows = tableRows.size();
		int numberOfColumns = element.findElements(By.cssSelector("tbody tr:nth-child(1) td")).size();
		String[][] table = new String[numberOrRows][numberOfColumns];

		for (int i = 0; i < numberOrRows; i++) {
			List<WebElement> tableColumnsPerRow = tableRows.get(i).findElements(By.cssSelector("td"));
			for (int j = 0; j < tableColumnsPerRow.size(); j++) {
				table[i][j] = tableColumnsPerRow.get(j).getText();
			}
		}

		return table;
	}
	
	/* (non-Javadoc)
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#getTableInfoAsList(java.lang.String)
	 */
	@Override
	public List<List<String>> getTableInfoAsList(String locator) {
		WebElement table = waitForElement(locator);
		List<List<String>> tableInfo = new ArrayList<List<String>>();
		List<WebElement> tableRows = table.findElements(By.cssSelector("tbody tr"));
		for (WebElement row:tableRows){
			List<String> rowText = new ArrayList<String>();
			List<WebElement> columnsPerRow = row.findElements(By.cssSelector("td"));
			for(WebElement column:columnsPerRow){
				rowText.add(column.getText());
			}
			tableInfo.add(rowText);
		}
		return tableInfo;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElementSpecificHeaderLocator(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getTableElementSpecificHeaderLocator(String locator, String elementName, String headerName) {
		if (locator.startsWith(XPATH)) {
			return "//" + findLocatorSubstring(locator)+ "//tr[" + getTableElementRowPosition(locator, elementName) + "]//td[" + getTableHeaderPosition(locator, headerName) + "]";
		} else if (locator.startsWith("//")) {
			return locator + "//tr[" + getTableElementRowPosition(locator, elementName) + "]//td[" + getTableHeaderPosition(locator, headerName) + "]";
		}

		else if (locator.startsWith(CSS)) {
			return locator + " tr:nth-child(" + getTableElementRowPosition(locator, elementName) + ") td:nth-child(" + getTableHeaderPosition(locator, headerName) + ")";
		} else {
			return "css=#" + locator + " tr:nth-child(" + getTableElementRowPosition(locator, elementName) + ") td:nth-child(" + getTableHeaderPosition(locator, headerName) + ")";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElementSpecificRowAndColumnLocator(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String getTableElementSpecificRowAndColumnLocator(String locator, String row, String column) {
		if (locator.startsWith(XPATH)) {
			return "//" + findLocatorSubstring(locator) + "//tr[" + row + "]//td[" + column + "]";
		} else if (locator.startsWith("//")) {
			return locator + "//tr[" + row + "]//td[" + column + "]";
		}

		else if (locator.startsWith(CSS)) {
			return locator + " tr:nth-child(" + row + ") td:nth-child(" + column + ")";
		} else {
			return "css=#" + locator + " tr:nth-child(" + row + ") td:nth-child(" + column + ")";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#navigate
	 * (java.lang.String)
	 */
	public void navigate(String url) {
		Assert.hasLength(url, "url should be configured");
		driver.navigate().to(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getCurrentUrl
	 * ()
	 */
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#close()
	 */
	@Override
	public void close() {
		driver.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#quit()
	 */
	@Override
	public void quit() {
		driver.quit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#pressLinkName
	 * (java.lang.String)
	 */
	@Override
	public void pressLinkName(String linkName) {
		(new WebDriverWait(driver, SteviaContext.getWaitForElement())).until(ExpectedConditions.visibilityOfElementLocated((By.linkText(linkName)))).click();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressLinkNameAndWaitForPageToLoad(java.lang.String)
	 */
	@Override
	public void pressLinkNameAndWaitForPageToLoad(String linkName) {
		pressLinkName(linkName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressLinkNameAndClickOkInAlert(java.lang.String)
	 */
	@Override
	public void pressLinkNameAndClickOkInAlert(String linkName) {
		pressLinkName(linkName);
		clickOkInAlert();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressLinkNameAndClickOkInAlertNoPageLoad(java.lang.String)
	 */
	@Override
	public void pressLinkNameAndClickOkInAlertNoPageLoad(String linkName) {
		pressLinkNameAndClickOkInAlert(linkName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressLinkNameAndClickCancelInAlert(java.lang.String)
	 */
	@Override
	public void pressLinkNameAndClickCancelInAlert(String linkName) {
		pressLinkName(linkName);
		clickCancelInAlert();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * executeJavascript(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Object executeJavascript(String js, Object... args) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		return executor.executeScript(js, args);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getAttributeValue(java.lang.String, java.lang.String)
	 */
	@Override
	public String getAttributeValue(String locator, String attribute) {
		return waitForElement(locator).getAttribute(attribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#dragAndDrop
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void dragAndDrop(String locatorFrom, String locatorTo) {
		getBuilder().dragAndDrop(waitForElement(locatorFrom), waitForElement(locatorTo)).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#dragAndDrop
	 * (java.lang.String, int, int)
	 */
	@Override
	public void dragAndDrop(String locatorFrom, int xOffset, int yOffset) {
		getBuilder().dragAndDropBy(waitForElement(locatorFrom), xOffset, yOffset).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * switchToNewWindow()
	 */
	@Override
	public void switchToLatestWindow() {
		Iterator<String> itr = driver.getWindowHandles().iterator();
		String lastElement = null;
		while (itr.hasNext()) {
			lastElement = itr.next();
		}
		driver.switchTo().window(lastElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getAllListOptions(java.lang.String)
	 */
	@Override
	public List<String> getAllListOptions(String locator) {
		List<String> optionValues = new ArrayList<String>();
		Select menuList = getSelectObject(locator);
		List<WebElement> options = menuList.getOptions();
		for (WebElement option : options) {
			optionValues.add(option.getText());
		}
		return optionValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#selectFrame
	 * (java.lang.String)
	 */
	@Override
	public void selectFrame(String frameID) {
		selectFrameMain();
		driver.switchTo().frame(frameID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * selectFrameMain()
	 */
	@Override
	public void selectFrameMain() {
		driver.switchTo().defaultContent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#maximizeWindow
	 * ()
	 */
	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getNumberOfElementsMatchLocator(java.lang.String)
	 */
	@Override
	public int getNumberOfElementsMatchLocator(String locator) {
		return driver.findElements(determineLocator(locator)).size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#moveToElement
	 * (java.lang.String, int, int)
	 */
	@Override
	public void moveToElement(String locator, int x, int y) {
		getBuilder().moveToElement(waitForElement(locator), x, y).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#moveToElement
	 * (java.lang.String)
	 */
	@Override
	public void moveToElement(String locator) {
		getBuilder().moveToElement(waitForElement(locator)).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#moveByOffset
	 * (int, int)
	 */
	@Override
	public void moveByOffset(int xOffset, int yOffset) {
		getBuilder().moveByOffset(xOffset, yOffset).perform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getAlertText
	 * ()
	 */
	@Override
	public String getAlertText() {
		return waitForAlert().getText();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * promptInputPressOK(java.lang.String)
	 */
	@Override
	public void promptInputPressOK(String inputMessage) {
		Alert alert = waitForAlert();
		alert.sendKeys(inputMessage);
		alert.accept();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * promptInputPressCancel(java.lang.String)
	 */
	@Override
	public void promptInputPressCancel(String inputMessage) {
		Alert alert = waitForAlert();
		alert.sendKeys(inputMessage);
		alert.dismiss();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForAjaxComplete(long)
	 */
	@Override
	public void waitForAjaxComplete(long milliseconds) {
		long endTime;
		boolean ajaxComplete = false;
		long startTime = System.currentTimeMillis();
		do {
			try {
				ajaxComplete = ((Boolean) executeJavascript("return jQuery.active == 0")).booleanValue();
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
				error(e.getMessage());
			}
			endTime = System.currentTimeMillis();
		} while (!ajaxComplete && endTime - startTime <= milliseconds);

		if (!ajaxComplete) {
			warn("The AJAX call was not completed with in " + milliseconds + " ms");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getElementPosition(java.lang.String)
	 */
	@Override
	public Point getElementPosition(String locator) {
		return waitForElement(locator).getLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#refresh()
	 */
	@Override
	public void refresh() {
		driver.navigate().refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getRowsNumber
	 * (java.lang.String)
	 */
	@Override
	public int getNumberOfTotalRows(String locator) {
		return waitForElement(locator).findElements(By.cssSelector("tbody tr")).size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getColumnsNumber(java.lang.String)
	 */
	@Override
	public int getNumberOfTotalColumns(String locator) {
		return waitForElement(locator).findElements(By.cssSelector("tbody tr:nth-child(1) td")).size();
	}

	/* (non-Javadoc)
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#getCookieByName(java.lang.String)
	 */
	@Override
	public HttpCookie getCookieByName(String name) {
		return new HttpCookie(name, driver.manage().getCookieNamed(name).getValue());
	}

	/* (non-Javadoc)
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#getAllCookies()
	 */
	@Override
	public List<HttpCookie> getAllCookies() {
		List<HttpCookie> allCookies = new ArrayList<HttpCookie>();
		Iterator<Cookie> it = driver.manage().getCookies().iterator();
		while(it.hasNext()){
			Cookie c = it.next();
			allCookies.add(new HttpCookie(c.getName(), c.getValue()));			
		}
		return allCookies;
	}
}
