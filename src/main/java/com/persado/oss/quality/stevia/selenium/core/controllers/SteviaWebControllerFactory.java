package com.persado.oss.quality.stevia.selenium.core.controllers;

import java.net.MalformedURLException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.persado.oss.quality.stevia.selenium.core.Constants;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.factories.WebControllerFactory;
import com.persado.oss.quality.stevia.selenium.core.controllers.registry.DriverRegistry;

public final class SteviaWebControllerFactory implements Constants {

	public static final Logger LOG = LoggerFactory.getLogger(SteviaWebControllerFactory.class);

	public static WebController getWebController(ApplicationContext context) throws MalformedURLException {
		WebController controller = null;
		
		String driverType = SteviaContext.getParam(DRIVER_TYPE);
		
		@SuppressWarnings("unchecked")
		DriverRegistry<String, WebControllerFactory> registry  = (DriverRegistry<String, WebControllerFactory>)context.getBean("steviaDriverRegistry");
		
		WebControllerFactory factory = registry.getMappings().get(driverType);
		Assert.notNull(factory,"Cannot find a factory for "+driverType);
		
		controller = (WebController) context.getBean(factory.getBeanName());
		factory.initialize(context, controller);
		return controller;
	}

	public static WebController getWebController(ApplicationContext context, Class<? extends WebController> requestedControllerClass) {
		WebController controller = context.getBean(requestedControllerClass);
		String[] namesForType = context.getBeanNamesForType(requestedControllerClass);
		Assert.isTrue(namesForType.length == 1, 
				"The requested controller "+requestedControllerClass.getName()+" is mapped with more than one names in the spring context!");
		
		@SuppressWarnings("unchecked")
		DriverRegistry<String, WebControllerFactory> registry  = (DriverRegistry<String, WebControllerFactory>)context.getBean("steviaDriverRegistry");
		
		
		for (Entry<String, WebControllerFactory> entry :  registry.getMappings().entrySet()) {
			if (entry.getValue().getBeanName().equalsIgnoreCase(namesForType[0])) {
				LOG.info("Factory from class detected : {}",entry.getKey());
				WebControllerFactory factory = entry.getValue();
				factory.initialize(context, controller);
				return controller;
			}
		}
		throw new IllegalStateException("Could not locate a factory for controller "+requestedControllerClass.getName()+" in this spring context!");
	}

   

}
