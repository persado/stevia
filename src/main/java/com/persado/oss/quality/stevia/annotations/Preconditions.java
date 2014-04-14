package com.persado.oss.quality.stevia.annotations;

import com.persado.oss.quality.stevia.selenium.core.WebController;

public @interface Preconditions {
	/**
	 * Controller to use for executing the methods in this list. 
	 * Optional, do not use if the same controller applies for preconditions
	 * @return
	 */
	Class<? extends WebController> controller() default VoidController.class;
	
	/**
	 * list of publicly accessible methods for preconditions
	 * @return 
	 */
	String[] value() default {};
}
