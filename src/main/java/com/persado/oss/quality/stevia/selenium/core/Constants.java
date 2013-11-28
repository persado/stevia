package com.persado.oss.quality.stevia.selenium.core;

public interface Constants {

	/** The Constant FIREFOX. */
	public static final String FIREFOX = "*firefox";
	
	/** The Constant CHROME. */
	public static final String CHROME = "*googlechrome";
	
	/** The Constant IEXPLORER. */
	public static final String IEXPLORER = "*iexplore";
	
	/** The Constant SAFARI. */
	public static final String SAFARI = "*safari";
	
	/** The Constant OPERA. */
	public static final String OPERA = "*opera";
	
	/** The parameter that determines the browser type. */
	public static final String BROWSER = "browser";
	
	/** The parameter that determines the Selenium RC host. */
	public static final String RC_HOST = "rcHost";
	
	/** The parameter that determines the Selenium RC port. */
	public static final String RC_PORT = "rcPort";
	
	/** The parameter that determines the application URL. */
	public static final String TARGET_HOST_URL = "targetHostUrl";
	
	/** The parameter that determines the driver type (WebDriver or Selenium RC). */
	public static final String DRIVER_TYPE = "driverType";
	
	/** The parameter that determines the test mode. */
	public static final String DEBUGGING = "debugging";
	
	/** The parameter that determines if actions will be logged in TestNG report. */
	public static final String ACTIONS_LOGGING = "actionsLogging";
	
	/** The parameter that determines the browser profile. */
	public static final String PROFILE = "profile";
	
	public static final String WRONG_BROWSER_PARAMETER = "Wrong value for 'browser' parameter was defined";
	
	public static final String TRUE = "true";

	
	
}
