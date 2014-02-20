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

import org.springframework.context.ApplicationContext;
import java.lang.Exception;
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

		try {
			selenium.start();
			// selenium.windowMaximize();
			selenium.open("");
		} catch (Exception e) {
			SteviaWebControllerFactory.LOG.warn("Exception caught while trying to start Selenium RC - did you start an RC server?");
			SteviaWebControllerFactory.LOG.warn("Exception message : "+e.getMessage(),e);
			throw new IllegalStateException("Failed to start Selenium RC",e);
		}
		selController.setSelenium(selenium);

		return controller;
	}

	@Override
	public String getBeanName() {
		return "seleniumController";
	}
}
