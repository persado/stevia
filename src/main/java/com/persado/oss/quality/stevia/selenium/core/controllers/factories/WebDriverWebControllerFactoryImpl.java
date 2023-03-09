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
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebDriverWebControllerFactoryImpl implements WebControllerFactory {

	DevTools devTools;
	private static final Logger LOG = LoggerFactory.getLogger(WebDriverWebControllerFactoryImpl.class);

	@Override
	public WebController initialize(ApplicationContext context, WebController controller) {
		Proxy proxy = null;
		WebDriver driver;

		if(SteviaContext.getParam(SteviaWebControllerFactory.PROXY) != null) {//security testing - ZAP
			proxy = new Proxy();
			proxy.setHttpProxy(SteviaContext.getParam(SteviaWebControllerFactory.PROXY));
			proxy.setSslProxy(SteviaContext.getParam(SteviaWebControllerFactory.PROXY));
		}

		WebDriverWebController wdController = (WebDriverWebController) controller;


		if (SteviaContext.getParam(SteviaWebControllerFactory.DEBUGGING).compareTo(SteviaWebControllerFactory.TRUE) == 0) { // debug=on
			if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER) == null || SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("firefox") == 0
					|| SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).isEmpty()) {
				FirefoxOptions options = new FirefoxOptions();

				options.setPageLoadStrategy(PageLoadStrategy.NORMAL);//Default Normal
				if(SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY) != null && !SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY).equals("normal"))
					options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY));

				//security testing - ZAP
				if(proxy != null){options.setCapability("proxy",proxy);}

				LOG.info("Debug enabled, using Firefox and options : "+options);
				driver = new FirefoxDriver(options);
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("chrome") == 0) {
				ChromeOptions options = new ChromeOptions();

				// possible fix for https://code.google.com/p/chromedriver/issues/detail?id=799
				//Ignore certifications - insecure for zap
				options.addArguments("--ignore-certificate-errors");
				options.addArguments("--disable-blink-features=CSSPseudoHas");
				options.addArguments("start-maximized");
				options.addArguments("test-type");
				options.addArguments("--disable-backgrounding-occluded-windows"); //chrome 87 freeze offscreen automation / https://support.google.com/chrome/thread/83911899?hl=en
				options.addArguments("--remote-allow-origins=*");//chrome 111 https://stackoverflow.com/a/75682422

				options.setPageLoadStrategy(PageLoadStrategy.NORMAL);//Default None
				if(SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY) != null && !SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY).equals("normal"))
					options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY));

				//security testing - ZAP
				if(proxy != null){options.setCapability("proxy",proxy);}

				LOG.info("Debug enabled, using ChromeDriver and options : "+options);
				driver = new ChromeDriver(options);
				mockingGeolocation((ChromeDriver) driver);
				simulateDeviceDimension((ChromeDriver) driver);

			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("iexplorer") == 0) {
				InternetExplorerOptions options = new InternetExplorerOptions();
				options.setPageLoadStrategy(PageLoadStrategy.NORMAL);//Default Normal
				if(SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY) != null && !SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY).equals("normal"))
					options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY));

				LOG.info("Debug enabled, using InternetExplorer and options : "+options);
				driver = new InternetExplorerDriver(options);
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("safari") == 0) {
				SafariOptions options = new SafariOptions();
				options.setPageLoadStrategy(PageLoadStrategy.NORMAL);//Default Normal
				if(SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY) != null && !SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY).equals("normal"))
					options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY));

				LOG.info("Debug enabled, using Safari and options : "+options);
				driver = new SafariDriver(options);
			} else {
				throw new IllegalArgumentException(SteviaWebControllerFactory.WRONG_BROWSER_PARAMETER);
			}

		} else { // debug=off
			AbstractDriverOptions browserOptions;

			if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER) == null || SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("firefox") == 0
					|| SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).isEmpty()) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Firefox options");
				browserOptions = new FirefoxOptions();

			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("chrome") == 0) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Chrome options");
				// possible fix for https://code.google.com/p/chromedriver/issues/detail?id=799
				browserOptions = new ChromeOptions();
				((ChromeOptions) browserOptions).addArguments("--ignore-certificate-errors");
				((ChromeOptions) browserOptions).addArguments("--disable-blink-features=CSSPseudoHas");
				((ChromeOptions) browserOptions).addArguments("start-maximized");
				((ChromeOptions) browserOptions).addArguments("test-type");
				((ChromeOptions) browserOptions).addArguments("test-type");
				((ChromeOptions) browserOptions).addArguments("--remote-allow-origins=*");//chrome 111 https://stackoverflow.com/a/75682422

			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("iexplorer") == 0) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Internet Explorer options");
				browserOptions = new InternetExplorerOptions();

			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("safari") == 0) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Safari options");
				browserOptions = new SafariOptions();

			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("opera") == 0) {
				LOG.info("Debug OFF, using a RemoteWebDriver with Opera options");
				browserOptions = new OperaOptions();

			} else {
				throw new IllegalArgumentException(SteviaWebControllerFactory.WRONG_BROWSER_PARAMETER);
			}


			browserOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);//Default Normal
			if(SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY) != null && !SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY).equals("normal"))
				browserOptions.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,SteviaContext.getParam(SteviaWebControllerFactory.LOAD_STRATEGY));

			if(proxy != null){browserOptions.setCapability("proxy",proxy);}
			browserOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			browserOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
			if(SteviaContext.getParam(SteviaWebControllerFactory.BROWSER_VERSION) != null){
				browserOptions.setBrowserVersion(SteviaContext.getParam(SteviaWebControllerFactory.BROWSER_VERSION));
			}
			//Selenoid capabilities
			browserOptions.setCapability("enableVideo", true); //By default enabed Selenoid video
			if(SteviaContext.getParam(SteviaWebControllerFactory.SELENOID_VIDEO) != null){
				browserOptions.setCapability("enableVideo", Boolean.parseBoolean(SteviaContext.getParam(SteviaWebControllerFactory.SELENOID_VIDEO))); //Selenoid video
			}
			browserOptions.setCapability("enableVNC", true); //Selenoid
			browserOptions.setCapability("labels", Map.<String, Object>of( //Selenoid manual session so that we can delete it
					"manual", "true"
			));
			if(SteviaContext.getParam(SteviaWebControllerFactory.SUITE_NAME) != null)
				browserOptions.setCapability("name", SteviaContext.getParam(SteviaWebControllerFactory.SUITE_NAME));


			Augmenter augmenter = new Augmenter(); // adds screenshot capability to a default webdriver.
			try {
				driver = augmenter.augment(new RemoteWebDriver(new URL("http://" + SteviaContext.getParam(SteviaWebControllerFactory.RC_HOST) + ":" + SteviaContext.getParam(SteviaWebControllerFactory.RC_PORT)
						+ "/wd/hub"), browserOptions));
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(e.getMessage(), e);
			}
			LOG.info("Debug OFF, Remote Web Driver options are: "+browserOptions);
		}

		//Comment out due to bug: https://github.com/SeleniumHQ/selenium/issues/10132
