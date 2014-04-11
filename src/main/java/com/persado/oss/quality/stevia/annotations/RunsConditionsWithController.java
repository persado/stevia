package com.persado.oss.quality.stevia.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.persado.oss.quality.stevia.selenium.core.WebController;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RunsConditionsWithController {

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
	String[] preConditionMethods() default {};
	
	/**
	 * list of publicly accessible methods for postconditions
	 * @return
	 */
	String[] postConditionMethods() default {};

}
