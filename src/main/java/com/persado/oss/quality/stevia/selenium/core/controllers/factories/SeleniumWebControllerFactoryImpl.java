package com.persado.oss.quality.stevia.selenium.core.controllers.factories;

import org.springframework.context.ApplicationContext;

import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.SeleniumWebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.SteviaWebControllerFactory;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class SeleniumWebControllerFactoryImpl implements WebControllerFactory {

	public WebController initialize(ApplicationContext context, WebController controller) {

		SeleniumWebController selController = (SeleniumWebController) controller;

		SteviaWebControllerFactory.LOG.info("Selenium RC mode; connecting to a Selenium RC host");
		Selenium selenium = null;
		if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER) == null || SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("firefox") == 0
				|| SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).isEmpty()) {
			SteviaWebControllerFactory.LOG.info("Using Firefox with selenium RC");
			selenium = new DefaultSelenium(SteviaContext.getParam(SteviaWebControllerFactory.RC_HOST), Integer.parseInt(SteviaContext.getParam(SteviaWebControllerFactory.RC_PORT)), SteviaWebControllerFactory.FIREFOX,
					SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL));
		} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("chrome") == 0) {
			SteviaWebControllerFactory.LOG.info("Using Chrome with selenium RC");
			selenium = new DefaultSelenium(SteviaContext.getParam(SteviaWebControllerFactory.RC_HOST), Integer.parseInt(SteviaContext.getParam(SteviaWebControllerFactory.RC_PORT)), SteviaWebControllerFactory.CHROME,
					SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL));
		} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("iexplorer") == 0) {
			SteviaWebControllerFactory.LOG.info("Using Chrome with selenium RC");
			selenium = new DefaultSelenium(SteviaContext.getParam(SteviaWebControllerFactory.RC_HOST), Integer.parseInt(SteviaContext.getParam(SteviaWebControllerFactory.RC_PORT)), SteviaWebControllerFactory.IEXPLORER,
					SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL));
		} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("safari") == 0) {
			SteviaWebControllerFactory.LOG.info("Using Safari with selenium RC");
			selenium = new DefaultSelenium(SteviaContext.getParam(SteviaWebControllerFactory.RC_HOST), Integer.parseInt(SteviaContext.getParam(SteviaWebControllerFactory.RC_PORT)), SteviaWebControllerFactory.SAFARI,
					SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL));
		} else if (SteviaContext.getParam(SteviaWebControllerFactory.BROWSER).compareTo("opera") == 0) {
			SteviaWebControllerFactory.LOG.info("Using Opera with selenium RC");
			selenium = new DefaultSelenium(SteviaContext.getParam(SteviaWebControllerFactory.RC_HOST), Integer.parseInt(SteviaContext.getParam(SteviaWebControllerFactory.RC_PORT)), SteviaWebControllerFactory.OPERA,
					SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL));
		} else {
			throw new IllegalArgumentException(SteviaWebControllerFactory.WRONG_BROWSER_PARAMETER);
		}
		selenium.start();
		// selenium.windowMaximize();
		selenium.open("");

		selController.setSelenium(selenium);

		return controller;
	}

	@Override
	public String getBeanName() {
		return "seleniumController";
	}
}