package com.persado.oss.quality.stevia.selenium.core;

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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.persado.oss.quality.stevia.network.http.HttpCookie;
import com.persado.oss.quality.stevia.selenium.core.controllers.commonapi.KeyInfo;



// TODO: Auto-generated Javadoc
/**
 * The Interface WebController.
 */
public interface WebController {

	/**
	 * Enable actions logging.
	 */
	void enableActionsLogging();

	/**
	 * Disable logging messages in TestNG Reports.
	 * Applies only in WebDriver
	 */
	void disableActionsLogging();

	/**
	 * Close the current window, quitting the browser if it's the last window currently open.
	 */

	void close();
	
	/**
	 * Quits the controller, closing every associated window.
	 */
	void quit();

	/**
	 * Wait for an element to become visible, with maximum time 10 seconds.If the
	 * time expires an Exception is thrown
	 * 
	 * @param locator an element locator
	 * @return the web element in case of WebDriver or null in case of Selenium
	 */
	WebElement waitForElement(String locator);

	/**
	 * Wait for an element to become visible with maximum time in seconds given as parameter.
	 * If the time expires an Exception is thrown
	 * 
	 * @param locator an element locator
	 * @param waitSeconds the number of seconds to wait for element visibility
	 * @return the web element in case of WebDriver or null in case of Selenium
	 */
	WebElement waitForElement(String locator, long waitSeconds);

	
	/**
	 * Wait for element invisibility.
	 *
	 * @param locator the element locator
	 * 
	 */
	void waitForElementInvisibility(String locator);

	/**
	 * Wait for element invisibility.
	 * @param locator the element locator
	 * @param waitSeconds time to wait in seconds, for element to become invisible
	 */
	void waitForElementInvisibility(String locator, long waitSeconds);
	
	
	/**
	 * Wait for element presence.
	 *
	 * @param locator the element locator
	 * @return the web element
	 */
	WebElement waitForElementPresence(String locator);
	
	/**
	 * Wait for element presence.
	 *
	 * @param locator the locator
	 * @param waitSeconds time to wait in seconds, for element to become present
	 * @return the web element
	 */
	WebElement waitForElementPresence(String locator,long waitSeconds);
	
	/**
	 * Find elements.
	 * 
	 * @param locator the element locator
	 * @return the list
	 */
	List<WebElement> findElements(String locator);

	/**
	 * Sets the value of an input field, as you typed it in. 
	 * 
	 * @param locator the element locator
	 * @param value  the value you want to type in
	 */
	void input(String locator, String value);

	/**
	 * Press on a link, button, check box or radio button.
	 *
	 * @param locator an element locator
	 */
	void press(String locator);
	
	
	/**
	 * Press on a link, button, check box or radio button wait for page to load.
	 *
	 * @param locator an element locator
	 */
	void pressAndWaitForPageToLoad(String locator);

	
	/**
	 * Press and wait for element.
	 *
	 * @param pressLocator the locator of the element you want to perform the press action
	 * @param elementToWaitLocator the locator of the element you wait to appear
	 * @param waitSeconds the time seconds to wait
	 */
	void pressAndWaitForElement(String pressLocator, String elementToWaitLocator,long waitSeconds);
	
	
	/**
	 * Press and wait for element.
	 * 
	 *@param pressLocator the locator of the element you want to perform the press action
	 * @param elementToWaitLocator the locator of the element you wait to appear
	 */
	void pressAndWaitForElement(String pressLocator, String elementToWaitLocator);
	
	/**
	 * Press on a link, button, check box or radio button that generates an alert,
	 * click OK in alert and wait for page to load.
	 *
	 * @param locator an element locator
	 */
	void pressAndClickOkInAlert(String locator);
	
	
	
	/**
	 * Press on a link, button, check box or radio button that generates an alert and
	 * click OK in alert.This action does not cause a new page to load
	 *
	 * @param locator the locator of the element you want to press
	 */
	void pressAndClickOkInAlertNoPageLoad(String locator);
	
	/**
	 * Press on a link, button, check box or radio button that generates an alert and
	 * click cancel in alert.This action does not cause a new page to load
	 *
	 * @param locator the locator of the element you want to press
	 */
	void pressAndClickCancelInAlert(String locator);

