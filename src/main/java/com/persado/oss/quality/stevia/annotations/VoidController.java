package com.persado.oss.quality.stevia.annotations;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.persado.oss.quality.stevia.network.http.HttpCookie;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.commonapi.KeyInfo;

class VoidController implements WebController {

	@Override
	public void enableActionsLogging() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableActionsLogging() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WebElement waitForElement(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebElement waitForElement(String locator, long waitSeconds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void waitForElementInvisibility(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitForElementInvisibility(String locator, long waitSeconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WebElement waitForElementPresence(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebElement waitForElementPresence(String locator, long waitSeconds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WebElement> findElements(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void input(String locator, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void press(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressAndWaitForPageToLoad(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressAndWaitForElement(String pressLocator,
			String elementToWaitLocator, long waitSeconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressAndWaitForElement(String pressLocator,
			String elementToWaitLocator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressAndClickOkInAlert(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressAndClickOkInAlertNoPageLoad(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressAndClickCancelInAlert(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void select(String locator, String option) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectByValue(String locator, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void multiSelectAdd(String locator, String option) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object executeJavascript(String js, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void waitForCondition(String jscondition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitForCondition(String jscondition, long waitSeconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Actions getBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mouseOver(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseUp(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void click(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doubleClick(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void highlight(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void highlight(String locator, String color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeScreenShot() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getText(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getFocus(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSelectedOption(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSelectedOptions(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInputValue(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAlertPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTextPresent(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTextNotPresent(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentEditable(String locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentDisabled(String locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentPresent(String locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentPresent(String locator, long seconds) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentNotPresent(String locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentVisible(String locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentVisible(String locator, long seconds) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentNotVisible(String locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentNotVisible(String locator, long seconds) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentSelected(String locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComponentNotSelected(String locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pressLinkName(String linkName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressLinkNameAndWaitForPageToLoad(String linkName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressLinkNameAndClickOkInAlert(String linkName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressLinkNameAndClickOkInAlertNoPageLoad(String linkName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressLinkNameAndClickCancelInAlert(String linkName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void typeKeys(String locator, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyDown(String locator, KeyInfo thekey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyUp(String locator, KeyInfo thekey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPress(String locator, KeyInfo thekey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyDown(KeyInfo thekey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyUp(KeyInfo thekey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPress(KeyInfo thekey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickOkInAlert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void promptInputPressOK(String inputMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void promptInputPressCancel(String inputMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickCancelInAlert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void navigate(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTableElementRowPosition(String locator, String elementName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfTotalRows(String locator) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfTotalColumns(String locator) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Map<String, String>> getTableInfo(String locator,
			int numberOfColumns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<String>> getTableInfoAsList(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableElementTextUnderHeader(String locator,
			String elementName, String headerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableElementTextForRowAndColumn(String locator,
			String row, String column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableHeaderPosition(String locator, String headerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableElementColumnPosition(String locator,
			String elementName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTableRecordsUnderHeader(String locator,
			String headerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getTableElements2DArray(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableElementSpecificHeaderLocator(String locator,
			String elementName, String headerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableElementSpecificRowAndColumnLocator(String locator,
			String row, String column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeValue(String locator, String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpCookie getCookieByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HttpCookie> getAllCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dragAndDrop(String locatorFrom, String locatorTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void switchToLatestWindow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAlertText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllListOptions(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void selectFrame(String frameID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectFrameMain() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void maximizeWindow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberOfElementsMatchLocator(String locator) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void moveToElement(String locator, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveToElement(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveByOffset(int xOffset, int yOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitForAjaxComplete(long milliseconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sleep(long milliseconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCurrentUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dragAndDrop(String locatorFrom, int xOffset, int yOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getElementPosition(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPageSource() {
		// TODO Auto-generated method stub
		return null;
	}
	
}