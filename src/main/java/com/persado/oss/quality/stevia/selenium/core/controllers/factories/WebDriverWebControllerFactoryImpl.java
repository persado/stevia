package com.persado.oss.quality.stevia.selenium.core.controllers.factories;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.context.ApplicationContext;

import com.opera.core.systems.OperaDriver;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.SteviaWebControllerFactory;
import com.persado.oss.quality.stevia.selenium.core.controllers.WebDriverWebController;

public class WebDriverWebControllerFactoryImpl implements WebControllerFactory {
	@Override
	public WebController initialize(ApplicationContext context, WebController controller) {
		WebDriverWebController wdController = (WebDriverWebController) controller;
		WebDriver driver = null;
		if (SteviaContext.getParam(SteviaWebControllerFactory.DEBUGGING).compareTo(SteviaWebControllerFactory.TRUE) == 0) { // debug=on
			if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER) == null || SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("firefox") == 0
					|| SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).isEmpty()) {
				if (SteviaContext.getParam(SteviaWebControllerFactory.PROFILE) == null || SteviaContext.getParam(SteviaWebControllerFactory.PROFILE).isEmpty()) {
					SteviaWebControllerFactory.LOG.info("Debug enabled, using Firefox Driver");
					driver = new FirefoxDriver();
				} else {
					SteviaWebControllerFactory.LOG.info("Debug enabled, using a local Firefox profile with FirefoxDriver");
					ProfilesIni allProfiles = new ProfilesIni();
					FirefoxProfile ffProfile = allProfiles.getProfile(SteviaWebControllerFactory.PROFILE);
					driver = new FirefoxDriver(ffProfile);
				}
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("chrome") == 0) {
				SteviaWebControllerFactory.LOG.info("Debug enabled, using ChromeDriver");
				driver = new ChromeDriver();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("iexplorer") == 0) {
				SteviaWebControllerFactory.LOG.info("Debug enabled, using InternetExplorerDriver");
				driver = new InternetExplorerDriver();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("safari") == 0) {
				SteviaWebControllerFactory.LOG.info("Debug enabled, using SafariDriver");
				driver = new SafariDriver();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("opera") == 0) {
				SteviaWebControllerFactory.LOG.info("Debug enabled, using OperaDriver");
				driver = new OperaDriver();
			} else {
				throw new IllegalArgumentException(SteviaWebControllerFactory.WRONG_BROWSER_PARAMETER);
			}

		} else { // debug=off
			DesiredCapabilities capability = new DesiredCapabilities();
			if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER) == null || SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("firefox") == 0
					|| SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).isEmpty()) {
				SteviaWebControllerFactory.LOG.info("Debug OFF, using a RemoteWebDriver with Firefox capabilities");
				capability = DesiredCapabilities.firefox();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("chrome") == 0) {
				SteviaWebControllerFactory.LOG.info("Debug OFF, using a RemoteWebDriver with Chrome capabilities");
				capability = DesiredCapabilities.chrome();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("iexplorer") == 0) {
				SteviaWebControllerFactory.LOG.info("Debug OFF, using a RemoteWebDriver with Internet Explorer capabilities");
				capability = DesiredCapabilities.internetExplorer();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("safari") == 0) {
				SteviaWebControllerFactory.LOG.info("Debug OFF, using a RemoteWebDriver with Safari capabilities");
				capability = DesiredCapabilities.safari();
			} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("opera") == 0) {
				SteviaWebControllerFactory.LOG.info("Debug OFF, using a RemoteWebDriver with Opera capabilities");
				capability = DesiredCapabilities.opera();
			} else {
				throw new IllegalArgumentException(SteviaWebControllerFactory.WRONG_BROWSER_PARAMETER);
			}
			Augmenter augmenter = new Augmenter(); // adds screenshot
													// capability
													// to a default web
													// driver.

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