	/**
	 * Select an option from a drop-down list.
	 *
	 * @param locator the drop-down list locator
	 * @param option the option
	 */
	void select(String locator, String option);

	
	/**
	 * Select by value.
	 *
	 * @param locator the select locator
	 * @param value the value
	 */
	void selectByValue(String locator, String value);
	
	/**
	 * Multi select add.
	 *
	 * @param locator the locator of the multi selector
	 * @param option the option you want to select
	 */
	void multiSelectAdd(String locator, String option);
	
	/**
	 * Execute javascript.
	 *
	 * @param js the javascript command to execute
	 * @param args the arguments
	 * @return the object
	 */
	Object executeJavascript(String js,Object...args);
	
	
 	/**
	  * Wait for condition.
	  *
	  * @param jscondition the javascript condition
	  */
	 void waitForCondition(String jscondition);
	
 	
 	
 	/**
	  * Wait for condition.
	  *
	  * @param jscondition the javascript condition
	  * @param waitSeconds the time in seconds to wait for the condition
	  */
	 void waitForCondition(String jscondition,long waitSeconds);
	
	
	/**
	 * Clear the value of an input field.
	 *
	 * @param locator the locator of the input field you want to clear
	 */
	void clear(String locator);

	/**
	 * Gets the builder.
	 *
	 * @return an instance of an Actions driver
	 */
	Actions getBuilder();

	/**
	 * Hover.
	 * 
	 * @param locator the locator of the element which you want to perform the hover
	 */
	void mouseOver(String locator);
	
	
	
	/**
	 * Mouse up.This simulates the event that occurs when the user releases the mouse button (i.e., stops holding
	 * the button down) on the specified element
	 *
	 * @param locator the locator of the element you perform the mouse up
	 */
	void mouseUp(String locator);


	/**
	 * Mouse down.This simulates a user pressing the left mouse button (without releasing it yet) on the specified
	 * element.
	 *
	 * @param locator the locator of the element where mouse down action is performed
	 */
	void mouseDown(String locator);
	
	
	/**
	 * Clicks on a link, button, check box or radio button.
	 *
	 * @param locator the locator of the element(i.e link, button, etc) to perform the click action
	 */
	void click(String locator);
	
	
	/**
	 * Double click. Simulates the double click action
	 * 
	 * @param locator locator of the element where double click is performed
	 *           
	 */
	void doubleClick(String locator);

	/**
	 * Highlight. Changes (highlights) the background color of the element
	 * 
	 * @param locator the locator of the element to highlight
	 */
	void highlight(String locator);
	
	
	/**
	 * Highlight. Changes (highlights) the current background color of the element to the one you specify
	 *
	 * @param locator the locator of the element to highlight
	 * @param color the color you want to give to the element background
	 */
	void highlight(String locator,String color);
 
	
	
	/**
	 * Take screen shot.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void takeScreenShot() throws IOException;
	

	/**
	 * Gets the text. This works for any element that contains text.
	 * 
	 * @param locator of the element
	 * @return the text contained in specified element
	 */
	String getText(String locator);
	
	
	/**
	 * Gets the focus. Move the focus to the specified element
	 *
	 * @param locator the locator of the element to focus
	 * @return the focus
	 */
	void getFocus(String locator);
     
	

	/**
	 * Gets the selected label.
	 * 
	 * @param locator of the element
	 *            
	 * @return the selected label
	 */
	String getSelectedOption(String locator);

	/**
	 * Gets the selected options (multiselect element).
	 * 
	 * @param locator the locator of the element
	 *            
	 * @return the select options
	 */
	List<String> getSelectedOptions(String locator);

	/**
	 * Gets the value of an input field.
	 *
	 * @param locator of the input field
	 * @return the value of an input field
	 */
	String getInputValue(String locator);

	
	/**
	 * Checks if is alert present.
	 *
	 * @return true, if is alert present
	 */
	boolean isAlertPresent();
	
	/**
	 * Checks if text is present.
	 * 
	 * @param value the text value you want to check for presence
	 *            
	 * @return true, if text is present
	 */
	boolean isTextPresent(String value);

	/**
	 * Checks if text is not present.
	 * 
	 * @param value the text value you want to check 
	 *  
	 * @return true, if text is not present
	 */
	boolean isTextNotPresent(String value);

	/**
	 * Checks if the specified input element is editable.
	 *
	 * @param locator  the locator of the element
	 * @return true, if is component editable
	 */
	boolean isComponentEditable(String locator);