//		if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("chrome") == 0) {
//			mockingGeolocation((ChromeDriver) driver);
//			simulateDeviceDimension((ChromeDriver) driver);
//		}

		if (SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL) != null) {
			driver.get(SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL));
		}

//		driver.manage().window().maximize();
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



	/*
		Use for Testing the location-based functionality of applications such as different offers, currencies, taxation rules,
		freight charges and date/time format for various geolocations.

		Optional keys -> need values if key is specified
	 */
	private void mockingGeolocation(ChromeDriver driver) {
		if (SteviaContext.getParam(SteviaWebControllerFactory.LATITUDE) != null) {
			Map location = new HashMap();
			location.put("latitude",Double.parseDouble(SteviaContext.getParam(SteviaWebControllerFactory.LATITUDE)));
			if (SteviaContext.getParam(SteviaWebControllerFactory.LONGITUDE) != null)
				location.put("longitude",Double.parseDouble(SteviaContext.getParam(SteviaWebControllerFactory.LONGITUDE)));
			if (SteviaContext.getParam(SteviaWebControllerFactory.ACCURACY) != null)
				location.put("accuracy",Double.parseDouble(SteviaContext.getParam(SteviaWebControllerFactory.ACCURACY)));

			DevTools devTools = driver.getDevTools();
			devTools.createSession();

			try {
				driver.executeCdpCommand("Emulation.setGeolocationOverride", location);
				LOG.info("MOCKING GEOLOCATION BY DEV TOOL:  with params -> latitude: {},longitude: {},accuracy: {}", SteviaContext.getParam(SteviaWebControllerFactory.LATITUDE), SteviaContext.getParam(SteviaWebControllerFactory.LONGITUDE), SteviaContext.getParam(SteviaWebControllerFactory.ACCURACY));
			}catch (InvalidArgumentException e){
				LOG.error("SIMULATE DEVICE BY DEV TOOL NOT SET:  Invalid parameters values for -> latitude: {},longitude: {},accuracy: {}", SteviaContext.getParam(SteviaWebControllerFactory.LATITUDE), SteviaContext.getParam(SteviaWebControllerFactory.LONGITUDE), SteviaContext.getParam(SteviaWebControllerFactory.ACCURACY));
			}
		}
	}

	/*
		Overrides the values of device screen dimensions (window.screen.width, window.screen.height,
		window.innerWidth, window.innerHeight, and "device-width"/"device-height"-related CSS media query results).

		Mandatory keys - pass 0 to disable override
 	*/
	private void simulateDeviceDimension(ChromeDriver driver){
		if(SteviaContext.getParam(SteviaWebControllerFactory.WIDTH) != null) {
			Map metrics = new HashMap();
			metrics.put("width", Integer.parseInt(SteviaContext.getParam(SteviaWebControllerFactory.WIDTH)));
			if(SteviaContext.getParam(SteviaWebControllerFactory.HEIGHT) != null)
				metrics.put("height", Integer.parseInt(SteviaContext.getParam(SteviaWebControllerFactory.HEIGHT)));
			if(SteviaContext.getParam(SteviaWebControllerFactory.DEVICE_TYPE) != null)
				metrics.put("mobile", Boolean.parseBoolean(SteviaContext.getParam(SteviaWebControllerFactory.DEVICE_TYPE)));
			if(SteviaContext.getParam(SteviaWebControllerFactory.DEVICE_SCALE_FACTOR) != null)
				metrics.put("deviceScaleFactor", Integer.parseInt(SteviaContext.getParam(SteviaWebControllerFactory.DEVICE_SCALE_FACTOR)));

			if (devTools == null) {
				devTools = driver.getDevTools();
				devTools.createSession();
			}

			try {
				driver.executeCdpCommand("Emulation.setDeviceMetricsOverride", metrics);
				LOG.info("SIMULATE DEVICE BY DEV TOOL:  with params : {},{},{},{}", SteviaContext.getParam(SteviaWebControllerFactory.WIDTH), SteviaContext.getParam(SteviaWebControllerFactory.HEIGHT), SteviaContext.getParam(SteviaWebControllerFactory.DEVICE_TYPE),SteviaContext.getParam(SteviaWebControllerFactory.DEVICE_SCALE_FACTOR));
			} catch (InvalidArgumentException e) {
				LOG.error("SIMULATE DEVICE BY DEV TOOL NOT SET:  Invalid parameters for simulate DEVICE, you need the mandatory ones: {},{},{},{}", "width", "height", "mobile", "deviceScaleFactor");
			}
		}
	}
}
