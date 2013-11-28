package com.persado.oss.quality.stevia.annotations;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
@Aspect
public class RunsWithControllerProcessor implements ApplicationContextAware {
	public static Logger LOG = LoggerFactory.getLogger(RunsWithControllerProcessor.class);
	
	private static final ThreadLocal<Map<String,WebController>> controllerCache = new ThreadLocal<Map<String,WebController>>() {
		@Override
		protected java.util.Map<String,WebController> initialValue() {
			return new HashMap<String,WebController>();
		};
	};

	private ApplicationContext context;

	@Around(value = "@within(com.persado.oss.quality.stevia.annotations.RunsWithController) || @annotation(com.persado.oss.quality.stevia.annotations.RunsWithController)")
	public Object adviceOnWebController(ProceedingJoinPoint pjp) throws Throwable {
		StopWatch watch = new StopWatch();
		
		WebController masked = null;
		Object retVal = null;
		try {
			RunsWithController rw = pjp.getTarget().getClass().getAnnotation(RunsWithController.class);
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
					masked = currentControllerObj;
					if (cache.containsKey(reqControllerKey)) { // we have one
						replacer = cache.get(reqControllerKey);
					} else {
						replacer = SteviaWebControllerFactory.getWebController(context, requestedControllerClass);
						cache.put(reqControllerKey, replacer);						
						LOG.debug("Controller {} not found in cache, creating new ", reqControllerKey);
					}
					SteviaContext.setWebController(replacer);

				} else {
					LOG.trace("Controller requested is the currently used one. No action");
				}
				watch.stop();
			}
			watch.start("Executing method");
		    retVal = pjp.proceed();
		    watch.stop();
		} finally {
			if (watch.isRunning()) {
				watch.stop();
			}
			watch.start("Controller unmasking");
			if (masked !=null) {
				SteviaContext.setWebController(masked);
			}
			watch.stop();
			LOG.info(watch.prettyPrint());
		}
	    return retVal;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}
}