	/**
	 * Checks if a component disabled. This means that no write/edit actions are allowed
	 * 
	 * @param locator the locator of the element you want to chek
	 *            
	 * @return true, if the component is disabled
	 */
	boolean isComponentDisabled(String locator);

	/**
	 * Checks if a component present in the page (meaning somewhere in the page).
	 *
	 * @param locator the locator of the element you want to verify presence
	 * @return true, if is component present
	 */
	boolean isComponentPresent(String locator);
	
	
	/**
	 * Checks if a component present in the page (meaning somewhere in the page) for at least a specified time.
	 *
	 * @param locator the locator of the element
	 * @param seconds the time in seconds that element needs to be present
	 * @return true, if the component is present for the specified time
	 */
	boolean isComponentPresent(String locator,long seconds);

	/**
	 * Checks if the component is not present in the page (meaning anywhere in the page).
	 *
	 * @param locator the locator of the element
	 * @return true, if the component is not present
	 */
	boolean isComponentNotPresent(String locator);
	

	/**
	 * Determines if the specified element is visible.
	 *
	 * @param locator the locator of the element
	 * @return true, if the component visible
	 */
	boolean isComponentVisible(String locator);
	
	/**
	 * Determines if the specified element is visible.
	 *
	 * @param locator the locator of the element
	 * @param seconds the time in seconds, where element needs to maintain visibility
	 * @return true, if the component is visible for the specified time
	 */
	boolean isComponentVisible(String locator,long seconds);

	/**
	 * Checks if a specified component is not visible.
	 * 
	 * @param locator the locator of the element
	 *        
	 * @return true, if the specified component is not visible
	 */
	boolean isComponentNotVisible(String locator);
	
	
	
	/**
	 * Checks if a component is not visible, for a specified time frame.
	 *
	 * @param locator the locator of the element
	 * @param seconds the time in seconds, where element needs to maintain invisibility
	 * @return true, if the component is not visible for the specified time
	 */
	boolean isComponentNotVisible(String locator,long seconds);

	/**
	 * Checks if a component (check box/radio) is checked (selected).
	 * 
	 * @param locator the locator of the element
	 *             
	 * @return true, if the specified component is selected
	 */
	boolean isComponentSelected(String locator);

	/**
	 * Checks if a component (check box/radio) is not selected.
	 * 
	 * @param locator the locator of the element
	 *            
	 * @return true, if the specified component is not selected
	 */
	boolean isComponentNotSelected(String locator);

	/**
	 * Press link name.
	 * 
	 * @param linkName the name of the link you want to press
	 *            
	 */
	void pressLinkName(String linkName);

	
	/**
	 * Press link name and wait for page to load.
	 *
	 * @param linkName the name of the link you want to press
	 */
	void pressLinkNameAndWaitForPageToLoad(String linkName);
	
	
    /**
     * Press link name and click ok in alert.
     *
     * @param linkName the link name
     */
    void pressLinkNameAndClickOkInAlert(String linkName);
    
    
    /**
     * Press link name and click ok in alert. No new  page is expected load.
     *
     * @param linkName the link name
     */
    void pressLinkNameAndClickOkInAlertNoPageLoad(String linkName);
	/**
	 * Press link name and click cancel in alert.
	 *
	 * @param linkName the link name
	 */
	void pressLinkNameAndClickCancelInAlert(String linkName);
	
	/**
	 * Type keys.Simulates keystroke events on the specified element, as though you typed the value key-by-key.
	 *
	 * @param locator the locator of the element you want to type
	 * @param value the key values to type
	 */
	void typeKeys(String locator,String value);
	
	/**
	 * Key down.Simulates a user pressing a key down
	 *
	 * @param locator the locator of the element
	 * @param thekey the key whose pressing you want to simulate
	 */
	void keyDown(String locator,KeyInfo thekey);
	
