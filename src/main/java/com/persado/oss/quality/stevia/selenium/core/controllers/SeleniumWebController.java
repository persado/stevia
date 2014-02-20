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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.persado.oss.quality.stevia.network.http.HttpCookie;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.commonapi.KeyInfo;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;


/**
 * The Class SeleniumWebController.
 */
public class SeleniumWebController extends WebControllerBase implements WebController {

	/** The Constant SELENIUM_LOG. */
	private static final Logger SELENIUM_LOG = LoggerFactory.getLogger(SeleniumWebController.class);

	/** The selenium. */
	private Selenium selenium;
	
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
	
	/**
	 * Gets the selenium.
	 * 
	 * @return the selenium
	 */
	public Selenium getSelenium() {
		return selenium;
	}

	/**
	 * Sets the selenium.
	 * 
	 * @param selenium
	 *            the new selenium
	 */
	public void setSelenium(Selenium selenium) {
		this.selenium = selenium;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * enableActionsLogging()
	 */
	@Deprecated
	public void enableActionsLogging() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * disableActionsLogging()
	 */
	@Deprecated
	public void disableActionsLogging() {
         
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForPageLoading(java.lang.String)
	 */
	public void waitForCondition(String jscondition) {
		waitForCondition(jscondition, SteviaContext.getWaitForElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForCondition(java.lang.String, long)
	 */
	public void waitForCondition(String jscondition, long waitSeconds) {
		String jsFunction = "selenium.browserbot.getCurrentWindow()." + jscondition;
		selenium.waitForCondition(jsFunction, String.valueOf(waitSeconds * TO_MILLIS));

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

		long startTime = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - startTime >= waitSeconds * TO_MILLIS)
				throw new SeleniumException("Timeout of " + waitSeconds + " seconds waiting for element " + locator + " visibility");
			try {
				if (selenium.isVisible(locator))
					break;
			} catch (Exception e) {
			}
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {

				SELENIUM_LOG.error(e.getMessage());
			}
		} while (true);
		selenium.highlight(locator);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForElementInvisibility(java.lang.String)
	 */
	@Override
	public void waitForElementInvisibility(String locator) {
		for (int second = 0;; second++) {
			if (second >= SteviaContext.getWaitForElementInvisibility())
				throw new SeleniumException("Timeout of " + SteviaContext.getWaitForElementInvisibility() + " seconds waiting for element " + locator + "invisibility");
			try {
				if (!selenium.isVisible(locator))
					break;
			} catch (Exception e) {
			}
			try {
				Thread.sleep(TO_MILLIS);
			} catch (InterruptedException e) {
				SELENIUM_LOG.error(e.getMessage());

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForElementInvisibility(java.lang.String, long)
	 */
	@Override
	public void waitForElementInvisibility(String locator, long waitSeconds) {
		for (int second = 0;; second++) {
			if (second >= waitSeconds)
				throw new SeleniumException("Timeout of " + waitSeconds + " seconds waiting for element " + locator + "invisibility");
			try {
				if (!selenium.isVisible(locator))
					break;
			} catch (Exception e) {
			}
			try {
				Thread.sleep(TO_MILLIS);
			} catch (InterruptedException e) {
				SELENIUM_LOG.error(e.getMessage());
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForElementPresence(java.lang.String, long)
	 */
	@Override
	public WebElement waitForElementPresence(String locator, long waitSeconds) {
		long startTime = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - startTime >= waitSeconds * TO_MILLIS)
				throw new SeleniumException("Timeout of " + waitSeconds + " seconds waiting for element " + locator + " visibility");
			try {
				if (selenium.isElementPresent(locator))
					break;
			} catch (Exception e) {
			}
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {

				SELENIUM_LOG.error(e.getMessage());
			}
		} while (true);
		selenium.highlight(locator);
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * waitForElementPresence(java.lang.String)
	 */
	@Override
	public WebElement waitForElementPresence(String locator) {
		return waitForElementPresence(locator, SteviaContext.getWaitForElement());
	}

	/**
	 * Find elements.
	 * 
	 * @param locator
	 *            the locator
	 * @return the list
	 * @deprecated
	 */
	@Override
	public List<WebElement> findElements(String locator) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#input(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void input(String locator, String value) {
		waitForElementPresence(locator);
		selenium.type(locator, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#press(
	 * java.lang.String)
	 */
	@Override
	public void press(String locator) {
		waitForElementPresence(locator);
		selenium.click(locator);
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
		selenium.waitForPageToLoad(Integer.toString(SteviaContext.getWaitForPageToLoad()*1000));

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
		clickOkInAlert();
		press(locator);
		selenium.waitForPageToLoad(Integer.toString(SteviaContext.getWaitForPageToLoad()*1000));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressAndClickOkInAlertNoPageLoad(java.lang.String)
	 */
	@Override
	public void pressAndClickOkInAlertNoPageLoad(String locator) {
		clickOkInAlert();
		press(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressAndClickCancelInAlert(java.lang.String)
	 */
	@Override
	public void pressAndClickCancelInAlert(String locator) {
		clickCancelInAlert();
		press(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#select
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void select(String locator, String option) {
		waitForElementPresence(locator);
		selenium.select(locator, "label=" + option);
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
		waitForElementPresence(locator);
		selenium.select(locator, "value=" + value);
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
		waitForElementPresence(locator);
		selenium.addSelection(locator, "label=" + option);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#clear(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void clear(String locator) {
		waitForElementPresence(locator);
		selenium.type(locator, "");
	}

	/**
	 * Gets the builder.
	 * 
	 * @return the builder
	 * @deprecated
	 */
	@Override
	public Actions getBuilder() {
		return null;
	}

	/**
	 * Check.
	 * 
	 * @param locator
	 *            the locator
	 */
	public void check(String locator) {
		waitForElementPresence(locator);
		selenium.check(locator);
	}

	/**
	 * Uncheck.
	 * 
	 * @param locator
	 *            the locator
	 */
	public void uncheck(String locator) {
		waitForElementPresence(locator);
		selenium.uncheck(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#doubleClick
	 * (java.lang.String)
	 */
	@Override
	public void doubleClick(String locator) {
		waitForElementPresence(locator);
		selenium.doubleClick(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#highlight
	 * (java.lang.String)
	 */
	@Override
	public void highlight(String locator) {
		waitForElementPresence(locator);
		selenium.highlight(locator);
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
		executeJavascript("this.browserbot.findElement(\"" + locator + "\").style.backgroundColor = '" + color + "'");
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
		String base64Screenshot = selenium.captureEntirePageScreenshotToString("");
		byte[] decodedScreenshot = Base64.decodeBase64(base64Screenshot.getBytes());
		FileOutputStream fos = null;
		File screenShotFname = createScreenshotFile();
		try {
			fos = new FileOutputStream(screenShotFname);
			fos.write(decodedScreenshot);
		} finally {
			if (null != fos) {
				fos.close();
			}

			reportLogScreenshot(screenShotFname);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getText
	 * (java.lang.String)
	 */
	@Override
	public String getText(String locator) {
		waitForElementPresence(locator);
		return selenium.getText(locator);
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
		waitForElementPresence(locator);
		selenium.focus(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#mouseOver
	 * (java.lang.String)
	 */
	@Override
	public void mouseOver(String locator) {
		waitForElementPresence(locator);
		selenium.mouseOver(locator);
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
		waitForElementPresence(locator);
		selenium.mouseUp(locator);
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
		waitForElementPresence(locator);
		selenium.mouseDown(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getSelectedLabel(java.lang.String)
	 */
	@Override
	public String getSelectedOption(String locator) {
		waitForElementPresence(locator);
		return selenium.getSelectedLabel(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getSelectOptions(java.lang.String)
	 */
	@Override
	public List<String> getSelectedOptions(String locator) {
		waitForElementPresence(locator);
		return Arrays.asList(selenium.getSelectedLabels(locator));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getInputValue
	 * (java.lang.String)
	 */
	@Override
	public String getInputValue(String locator) {
		waitForElementPresence(locator);
		return selenium.getValue(locator);
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
		return selenium.isAlertPresent();
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
		return selenium.isTextPresent(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isTextNotPresent(java.lang.String)
	 */
	@Override
	public boolean isTextNotPresent(String value) {
		return !selenium.isTextPresent(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentEditable(java.lang.String)
	 */
	@Override
	public boolean isComponentEditable(String locator) {
		waitForElementPresence(locator);
		return selenium.isEditable(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentDisabled(java.lang.String)
	 */
	@Override
	public boolean isComponentDisabled(String locator) {
		waitForElementPresence(locator);
		return !selenium.isEditable(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentPresent(java.lang.String)
	 */
	@Override
	public boolean isComponentPresent(String locator) {
		try {
			return selenium.isElementPresent(locator);
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentPresent(java.lang.String, long)
	 */
	@Override
	public boolean isComponentPresent(String locator, long seconds) {
		long startTime = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - startTime >= seconds * TO_MILLIS)
				return false;
			try {
				if (selenium.isElementPresent(locator))
					break;
			} catch (Exception e) {
			}
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
				SELENIUM_LOG.error(e.getMessage());
			}
		} while (true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentNotPresent(java.lang.String)
	 */
	@Override
	public boolean isComponentNotPresent(String locator) {
		return !isComponentPresent(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentVisible(java.lang.String)
	 */
	@Override
	public boolean isComponentVisible(String locator) {
		return isComponentPresent(locator) && selenium.isVisible(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentVisible(java.lang.String, long)
	 */
	@Override
	public boolean isComponentVisible(String locator, long seconds) {
		long startTime = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - startTime >= seconds * TO_MILLIS)
				return false;
			try {
				if (selenium.isVisible(locator))
					break;
			} catch (Exception e) {
			}
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
				SELENIUM_LOG.error(e.getMessage());
			}
		} while (true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentNotVisible(java.lang.String)
	 */
	@Override
	public boolean isComponentNotVisible(String locator) {
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
		long startTime = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - startTime >= seconds * TO_MILLIS)
				return false;
			try {
				if (!selenium.isVisible(locator))
					break;
			} catch (Exception e) {
			}
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
				SELENIUM_LOG.error(e.getMessage());
			}
		} while (true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentSelected(java.lang.String)
	 */
	@Override
	public boolean isComponentSelected(String locator) {
		waitForElementPresence(locator);
		return selenium.isChecked(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * isComponentNotSelected(java.lang.String)
	 */
	@Override
	public boolean isComponentNotSelected(String locator) {
		waitForElementPresence(locator);
		return !selenium.isChecked(locator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#clickOkInAlert
	 * ()
	 */

	public void clickOkInAlert() {
		executeJavascript("window.confirm = function(msg) { return true; }");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * clickCancelInAlert()
	 */
	public void clickCancelInAlert() {
		executeJavascript("window.confirm = function(msg) { return false; }");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#navigate
	 * (java.lang.String)
	 */
	@Override
	public void navigate(String url) {
		selenium.open(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#close()
	 */
	@Override
	public void close() {
		selenium.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#quit()
	 */
	@Override
	public void quit() {
		selenium.stop();
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
		highlight("link=" + linkName);
		selenium.click("link=" + linkName);
		selenium.getCookie();

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
		selenium.waitForPageToLoad(Integer.toString(SteviaContext.getWaitForPageToLoad()*1000));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressLinkNameAndClickOkInAlert(java.lang.String)
	 */
	@Override
	public void pressLinkNameAndClickOkInAlert(String linkName) {
		clickOkInAlert();
		pressLinkName(linkName);
		selenium.waitForPageToLoad(Integer.toString(SteviaContext.getWaitForPageToLoad()*1000));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressLinkNameAndClickOkInAlertNoPageLoad(java.lang.String)
	 */
	@Override
	public void pressLinkNameAndClickOkInAlertNoPageLoad(String linkName) {
		clickOkInAlert();
		pressLinkName(linkName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * pressLinkNameAndClickCancelInAlert(java.lang.String)
	 */
	@Override
	public void pressLinkNameAndClickCancelInAlert(String linkName) {
		clickCancelInAlert();
		pressLinkName(linkName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableHeaderPosition(java.lang.String, java.lang.String)
	 */
	public String getTableHeaderPosition(String locator, String headerName) {

		int columnHeaders = 0;
		String headerLocator1 = "";
		String headerLocator2 = "";
		String headerNumber = "headerNumber";

		if (locator.startsWith(XPATH) || locator.startsWith("//")) {
			columnHeaders = selenium.getXpathCount(locator + "//th[1 or count(*)=0]").intValue();
			headerLocator1 = locator + "//th[" + headerNumber + "][1]";
			headerLocator2 = locator + "//th[" + headerNumber + "][count(*)=0]";
		} else if (locator.startsWith(CSS)) {
			columnHeaders = selenium.getCssCount(locator + " th").intValue();
			headerLocator1 = locator + " th:nth-of-type(" + headerNumber + ") *:first-child";
			headerLocator2 = locator + " th:nth-of-type(" + headerNumber + ")";

		} else {
			columnHeaders = selenium.getXpathCount("/*[@id=']" + locator + "']//th[1 or count(*)=0]").intValue();

			headerLocator1 = "/*[@id=']" + locator + "']//th[" + headerNumber + "][1]";
			headerLocator2 = "/*[@id=']" + locator + "']//th[" + headerNumber + "][count(*)=0]";

		}

		for (int columnHeaderCounter = 1; columnHeaderCounter <= columnHeaders; columnHeaderCounter++) {
			try {
				if (selenium.getText(headerLocator1.replace("headerNumber", String.valueOf(columnHeaderCounter))).equals(headerName)) {
					return String.valueOf(columnHeaderCounter);
				}
			} catch (Exception e) {
				if (selenium.getText(headerLocator2.replace("headerNumber", String.valueOf(columnHeaderCounter))).equals(headerName)) {
					return String.valueOf(columnHeaderCounter);
				}
			}
		}
		throw new SeleniumException("Header Name not found");
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

		if (locator.startsWith(XPATH) || locator.startsWith("//")) {
			return locator + "//tbody//tr[" + row + "]//td[" + column + "]";

		} else if (locator.startsWith(CSS)) {
			return locator + " tr:nth-of-type(" + row + ") td:nth-of-type(" + column + ")";
		}

		else {
			return "/*[@id=']" + locator + "//tr[" + row + "]//td[" + column + "]";

		}

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
		if (locator.startsWith(XPATH) || locator.startsWith("//")) {
			return locator + "//tr[" + getTableElementRowPosition(locator, elementName) + "]//td[" + getTableHeaderPosition(locator, headerName) + "]";
		} else if (locator.startsWith(CSS)) {
			return locator + " tr:nth-child(" + getTableElementRowPosition(locator, elementName) + ") td:nth-child(" + getTableHeaderPosition(locator, headerName) + ")";
		} else {
			return "css=#" + locator + " tr:nth-child(" + getTableElementRowPosition(locator, elementName) + ") td:nth-child(" + getTableHeaderPosition(locator, headerName) + ")";
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.upstreamsystems.quality.stevia.selenium.core.WebController#
	 * getTableElementColumnPosition(java.lang.String, java.lang.String)
	 */
	@Override
	public String getTableElementColumnPosition(String locator, String elementName) {
		for (int i = 1; i <= getNumberOfTotalRows(locator); i++) {
			for (int j = 1; j <= getNumberOfTotalColumns(locator); j++) {
				if (selenium.getText(getRowLocator(locator, j).replace("rowNumber", String.valueOf(i))).equals(elementName)) {
					return String.valueOf(j);
				}
			}
		}
		throw new SeleniumException("Column name not Found");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElementRowPosition(java.lang.String, java.lang.String)
	 */
	@Override
	public String getTableElementRowPosition(String locator, String elementName) {
		for (int rowCounter = 1; rowCounter <= getNumberOfTotalRows(locator); rowCounter++) {
			for (int columnCounter = 1; columnCounter <= getNumberOfTotalColumns(locator); columnCounter++) {
				if (getText(getRowLocator(locator, columnCounter).replace("rowNumber", String.valueOf(rowCounter))).equals(elementName)) {
					return String.valueOf(rowCounter);
				}
			}
		}
		throw new SeleniumException("Element not Found");
	}

	/* (non-Javadoc)
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#getTableRecordsUnderHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getTableRecordsUnderHeader(String locator, String headerName) {
		List<String> records = new ArrayList<String>();
		for (int i = 1; i <= getNumberOfTotalRows(locator); i++) {
			records.add(getTableElementTextForRowAndColumn(locator, String.valueOf(i), getTableHeaderPosition(locator, headerName)));
		}
		return records;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElementTextForSpecificHeader(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getTableElementTextUnderHeader(String locator, String elementName, String headerName) {
		String row = String.valueOf(Integer.parseInt(getTableElementRowPosition(locator, elementName)));
		String column = String.valueOf(Integer.parseInt(getTableHeaderPosition(locator, headerName)) - 1);
		return selenium.getTable(locator + "." + row + "." + column);
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
		int rows = 0;

		if (locator.startsWith(XPATH) || locator.startsWith("//")) {
			rows = selenium.getXpathCount(locator + "//tbody//tr").intValue();

		} else if (locator.startsWith(CSS)) {
			rows = selenium.getCssCount(locator + " tbody tr").intValue();
		}

		else {
			rows = selenium.getXpathCount("/*[@id=']" + locator + "//tbody//tr").intValue();
		}
		return rows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getColumnsNumber(java.lang.String)
	 */
	@Override
	public int getNumberOfTotalColumns(String locator) {
		int columns = 0;

		if (locator.startsWith(XPATH) || locator.startsWith("//")) {
			columns = selenium.getXpathCount(locator + "//tbody//tr[1]//td").intValue();

		} else if (locator.startsWith(CSS)) {
			columns = selenium.getCssCount(locator + " tbody tr:nth-child(1) td").intValue();
		}

		else {
			columns = selenium.getXpathCount("/*[@id=']" + locator + "//tbody//tr[1]//td//").intValue();
		}
		return columns;
	}

	/**
	 * Gets the row locator.
	 * 
	 * @param locator
	 *            the locator
	 * @param headerName
	 *            the header name
	 * @return the row locator
	 */
	protected String getRowLocator(String locator, String headerName) {
		String rowLocator = "";
		String rowNumber = "rowNumber";
		String header = getTableHeaderPosition(locator, headerName);

		if (locator.startsWith(XPATH) || locator.startsWith("/")) {
			rowLocator = locator + "//tbody//tr[" + rowNumber + "]//td[" + header + "]";

		} else if (locator.startsWith(CSS)) {
			rowLocator = locator + " tr:nth-of-type(" + rowNumber + ") td:nth-of-type(" + header + ")";
		}

		else {
			rowLocator = "/*[@id=']" + locator + "//tr[" + rowNumber + "]//td[" + header + "]";

		}
		return rowLocator;
	}

	/**
	 * Gets the row locator.
	 * 
	 * @param locator
	 *            the locator
	 * @param columnNumber
	 *            the column number
	 * @return the row locator
	 */
	protected String getRowLocator(String locator, int columnNumber) {
		String rowLocator = "";
		String rowNumber = "rowNumber";

		if (locator.startsWith(XPATH) || locator.startsWith("/")) {
			rowLocator = locator + "//tbody//tr[" + rowNumber + "]//td[" + columnNumber + "]";

		} else if (locator.startsWith(CSS)) {
			rowLocator = locator + " tr:nth-of-type(" + rowNumber + ") td:nth-of-type(" + columnNumber + ")";
		}

		else {
			rowLocator = "/*[@id=']" + locator + "//tr[" + rowNumber + "]//td[" + columnNumber + "]";

		}
		return rowLocator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#click(
	 * java.lang.String)
	 */
	@Override
	public void click(String locator) {
		selenium.click(locator);

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
		waitForElementPresence(locator);
		selenium.typeKeys(locator, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyDown
	 * (java.lang.String, org.openqa.selenium.KeyInfo)
	 */
	@Override
	public void keyDown(String locator, KeyInfo thekey) {
		waitForElementPresence(locator);
		selenium.keyDown(locator, "\\" + thekey.getEvent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyUp(
	 * java.lang.String, org.openqa.selenium.KeyInfo)
	 */
	@Override
	public void keyUp(String locator, KeyInfo thekey) {
		waitForElementPresence(locator);
		selenium.keyUp(locator, "\\" + thekey.getEvent());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyPress
	 * (java.lang.String,
	 * com.persado.oss.quality.stevia.selenium.core.controllers
	 * .commonapi.KeyInfo)
	 */
	@Override
	public void keyPress(String locator, KeyInfo thekey) {
		waitForElementPresence(locator);
		selenium.keyPress(locator, "\\" + thekey.getEvent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyDown
	 * (com
	 * .upstreamsystems.quality.stevia.selenium.core.controllers.commonapi.KeyInfo
	 * )
	 */
	@Override
	public void keyDown(KeyInfo thekey) {
		selenium.keyDownNative(thekey.getEvent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyUp(
	 * com.upstreamsystems
	 * .quality.stevia.selenium.core.controllers.commonapi.KeyInfo)
	 */
	@Override
	public void keyUp(KeyInfo thekey) {
		selenium.keyUpNative(thekey.getEvent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#keyPress
	 * (com
	 * .upstreamsystems.quality.stevia.selenium.core.controllers.commonapi.KeyInfo
	 * )
	 */
	@Override
	public void keyPress(KeyInfo thekey) {
		selenium.keyPressNative(thekey.getEvent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * executeJavascript(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Object executeJavascript(String js, Object... args) {
		return selenium.getEval(js);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getAttributeValue(java.lang.String, java.lang.String)
	 */
	@Override
	public String getAttributeValue(String locator, String attribute) {
		waitForElementPresence(locator);
		return selenium.getAttribute(locator + "@" + attribute);
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
		waitForElementPresence(locatorFrom);
		waitForElementPresence(locatorTo);
		selenium.dragAndDropToObject(locatorFrom, locatorTo);

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
		waitForElementPresence(locatorFrom);
		selenium.dragAndDrop(locatorFrom, xOffset + "," + yOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * switchToNewWindow()
	 */
	@Override
	public void switchToLatestWindow() {
		String[] availableWindows = selenium.getAllWindowIds();
		selenium.selectWindow(availableWindows[availableWindows.length - 1]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getAllListOptions(java.lang.String)
	 */
	@Override
	public List<String> getAllListOptions(String locator) {
		waitForElementPresence(locator);
		return Arrays.asList(selenium.getSelectOptions(locator));
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
		selenium.selectFrame(frameID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * selectFrameMain()
	 */
	@Override
	public void selectFrameMain() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#maximizeWindow
	 * ()
	 */
	public void maximizeWindow() {
		selenium.windowMaximize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getNumberOfElementsMatchLocator(java.lang.String)
	 */
	@Override
	public int getNumberOfElementsMatchLocator(String locator) {
		if (locator.startsWith(XPATH) || locator.startsWith("//")) {
			return selenium.getXpathCount(locator).intValue();
		} else if (locator.startsWith(CSS)) {
			return selenium.getCssCount(locator).intValue();
		} else if (locator.startsWith(NAME)) {
			return selenium.getCssCount("css=[name=" + locator + "]").intValue();
		} else if (locator.startsWith(LINK)) {
			return selenium.getCssCount(locator).intValue();
		} else {
			return selenium.getCssCount("css=#" + locator).intValue();
		}

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
		waitForElement(locator);
		selenium.mouseMove(locator);
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
		waitForElement(locator);
		selenium.mouseMoveAt(locator, x + "," + y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#moveByOffset
	 * (int, int)
	 */
	@Deprecated
	public void moveByOffset(int xOffset, int yOffset) {

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
		return selenium.getAlert();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * promptInputPressOK(java.lang.String)
	 */
	@Override
	public void promptInputPressOK(String inputMessage) {
		// selenium.answerOnNextPrompt(inputMessage);
		executeJavascript("window.prompt = function(msg) { return '" + inputMessage + "'; }");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * promptInputPressCancel(java.lang.String)
	 */
	@Override
	public void promptInputPressCancel(String inputMessage) {

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
		return selenium.getTable(locator + "." + (Integer.valueOf(row) - 1) + "." + (Integer.valueOf(column) - 1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#
	 * getTableElements2DArray(java.lang.String)
	 */
	@Override
	public String[][] getTableElements2DArray(String locator) {
		int numberOrRows = getNumberOfTotalRows(locator);
		int numberOfColumns = getNumberOfTotalColumns(locator);
		String[][] table = new String[numberOrRows][numberOfColumns];

		for (int i = 0; i < numberOrRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				table[i][j] = getTableElementTextForRowAndColumn(locator, String.valueOf(i + 2), String.valueOf(j + 1));

			}
		}

		return table;
	}
	
	/* (non-Javadoc)
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#getTableInfoAsList(java.lang.String)
	 */
	@Override
	public List<List<String>> getTableInfoAsList(String locator) {
		int numberOrRows = getNumberOfTotalRows(locator);
		int numberOfColumns = getNumberOfTotalColumns(locator);
		List<List<String>> tableInfo = new ArrayList<List<String>>();

		for (int i = 0; i < numberOrRows; i++) {
			List<String> rowText = new ArrayList<String>();
			for (int j = 0; j < numberOfColumns; j++) {
				rowText.add(getTableElementTextForRowAndColumn(locator, String.valueOf(i + 2), String.valueOf(j + 1)));
			}
			tableInfo.add(rowText);
		}

		return tableInfo;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getCurrentUrl
	 * ()
	 */
	public String getCurrentUrl() {
		return selenium.getLocation();
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
		boolean ajaxComplete;
		long startTime = System.currentTimeMillis();
		do {
			ajaxComplete = Boolean.parseBoolean(((String) executeJavascript("selenium.browserbot.getCurrentWindow().jQuery.active == 0")));
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
				error(e.getMessage());
			}
			endTime = System.currentTimeMillis();
		} while (!ajaxComplete && endTime - startTime <= milliseconds);

		if (ajaxComplete) {
			info("The AJAX call was completed in " + (endTime - startTime) + " ms");
		} else {
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
		waitForElementPresence(locator);
		return new Point(selenium.getElementPositionLeft(locator).intValue(), selenium.getElementPositionTop(locator).intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#refresh()
	 */
	@Override
	public void refresh() {
		selenium.refresh();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#refresh()
	 */
	@Override
	public String getPageSource() {
		return selenium.getHtmlSource();
	}

	/* (non-Javadoc)
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#getCookieByName(java.lang.String)
	 */
	@Override
	public HttpCookie getCookieByName(String name) {
		return new HttpCookie(name, selenium.getCookieByName(name));
	}

	/* (non-Javadoc)
	 * @see com.persado.oss.quality.stevia.selenium.core.WebController#getAllCookies()
	 */
	@Override
	public List<HttpCookie> getAllCookies() {
		List<HttpCookie> allCookies = new ArrayList<HttpCookie>();
		String cookie = selenium.getCookie();
		allCookies.add(new HttpCookie(cookie.substring(0, cookie.indexOf('=')),cookie.substring(cookie.indexOf('=')+1)));
		return allCookies;
	}
	
	

}
