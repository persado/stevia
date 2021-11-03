package com.persado.oss.quality.stevia.pageObjects;

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


import com.persado.oss.quality.stevia.selenium.core.WebComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class GoogleHomePage extends WebComponent{

	private static Logger LOG = LoggerFactory.getLogger(GoogleHomePage.class);

	/*
	 * Declare page elements (Buttons, Input fields etc)
	 * in the form of enumeration
	 */
	public enum GoogleHomePageLocators {
		
		INPUT_GOOGLE_SEARCH("search_form_input_homepage"),
		INPUT_USER("css=input#user_email"),
		INPUT_PWD("css=div.form-inputs inputnth(1)"),
		BUTTON_SIGN_IN("name=commit"),
		DASHBOARD("css=h1:contains('Dashboard')"),

		BTN_GOOGLE_SEARCH("search_button_homepage"),
		;
		
	private String myLocator;

	GoogleHomePageLocators(String locator) {
		myLocator = locator;
	}

	public String get() {
		return myLocator;
	}

	public String getWithParams(Object... params) {
		return MessageFormat.format(myLocator, params);
	}
}	
	/*
	 * Input text to search for on Google home page
	 */
	public void inputSearchText(String desiredText){
		controller().input(GoogleHomePageLocators.INPUT_GOOGLE_SEARCH.get(), desiredText);
	}

	public void inputUser(String desiredText){
		LOG.info("Element Username is visible: "+(controller().isComponentVisible(GoogleHomePageLocators.INPUT_USER.get())));
		controller().input(GoogleHomePageLocators.INPUT_USER.get(), desiredText);
	}

	public void inputPwd(String desiredText){
		LOG.info("Element Username is visible: "+(controller().isComponentVisible(GoogleHomePageLocators.INPUT_USER.get())));
		controller().input(GoogleHomePageLocators.INPUT_PWD.get(), desiredText);
	}

	public void pressSignIn(){
		controller().isComponentVisible(GoogleHomePageLocators.BUTTON_SIGN_IN.get());
		controller().pressAndWaitForElement(GoogleHomePageLocators.BUTTON_SIGN_IN.get(),GoogleHomePageLocators.DASHBOARD.get());
	}
	
	/*
	 * Press button Search on Google home page
	 */
	public void pressGoogleSearchButton(){
		controller().press(GoogleHomePageLocators.BTN_GOOGLE_SEARCH.get());
	}
	
	
}