	/**
	 * Key up.Simulates a user releasing a key
	 *
	 * @param locator the locator of the element
	 * @param thekey the key to release
	 */
	void keyUp(String locator,KeyInfo thekey); 
	
	
	/**
	 * Key press. Simulates the action of a user to press a key(once) and release it
	 *
	 * @param locator the element locator
	 * @param thekey the key you want to press
	 */
	void keyPress(String locator,KeyInfo thekey);
	
	
    /**
     * Key down.This action simulates a user pressing a key (without releasing it yet) by sending a native operating system
   * keystroke.
     *
     * @param thekey the keys you want to press down
     */
    void keyDown(KeyInfo thekey);
	
	
	/**
	 * Key up. This action simulates a user releasing a key by sending a native operating system
   * keystroke.
	 *
	 * @param thekey the key to release
	 */
	void keyUp(KeyInfo thekey); 
	
	
	/**
	 * Key press. This action simulates a user pressing and releasing a key by sending a native operating system keystroke.
	 *
	 * @param thekey the key to press
	 */
	void keyPress(KeyInfo thekey);
	
	/**
	 * Click OK in an alert pop-up.
	 */
	void clickOkInAlert();

	/**
	 * Prompt input press ok. This action simulates the alerts, that user input is required, before pressing ok
	 *
	 * @param inputMessage the input message to type in the input prompt
	 */
	void promptInputPressOK(String inputMessage);
	
	/**
	 * Prompt input press cancel.  This action simulates the alerts, that user input is required, before pressing cancel
	 *
	 * @param inputMessage the input message to type in the input prompt
	 */
	void promptInputPressCancel(String inputMessage);
	
	/**
	 * Click Cancel in an alert pop-up.
	 */
	void clickCancelInAlert();

	/**
	 * Navigate to a specific url.
	 *
	 * @param url the url you want to navigate to
	 */
	void navigate(String url);
	
	
	/**
	 * Refresh. Simulates the refresh button of the browser
	 */
	void refresh();
	

	/**
	 * Gets the table element row position.
	 * 
	 * @param locator the table
	 * @param elementName the element name you want to find
	 * @return the table element row position
	 */
	String getTableElementRowPosition(String locator, String elementName);
	
	/**
	 * Gets the rows number of a table.
	 *
	 * @param locator the locator of the table
	 * @return the rows number of the table
	 */
	int getNumberOfTotalRows(String locator);
	
	
	/**
	 * Gets the columns number of a table.
	 *
	 * @param locator the locator of the table
	 * @return the columns number of the table
	 */
	int getNumberOfTotalColumns(String locator);
	
	/**
	 * Gets the table info.
	 *
	 * @param locator the locator of the table
	 * @param numberOfColumns the number of table columns
	 * @return the a hash map of the table elements, for each row and column
	 */
	Map<String, Map<String, String>> getTableInfo(String locator, int numberOfColumns);
	

	
	/**
	 * Gets the table info.
	 *
	 * @param locator the locator of the table
	 * @return the a List of List of Strings of the table elements, for each row and column
	 */
	List<List<String>> getTableInfoAsList(String locator);

	
	/**
	 * Gets the table element text for specific header.
	 * 
	 * @param locator the table locator
	 * @param elementName the name of an element that is in the same row with the element you want to find, 
	 * @param headerName the name of the header, under which the element you want to find is
	 * @return the element in the table, under the header you gave, and in the same row with the element you provided
	 */
	String getTableElementTextUnderHeader(String locator, String elementName, String headerName);

	/**
	 * Gets the text element in table for specific row and column.
	 *
	 * @param locator the table locator
	 * @param row the row of the element
	 * @param column the column of the element
	 * @return the text for the specific row and column of the table
	 */
	String getTableElementTextForRowAndColumn(String locator, String row, String column);
	
	
	/**
	 * Gets the table header position.
	 *
	 * @param locator the locator
	 * @param headerName the header name
	 * @return the table header position
	 */
	String getTableHeaderPosition(String locator, String headerName);
	
	
	/**
	 * Gets the table element column position.
	 *
	 * @param locator the locator of the table
	 * @param elementName the element name you want to find
	 * @return the table element column position
	 */
	String getTableElementColumnPosition(String locator, String elementName);
	
	
	/**
	 * Gets the elements of a table, under a specific table header.
	 *
	 * @param locator the table locator
	 * @param headerName the header name of the element under which there are the elements you will get
	 * @return an array of Strings with the requested elements
	 */

	 List<String> getTableRecordsUnderHeader(String locator, String headerName);
	

	
	
	/**
	 * Gets the table elements in a 2-dimensional array.
	 *
	 * @param locator the locator of the two dimensional array
	 * @return a 2-D array with rows and columns
	 */
	String[][] getTableElements2DArray(String locator);
	
