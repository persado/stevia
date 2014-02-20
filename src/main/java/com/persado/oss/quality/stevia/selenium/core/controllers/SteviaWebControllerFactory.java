package com.persado.oss.quality.stevia.selenium.core.controllers;

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
