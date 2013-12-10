package com.persado.oss.quality.stevia.selenium.core.controllers;

import com.persado.oss.quality.stevia.network.http.HttpCookie;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.commonapi.KeyInfo;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.util.List;

/**
 * Created by gkogketsof on 12/10/13.
 */
public class AppiumWebController extends WebControllerBase implements WebController {
    private WebDriver driver;
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

    }

    @Override
    public WebElement waitForElement(String locator) {
        return null;
    }

    @Override
    public WebElement waitForElement(String locator, long waitSeconds) {
        return null;
    }

    @Override
    public void waitForElementInvisibility(String locator) {

    }

    @Override
    public void waitForElementInvisibility(String locator, long waitSeconds) {

    }

    @Override
    public WebElement waitForElementPresence(String locator) {
        return null;
    }

    @Override
    public WebElement waitForElementPresence(String locator, long waitSeconds) {
        return null;
    }

    @Override
    public List<WebElement> findElements(String locator) {
        return null;
    }

    @Override
    public void input(String locator, String value) {

    }

    @Override
    public void press(String locator) {

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

    }

    @Override
    public String getText(String locator) {
        return null;
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
        return false;
    }

    @Override
    public boolean isComponentVisible(String locator, long seconds) {
        return false;
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
        return null;
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
        return null;
    }

    @Override
    public String getPageSource() {
        return null;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