	/**
	 * Gets the table element locator, that exists under a specific table header.
	 *
	 * @param locator the table locator
	 * @param elementName the element name, whose locator you want to find
	 * @param headerName the header name under which the element is
	 * @return the table element locator
	 */
	String getTableElementSpecificHeaderLocator(String locator, String elementName, String headerName);
	
	/**
	 * Constructs the locator of an element for specific row and column of a table.
	 *
	 * @param locator the table locator
	 * @param row the row the element is
	 * @param column the column the element is
	 * @return the locator, with the specific row and column embedded
	 */
	String getTableElementSpecificRowAndColumnLocator(String locator, String row, String column);
	
	/**
	 * Gets the value of an element attribute.
	 *
	 * @param locator the element locator
	 * @param attribute the attribute value you want to retrieve
	 * @return the attribute value
	 */
	String getAttributeValue(String locator,String attribute);
	
	
	
	/**
	 * Gets the cookie by name.
	 *
	 * @param name the name
	 * @return an HttpCookie
	 */
	HttpCookie getCookieByName(String name);
	
	
	
	
	/**
	 * Gets the all cookies.
	 *
	 * @return the all cookies
	 */
	List<HttpCookie> getAllCookies();

	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator from drag is performed
	 * @param locatorTo the locator where the drop will take place
	 */
	void dragAndDrop(String locatorFrom, String locatorTo);
	
	/**
	 * Switch to last opened Window.
	 */
	void switchToLatestWindow();
	
	/**
	 * Gets the alert text. Retrieves the message of a JavaScript alert generated during the previous action
	 *
	 * @return the text of alert
	 */
	String getAlertText();
	
	/**
	 * Gets all option labels in the specified select drop-down.
	 *
	 * @param locator the locator of the select drop down
	 * @return all the list options
	 */
	List<String> getAllListOptions(String locator);

	/**
	 * Get Frame using a frame ID. Selects a frame within the current window.
	 *
	 * @param frameID the frame id
	 */
	void selectFrame(String frameID);

	/**
	 * Get Main Frame or Return Back to Main Frame.This means that this action selects either the first frame on the page, or the main document when a page contains
     * iframes
	 */
	void selectFrameMain();
	
	/**
	 * Maximize window. This means that the currently open window is maximized
	 */
	void maximizeWindow();

	/**
	 * Gets the number of elements that match a locator.
	 *
	 * @param locator the element locator
	 * @return the number of elements that match exactly the given locator
	 */
	int getNumberOfElementsMatchLocator(String locator);

	/**
	 * Move to Element and use offset.
	 *
	 * @param locator the locator of the element to move
	 * @param x the x offset to move, from original place
	 * @param y the y offset to move, from original place
	 */
	void moveToElement(String locator, int x, int y);

	/**
	 * Move to an Element.
	 *
	 * @param locator the element locator
	 */
	void moveToElement(String locator);

	/**
	 * Move to Element By offset.
	 *
	 * @param xOffset the x offset to move, from original place
	 * @param yOffset the y offset to move, from original place
	 */
	void moveByOffset(int xOffset, int yOffset);
	

    

     /**
      * Wait for Ajax calls to be completed.
	  * Works only if you're using jQuery for your Ajax requests
      *
      * @param milliseconds the maximum wait time in milliseconds
      */
     void waitForAjaxComplete(long milliseconds);	 
   
	
	/**
	 * Causes the current thread to sleep for a specific number of milliseconds.
	 *
	 * @param milliseconds the time in milliseconds for sleeping
	 */
	void sleep(long milliseconds);
	
	/**
	 * Gets the absolute url of the current page.
	 *
	 * @return the absolute url of the current page
	 */
	String getCurrentUrl();
	

	/**
	 * Drag and drop.
	 *
	 * @param locatorFrom the locator of the element to drag
	 * @param xOffset the x offset to drop
	 * @param yOffset the y offset to drop
	 */
	void dragAndDrop(String locatorFrom, int xOffset, int yOffset);

	/**
	 * Gets the element position.
	 *
	 * @param locator the element locator
	 * @return the element position
	 */
	Point getElementPosition(String locator);

	/**
	 * Get Page HTML Source Code.
	 *
	 * @return the page source
	 */
	String getPageSource() ;
	
}
