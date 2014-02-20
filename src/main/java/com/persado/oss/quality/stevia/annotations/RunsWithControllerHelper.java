package com.persado.oss.quality.stevia.annotations;

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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.SteviaWebControllerFactory;

@Component
public class RunsWithControllerHelper implements ApplicationContextAware {
	public static Logger LOG = LoggerFactory.getLogger(RunsWithControllerHelper.class);
	
	private static final ThreadLocal<Map<String,WebController>> controllerCache = new ThreadLocal<Map<String,WebController>>() {
		@Override
		protected java.util.Map<String,WebController> initialValue() {
			return new HashMap<String,WebController>();
		};
	};
	
	private static final Map<String, WebController> ownControllers = new HashMap<String, WebController>();

	public static void tearDown() {
		for (WebController controller : ownControllers.values()) {
			LOG.info("Removing {} in teardown",controller);
			controller.close();
		}
		ownControllers.clear();
		controllerCache.get().clear();
	}
	
	private ApplicationContext context;
	
	
	public void maskExistingController(Method m) throws Throwable {
		StopWatch watch = new StopWatch();
		try {
			
			RunsWithController rw = 
					(m.getDeclaringClass().getAnnotation(RunsWithController.class) != null) ? 
								m.getDeclaringClass().getAnnotation(RunsWithController.class) : 
								m.getAnnotation(RunsWithController.class);
			if (null != rw) {
				watch.start("Controller masking");
				Class<? extends WebController> requestedControllerClass = rw.controller();
				WebController currentControllerObj = SteviaContext.getWebController();
				Class<? extends WebController> currentControllerClass = currentControllerObj.getClass();
				Map<String, WebController> cache = controllerCache.get();


				String curControllerKey = currentControllerClass.getCanonicalName();
				String reqControllerKey = requestedControllerClass.getCanonicalName();

				//check if running controller is already cached.				
				if (!cache.containsKey(curControllerKey) ) {
					cache.put(curControllerKey,currentControllerObj);
				}
				
				//check if requested controller is different from the currently running
				if (!curControllerKey.startsWith(reqControllerKey)) {
					WebController replacer = null;
					if (cache.containsKey(reqControllerKey)) { // we have one
						replacer = cache.get(reqControllerKey);
					} else {
						replacer = SteviaWebControllerFactory.getWebController(context, requestedControllerClass);
						cache.put(reqControllerKey, replacer);			
						ownControllers.put(reqControllerKey, replacer);
						LOG.debug("Controller {} not found in cache, creating new ", reqControllerKey);
					}
					SteviaContext.setWebController(replacer);
					cache.put("masked", currentControllerObj); //save for later

				} else {
					LOG.trace("Controller requested is the currently used one. No action");
				}
				watch.stop();
			} else {
				throw new IllegalStateException("unable to find an entry for the annotation!");
			}
		} finally {
			if (watch.isRunning()) {
				watch.stop();
			}
			LOG.info(watch.prettyPrint());
		}
	}
	public void revertToOriginalController() throws Throwable {
		Map<String, WebController> cache = controllerCache.get();

		if (cache.containsKey("masked")) {
			LOG.trace("Controller unmasked");
			SteviaContext.setWebController(cache.remove("masked"));
		}
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}
}
