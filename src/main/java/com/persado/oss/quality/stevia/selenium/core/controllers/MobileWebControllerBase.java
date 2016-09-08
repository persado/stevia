package com.persado.oss.quality.stevia.selenium.core.controllers;

/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2016 Persado Intellectual Property Limited
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
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gkogketsof on 12/11/13.
 */
public abstract class MobileWebControllerBase extends WebControllerBase implements WebController {

    private WebDriver driver;

    private static final Logger MOBILEWEBDRIVER_LOG = LoggerFactory.getLogger(MobileWebControllerBase.class);

    /** The Constant THREAD_SLEEP. */
    private static final long THREAD_SLEEP = 100;

    public enum ElementLocatorType {
        xpath,
        tag,
        name,
    }

    @Override
    public void enableActionsLogging() {

    }

    @Override
    public void disableActionsLogging() {

    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public WebElement waitForElement(String locator) {
        return waitForElement(locator, SteviaContext.getWaitForElement());
    }

    @Override
    public WebElement waitForElement(String locator, long waitSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, waitSeconds,THREAD_SLEEP);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(determineLocator(locator)));
    }

    @Override
    public void waitForElementInvisibility(String locator) {

    }

    @Override
    public void waitForElementInvisibility(String locator, long waitSeconds) {

    }

    @Override
    public WebElement waitForElementPresence(String locator) {
        return waitForElementPresence(locator, SteviaContext.getWaitForElement());
    }

    @Override
    public WebElement waitForElementPresence(String locator, long waitSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, waitSeconds,THREAD_SLEEP);
        return wait.until(ExpectedConditions.presenceOfElementLocated(determineLocator(locator)));
    }

    @Override
    public List<WebElement> findElements(String locator) {
        return null;
    }

    @Override
    public void input(String locator, String value) {
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(value);
    }

    @Override
    public void press(String locator) {
        waitForElement(locator).click();
    }

    @Override
    public void pressAndWaitForPageToLoad(String locator) {

    }

    @Override
    public void pressAndWaitForElement(String pressLocator, String elementToWaitLocator, long waitSeconds) {

    }

    @Override
    public void pressAndWaitForElement(String pressLocator, String elementToWaitLocator) {

    }

    @Override
    public void pressAndClickOkInAlert(String locator) {

    }

    @Override
    public void pressAndClickOkInAlertNoPageLoad(String locator) {

    }

    @Override
    public void pressAndClickCancelInAlert(String locator) {

    }

    @Override
    public void select(String locator, String option) {

    }

    @Override
    public void selectByValue(String locator, String value) {

    }

    @Override
    public void multiSelectAdd(String locator, String option) {

    }

    @Override
    public Object executeJavascript(String js, Object... args) {
        return null;
    }

    @Override
    public void waitForCondition(String jscondition) {

    }

    @Override
    public void waitForCondition(String jscondition, long waitSeconds) {

    }

    @Override
    public void clear(String locator) {

    }

    @Override
    public Actions getBuilder() {
        return null;
    }

    @Override
    public void mouseOver(String locator) {

    }

    @Override
    public void mouseUp(String locator) {

    }

    @Override
    public void mouseDown(String locator) {

    }

    @Override
    public void click(String locator) {

    }

    @Override
    public void doubleClick(String locator) {

    }

    @Override
    public void highlight(String locator) {

    }

    @Override
    public void highlight(String locator, String color) {

    }

    @Override
    public void takeScreenShot() throws IOException {
        File scrFile = null;
        try {
            scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        } catch (Exception e) {
            MOBILEWEBDRIVER_LOG.error("Failed to generate screenshot, problem with driver: {} ", e.getMessage());
        }

        if (scrFile != null) {
            File file = createScreenshotFile();
            FileUtils.copyFile(scrFile, file);

            reportLogScreenshot(file);
        }
    }

    @Override
    public String getText(String locator) {
    	WebElement element = waitForElement(locator);
    	return element.getText();
    }

    @Override
    public void getFocus(String locator) {

    }

    @Override
    public String getSelectedOption(String locator) {
        return null;
    }

    @Override
    public List<String> getSelectedOptions(String locator) {
        return null;
    }

    @Override
    public String getInputValue(String locator) {
        return null;
    }

    @Override
    public boolean isAlertPresent() {
        return false;
    }

    @Override
    public boolean isTextPresent(String value) {
        return false;
    }

    @Override
    public boolean isTextNotPresent(String value) {
        return false;
    }

    @Override
    public boolean isComponentEditable(String locator) {
        return false;
    }

    @Override
    public boolean isComponentDisabled(String locator) {
        return false;
    }

    @Override
    public boolean isComponentPresent(String locator) {
        return false;
    }

    @Override
    public boolean isComponentPresent(String locator, long seconds) {
        return false;
    }

    @Override
    public boolean isComponentNotPresent(String locator) {
        return false;
    }

    @Override
    public boolean isComponentVisible(String locator) {
        return isComponentPresent(locator) && driver.findElement(determineLocator(locator)).isDisplayed();
    }

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

    @Override
    public boolean isComponentNotVisible(String locator) {
        return false;
    }

    @Override
    public boolean isComponentNotVisible(String locator, long seconds) {
        return false;
    }

    @Override
    public boolean isComponentSelected(String locator) {
        return false;
    }

    @Override
    public boolean isComponentNotSelected(String locator) {
        return false;
    }

    @Override
    public void pressLinkName(String linkName) {

    }

    @Override
    public void pressLinkNameAndWaitForPageToLoad(String linkName) {

    }

    @Override
    public void pressLinkNameAndClickOkInAlert(String linkName) {

    }

    @Override
    public void pressLinkNameAndClickOkInAlertNoPageLoad(String linkName) {

    }

    @Override
    public void pressLinkNameAndClickCancelInAlert(String linkName) {

    }

    @Override
    public void typeKeys(String locator, String value) {

    }

    @Override
    public void keyDown(String locator, KeyInfo thekey) {

    }

    @Override
    public void keyUp(String locator, KeyInfo thekey) {

    }

    @Override
    public void keyPress(String locator, KeyInfo thekey) {

    }

    @Override
    public void keyDown(KeyInfo thekey) {

    }

    @Override
    public void keyUp(KeyInfo thekey) {

    }

    @Override
    public void keyPress(KeyInfo thekey) {

    }

    @Override
    public void clickOkInAlert() {

    }

    @Override
    public void promptInputPressOK(String inputMessage) {

    }

    @Override
    public void promptInputPressCancel(String inputMessage) {

    }

    @Override
    public void clickCancelInAlert() {

    }

    @Override
    public void navigate(String url) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public String getTableElementRowPosition(String locator, String elementName) {
        return null;
    }

    @Override
    public int getNumberOfTotalRows(String locator) {
        return 0;
    }

    @Override
    public int getNumberOfTotalColumns(String locator) {
        return 0;
    }

    @Override
    public List<List<String>> getTableInfoAsList(String locator) {
        return null;
    }

    @Override
    public String getTableElementTextUnderHeader(String locator, String elementName, String headerName) {
        return null;
    }

    @Override
    public String getTableElementTextForRowAndColumn(String locator, String row, String column) {
        return null;
    }

    @Override
    public String getTableHeaderPosition(String locator, String headerName) {
        return null;
    }

    @Override
    public String getTableElementColumnPosition(String locator, String elementName) {
        return null;
    }

    @Override
    public List<String> getTableRecordsUnderHeader(String locator, String headerName) {
        return null;
    }

    @Override
    public String[][] getTableElements2DArray(String locator) {
        return new String[0][];
    }

    @Override
    public String getTableElementSpecificHeaderLocator(String locator, String elementName, String headerName) {
        return null;
    }

    @Override
    public String getTableElementSpecificRowAndColumnLocator(String locator, String row, String column) {
        return null;
    }

    @Override
    public String getAttributeValue(String locator, String attribute) {
        return waitForElement(locator).getAttribute(attribute);
    }

    @Override
    public HttpCookie getCookieByName(String name) {
        return null;
    }

    @Override
    public List<HttpCookie> getAllCookies() {
        return null;
    }

    @Override
    public void dragAndDrop(String locatorFrom, String locatorTo) {

    }

    @Override
    public void switchToLatestWindow() {

    }

    @Override
    public String getAlertText() {
        return null;
    }

    @Override
    public List<String> getAllListOptions(String locator) {
        return null;
    }

    @Override
    public void selectFrame(String frameID) {

    }

    @Override
    public void selectFrameMain() {

    }

    @Override
    public void maximizeWindow() {

    }

    @Override
    public int getNumberOfElementsMatchLocator(String locator) {
        return 0;
    }

    @Override
    public void moveToElement(String locator, int x, int y) {

    }

    @Override
    public void moveToElement(String locator) {

    }

    @Override
    public void moveByOffset(int xOffset, int yOffset) {

    }

    @Override
    public void waitForAjaxComplete(long milliseconds) {

    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public void dragAndDrop(String locatorFrom, int xOffset, int yOffset) {

    }

    @Override
    public Point getElementPosition(String locator) {
        return waitForElement(locator).getLocation();
    }

    @Override
    public String getPageSource() {
        return null;
    }

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
     * Determine locator.
     *
     * @param locator
     *            the locator
     * @return the by
     */
    public By determineLocator(String locator) {
        if (locator.startsWith(ElementLocatorType.xpath.toString())) {
            return By.xpath(findLocatorSubstring(locator));
        } else if (locator.startsWith("//")) {
            return By.xpath(locator);
        } else if(locator.startsWith(ElementLocatorType.name.toString())) {
            return By.name(findLocatorSubstring(locator));
        } else {
            return By.tagName(findLocatorSubstring(locator));
        }
    }

    /**
     * Find locator substring.
     *
     * @param locator the element locator
     * @return the string after the character '='
     */
    private String findLocatorSubstring(String locator){
        return locator.substring(locator.indexOf('=')+1);
    }

    public void scrollTo(String locator){
        WebElement element = waitForElementPresence(locator);
        HashMap<String,Double> scrollObject = new HashMap<String, Double>();
        scrollObject.put("element",Double.parseDouble(((RemoteWebElement) element).getId()));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("mobile: scrollTo", scrollObject);
    }

    public void setScreenOrientation(String orientation){
        System.out.println(((Rotatable)driver).getOrientation());
        ((Rotatable)driver).rotate(ScreenOrientation.LANDSCAPE);
//        HashMap<String,Double> scrollObject = new HashMap<String, Double>();
//        scrollObject.put("orientation",Double.parseDouble("LANDSCAPE"));
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("mobile: orientation", scrollObject);
    }

    public void tapOnElement(String locator){
        WebElement element = waitForElementPresence(locator);
        TouchActions tap = new TouchActions(driver).singleTap(element);
        tap.perform();
    }
}
