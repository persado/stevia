package com.persado.oss.quality.stevia.selenium.core.controllers.factories;

import org.springframework.context.ApplicationContext;

import com.persado.oss.quality.stevia.selenium.core.WebController;

public interface WebControllerFactory {

	/**
	 * initializes a WebController using the spring context provided and the 
	 * web controller bean.
	 * 
	 * @param context
	 * @param controller
	 * @return the returned bean is the provided, augmented with additional functionality
	 */
	public WebController initialize(ApplicationContext context, WebController controller);
	
	/**
	 * returns the name of bean as registered in the @id attribute in the stevia context <bean .../> construct.
	 * @return
	 */
	public String getBeanName();
	
}
