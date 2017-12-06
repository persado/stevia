package com.persado.oss.quality.stevia.selenium.core.controllers.factories;

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

import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.SteviaWebControllerFactory;
import com.persado.oss.quality.stevia.selenium.core.controllers.WebDriverWebController;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;

public class WebDriverWebControllerFactoryImpl implements WebControllerFactory {
	private static final Logger LOG = LoggerFactory.getLogger(WebDriverWebControllerFactoryImpl.class);
	
	@Override
	public WebController initialize(ApplicationContext context, WebController controller) {
		WebDriverWebController wdController = (WebDriverWebController) controller;
		WebDriver driver = null;
		if (SteviaContext.getParam(SteviaWebControllerFactory.DEBUGGING).compareTo(SteviaWebControllerFactory.TRUE) == 0) { // debug=on
			if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER) == null || SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("firefox") == 0
					|| SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).isEmpty()) {
				LOG.info("Debug enabled, using Firefox Driver");
				driver = new FirefoxDriver();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("chrome") == 0) {
				LOG.info("Debug enabled, using ChromeDriver");
				// possible fix for https://code.google.com/p/chromedriver/issues/detail?id=799
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			    ChromeOptions options = new ChromeOptions();
				options.addArguments("start-maximized");
				options.addArguments("test-type");
			    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				driver = new ChromeDriver(capabilities);
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("iexplorer") == 0) {
				LOG.info("Debug enabled, using InternetExplorerDriver");
				driver = new InternetExplorerDriver();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("safari") == 0) {
				LOG.info("Debug enabled, using SafariDriver");
				driver = new SafariDriver();
			} else {
				throw new IllegalArgumentException(SteviaWebControllerFactory.WRONG_BROWSER_PARAMETER);
			}

		} else { // debug=off
			DesiredCapabilities capability = new DesiredCapabilities();
			if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER) == null || SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("firefox") == 0
					|| SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).isEmpty()) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Firefox capabilities");
				capability = DesiredCapabilities.firefox();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("chrome") == 0) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Chrome capabilities");
				// possible fix for https://code.google.com/p/chromedriver/issues/detail?id=799
				capability = DesiredCapabilities.chrome();
			    ChromeOptions options = new ChromeOptions();
				options.addArguments("start-maximized");
			    options.addArguments("test-type");
			    capability.setCapability(ChromeOptions.CAPABILITY, options);
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("iexplorer") == 0) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Internet Explorer capabilities");
				capability = DesiredCapabilities.internetExplorer();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("safari") == 0) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Safari capabilities");
				capability = DesiredCapabilities.safari();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("opera") == 0) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Opera capabilities");
				capability = DesiredCapabilities.opera();
			} else {
				throw new IllegalArgumentException(SteviaWebControllerFactory.WRONG_BROWSER_PARAMETER);
			}
			Augmenter augmenter = new Augmenter(); // adds screenshot capability to a default webdriver.
			try {
				driver = augmenter.augment(new RemoteWebDriver(new URL("http://" + SteviaContext.getParam(SteviaWebControllerFactory.RC_HOST) + ":" + SteviaContext.getParam(SteviaWebControllerFactory.RC_PORT)
						+ "/wd/hub"), capability));
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(e.getMessage(), e);
			}

		}

		if (SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL) != null) {
			driver.get(SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL));
		}
		// driver.manage().window().maximize();
		wdController.setDriver(driver);
		if (SteviaContext.getParam(SteviaWebControllerFactory.ACTIONS_LOGGING).compareTo(SteviaWebControllerFactory.TRUE) == 0) {
			wdController.enableActionsLogging();
		}
		return wdController;
	}

	@Override
	public String getBeanName() {
		return "webDriverController";
	}

}
