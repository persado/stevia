package com.persado.oss.quality.stevia.selenium.core.controllers;

import org.springframework.context.ApplicationContext;

import com.persado.oss.quality.stevia.selenium.core.WebController;

interface WebControllerFactory {

	public WebController initialize(ApplicationContext context, WebController controller);
	
}
