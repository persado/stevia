/**
 * Copyright (c) 2013, Persado Intellectual Property Limited. All rights
 * reserved.
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
 * 
 * 
 */
package com.persado.oss.quality.stevia.spring;

import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.opera.core.systems.OperaDriver;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.SeleniumWebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.WebDriverWebController;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;



/**
 * The Class SteviaTestBase.
 */
@ContextConfiguration({ "classpath:META-INF/spring/stevia-boot-context.xml" })
public class SteviaTestBase extends AbstractTestNGSpringContextTests {

	/** The Constant FIREFOX. */
	private static final String FIREFOX = "*firefox";
	
	/** The Constant CHROME. */
	private static final String CHROME = "*googlechrome";
	
	/** The Constant IEXPLORER. */
	private static final String IEXPLORER = "*iexplore";
	
	/** The Constant SAFARI. */
	private static final String SAFARI = "*safari";
	
	/** The Constant OPERA. */
	private static final String OPERA = "*opera";
	
	/** The parameter that determines the browser type. */
	private static final String BROWSER = "browser";
	
	/** The parameter that determines the Selenium RC host. */
	private static final String RC_HOST = "rcHost";
	
	/** The parameter that determines the Selenium RC port. */
	private static final String RC_PORT = "rcPort";
	
	/** The parameter that determines the application URL. */
	private static final String TARGET_HOST_URL = "targetHostUrl";
	
	/** The parameter that determines the driver type (WebDriver or Selenium RC). */
	private static final String DRIVER_TYPE = "driverType";
	
	/** The parameter that determines the test mode. */
	private static final String DEBUGGING = "debugging";
	
	/** The parameter that determines if actions will be logged in TestNG report. */
	private static final String ACTIONS_LOGGING = "actionsLogging";
	
	/** The parameter that determines the browser profile. */
	private static final String PROFILE = "profile";
	
	private static final String WRONG_BROWSER_PARAMETER = "Wrong value for 'browser' parameter was defined";
	
	/** The Constant STEVIA_TEST_BASE_LOG. */
	private static final Logger STEVIA_TEST_BASE_LOG = LoggerFactory.getLogger(SteviaTestBase.class);
	
	/** The selenium server. */
	private static SeleniumServer seleniumServer;
	
	/** Determined if RC server is started programmatically. */
	private static boolean isRCStarted = false; 

	/** suite-global output directory. */
	private static String suiteOutputDir;

	private static final String TRUE = "true";
	
	
	/**
	 * Extends the TestNG method to prepare the Spring contexts for parallel tests.
	 * As seen at {@link http://goo.gl/g8QT2}
	 *
	 * @throws Exception the exception
	 */
	@BeforeSuite(alwaysRun = true)
	@BeforeClass(alwaysRun = true)
	@BeforeTest(alwaysRun = true)
	@Override
	protected void springTestContextPrepareTestInstance() throws Exception {
		super.springTestContextPrepareTestInstance();
	} 
	
	
	/**
	 * Start rc server.
	 *
	 * @param rcHost the rc host
	 * @param rcPort the rc port
	 * @param targetHostUrl the target host url
	 * @param driverType the driver type
	 * @param debugging the debugging
	 * @param testContext the test context
	 * @throws Exception the exception
	 */
	@BeforeSuite(alwaysRun = true)
	@Parameters({BROWSER,RC_HOST,RC_PORT, TARGET_HOST_URL, DRIVER_TYPE, DEBUGGING,ACTIONS_LOGGING,PROFILE})
	protected void configureSuiteSettings(@Optional String browser,
			String rcHost, String rcPort, @Optional String targetHostUrl,
			String driverType, String debugging, String actionsLogging,
			@Optional String profile, ITestContext testContext)
			throws Exception {		
		//if the suite needs RC server, we start it here 
		if (driverType.compareTo("webdriver") != 0 && debugging.compareTo(TRUE)==0 && !isRCStarted){
			startRCServer();
		}
		setSuiteOutputDir(testContext.getSuite().getOutputDirectory());
		
		//stevia context init

		STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
		STEVIA_TEST_BASE_LOG.warn("*** SUITE initialisation phase                                                    ***");
		STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
		initializeDriver(browser,rcHost, rcPort, targetHostUrl, driverType, debugging, actionsLogging,profile);
		// user code
		suiteInitialisation(testContext);
		//stevia context clean
		SteviaContext.clean();
	}

	/**
	 * Suite-Level initialisation callback; this method should be overrriden to 
	 * allow suite-level configuration to happen - preferrably at the Base class
	 * of the tests (overriden versions of this method will be called from the class 
	 * extending this Base, at Suite initialisation, best place for this method 
         * to be overriden is at the class extending this Base class).
	 * 
	 * @param context test context
	 */
	protected void suiteInitialisation(ITestContext context) {
		STEVIA_TEST_BASE_LOG.warn("suiteInitialisation method not overriden; doing nothing at suite level.");
	}

	/**
	 * Before test.
	 *
	 * @param browser the browser
	 * @param rcHost the rc host
	 * @param rcPort the rc port
	 * @param targetHostUrl the target host url
	 * @param driverType the driver type
	 * @param debugging the debugging
	 * @param actionsLogging the actions logging
	 * @param profile the profile
	 * @param testContext the test context
	 * @throws Exception the exception
	 */
	@BeforeTest(alwaysRun = true)
	@Parameters({BROWSER,RC_HOST,RC_PORT, TARGET_HOST_URL, DRIVER_TYPE, DEBUGGING,ACTIONS_LOGGING,PROFILE})
	protected void beforeTest(@Optional String browser, String rcHost,
			String rcPort, @Optional String targetHostUrl, String driverType,
			String debugging, String actionsLogging, @Optional String profile,
			ITestContext testContext) throws Exception {
		// we check here **again** if the test needs the RC server and start it.
		if (driverType.compareTo("webdriver") != 0 && debugging.compareTo(TRUE)==0 && !isRCStarted){
			startRCServer();
		}
		String parallelSetup = testContext.getSuite().getParallel();
		if (parallelSetup == null || parallelSetup.isEmpty() || parallelSetup.equalsIgnoreCase("false") || parallelSetup.equalsIgnoreCase("tests")) {

			STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
			STEVIA_TEST_BASE_LOG.warn("*** Driver initialisation phase, current parallel level is @BeforeTest            ***");
			STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
			
			initializeDriver(browser,rcHost, rcPort, targetHostUrl, driverType, debugging, actionsLogging,profile);
		}
	}
	
	/**
	 * Before class.
	 *
	 * @param browser the browser
	 * @param rcHost the rc host
	 * @param rcPort the rc port
	 * @param targetHostUrl the target host url
	 * @param driverType the driver type
	 * @param debugging the debugging
	 * @param actionsLogging the actions logging
	 * @param profile the profile
	 * @param testContext the test context
	 * @throws Exception the exception
	 */
	@BeforeClass(alwaysRun = true)
	@Parameters({BROWSER,RC_HOST,RC_PORT, TARGET_HOST_URL, DRIVER_TYPE, DEBUGGING,ACTIONS_LOGGING,PROFILE})
	protected void beforeClass(@Optional String browser,String rcHost, String rcPort,@Optional String targetHostUrl,String driverType, String debugging, String actionsLogging,@Optional String profile,ITestContext testContext) throws Exception {
		if (testContext.getSuite().getParallel().equalsIgnoreCase("classes")) {

			STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
			STEVIA_TEST_BASE_LOG.warn("*** Driver initialisation phase, current parallel level is @BeforeClass**************");
			STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
			
			initializeDriver(browser,rcHost, rcPort, targetHostUrl, driverType, debugging, actionsLogging,profile);
		}
	}
	
	/**
	 * Before method.
	 *
	 * @param browser the browser
	 * @param rcHost the rc host
	 * @param rcPort the rc port
	 * @param targetHostUrl the target host url
	 * @param driverType the driver type
	 * @param debugging the debugging
	 * @param actionsLogging the actions logging
	 * @param profile the profile
	 * @param testContext the test context
	 * @throws Exception the exception
	 */
	@BeforeMethod(alwaysRun = true)
	@Parameters({BROWSER,RC_HOST,RC_PORT, TARGET_HOST_URL, DRIVER_TYPE, DEBUGGING,ACTIONS_LOGGING,PROFILE})
	protected void beforeMethod(@Optional String browser,String rcHost, String rcPort,@Optional String targetHostUrl,String driverType, String debugging, String actionsLogging,@Optional String profile,ITestContext testContext) throws Exception {
		if (testContext.getSuite().getParallel().equalsIgnoreCase("methods")) {

			STEVIA_TEST_BASE_LOG.warn("***************************************************************************************");
			STEVIA_TEST_BASE_LOG.warn("*** Driver initialisation phase, current parallel level is @BeforeMethod[PANICMODE] ***");
			STEVIA_TEST_BASE_LOG.warn("***************************************************************************************");
			
			initializeDriver(browser,rcHost, rcPort, targetHostUrl, driverType, debugging, actionsLogging,profile);
		}
	}
	
	/**
	 * Clean context on class.
	 *
	 * @param testContext the test context
	 */
	@AfterClass(alwaysRun = true)
	public void cleanContextOnClass(ITestContext testContext) {
		if (testContext.getSuite().getParallel().equalsIgnoreCase("classes")) {
			SteviaContext.clean();
		}
	}
	
	/**
	 * Clean context on test.
	 *
	 * @param testContext the test context
	 */
	@AfterTest(alwaysRun = true)
	public void cleanContextOnTest(ITestContext testContext) {
		String parallelSetup = testContext.getSuite().getParallel();
		if (parallelSetup == null || parallelSetup.isEmpty()
				|| parallelSetup.equalsIgnoreCase("false")
				|| parallelSetup.equalsIgnoreCase("tests")) {
			SteviaContext.clean();
		}
	}
	
	/**
	 * Clean context on method.
	 *
	 * @param testContext the test context
	 */
	@AfterMethod(alwaysRun = true)
	public void cleanContextOnMethod(ITestContext testContext) {
		if (testContext.getSuite().getParallel().equalsIgnoreCase("methods")) {
			SteviaContext.clean();
		}
	}
	

	/**
	 * Initialize driver.
	 *
	 * @param browser the browser type (optional-default value firefox)
	 * @param rcHost the rc host
	 * @param rcPort the rc port
	 * @param targetHostUrl the target host url
	 * @param driverType the driver type (webdriver or selenium)
	 * @param debugging (if true use local Web Driver - if false Remote Web Driver )
	 * @param actionsLogging the actions logging (valid only in WebDriver if true log actions output to console and testng results)
	 * @param profile the profile
	 * @throws Exception the exception
	 */
	public void initializeDriver(String browser, String rcHost, String rcPort, String targetHostUrl,String driverType, String debugging, String actionsLogging,String profile) throws Exception {
		if (applicationContext == null) {
			super.springTestContextPrepareTestInstance();
		}
		if (driverType.compareTo("webdriver") == 0) {
			WebController controller = (WebController) applicationContext.getBean("webDriverController" );
			WebDriverWebController wdController = (WebDriverWebController)controller;
			WebDriver driver = null;
			if (debugging.compareTo(TRUE)==0) {
				
				if(browser == null || browser.compareTo("firefox") == 0 || browser.isEmpty()){
					if (profile == null || profile.isEmpty()) {
						STEVIA_TEST_BASE_LOG.info("Debug enabled, using Firefox Driver");
						driver = new FirefoxDriver();
					} else {
						STEVIA_TEST_BASE_LOG.info("Debug enabled, using a local Firefox profile with FirefoxDriver");
						ProfilesIni allProfiles = new ProfilesIni();
						FirefoxProfile ffProfile = allProfiles.getProfile(profile);
						driver = new FirefoxDriver(ffProfile);
					}
				}
				else if(browser.compareTo("chrome") == 0){
					STEVIA_TEST_BASE_LOG.info("Debug enabled, using ChromeDriver");
					driver = new ChromeDriver();
				}
				else if(browser.compareTo("iexplorer") == 0){
					STEVIA_TEST_BASE_LOG.info("Debug enabled, using InternetExplorerDriver");
					driver = new InternetExplorerDriver();
				}
				else if(browser.compareTo("safari") == 0){
					STEVIA_TEST_BASE_LOG.info("Debug enabled, using SafariDriver");
					driver = new SafariDriver();
				}
				else if(browser.compareTo("opera") == 0){
					STEVIA_TEST_BASE_LOG.info("Debug enabled, using OperaDriver");
				    driver = new OperaDriver();
				}
				else{
					throw new Exception(WRONG_BROWSER_PARAMETER);
				}
				if (targetHostUrl != null){
				driver.get(targetHostUrl);
				}
				//driver.manage().window().maximize();
				wdController.setDriver(driver);
				SteviaContext.setWebController(wdController);
				if (actionsLogging.compareTo(TRUE) == 0) {
					SteviaContext.getWebController().enableActionsLogging();
				}

			} else {
				DesiredCapabilities capability = new DesiredCapabilities();
				if(browser == null || browser.compareTo("firefox") == 0 || browser.isEmpty()){
				   STEVIA_TEST_BASE_LOG.info("Debug OFF, using a RemoteWebDriver with Firefox capabilities");
				   capability = DesiredCapabilities.firefox();
				}
				else if(browser.compareTo("chrome") == 0){
					STEVIA_TEST_BASE_LOG.info("Debug OFF, using a RemoteWebDriver with Chrome capabilities");
					capability = DesiredCapabilities.chrome();	
				}
				else if(browser.compareTo("iexplorer") == 0){
					STEVIA_TEST_BASE_LOG.info("Debug OFF, using a RemoteWebDriver with Internet Explorer capabilities");
					capability = DesiredCapabilities.internetExplorer();
				}
				else if(browser.compareTo("safari") == 0){
					STEVIA_TEST_BASE_LOG.info("Debug OFF, using a RemoteWebDriver with Safari capabilities");
					capability = DesiredCapabilities.safari();
				}
				else if(browser.compareTo("opera") == 0){
					STEVIA_TEST_BASE_LOG.info("Debug OFF, using a RemoteWebDriver with Opera capabilities");
					capability = DesiredCapabilities.opera();
				}
				else{
					throw new Exception(WRONG_BROWSER_PARAMETER);
				}
				Augmenter augmenter = new Augmenter(); // adds screenshot capability to a default web driver.
			    driver = augmenter.augment(new RemoteWebDriver(new URL("http://"+rcHost+":"+rcPort+"/wd/hub"),capability ) );
				if (targetHostUrl != null){
					driver.get(targetHostUrl);
					}
				//driver.manage().window().maximize();
				wdController.setDriver(driver);
				SteviaContext.setWebController(wdController);
				if (actionsLogging.compareTo(TRUE) == 0){
					SteviaContext.getWebController().enableActionsLogging();
				}
			}
			
			
		} else if (driverType.compareTo("selenium") == 0){
			STEVIA_TEST_BASE_LOG.info("Selenium RC mode; connecting to a Selenium RC host");
			Selenium selenium = null;
			if(browser == null || browser.compareTo("firefox") == 0 || browser.isEmpty()){
			   STEVIA_TEST_BASE_LOG.info("Using Firefox with selenium RC");
			   selenium = new DefaultSelenium(rcHost, Integer.parseInt(rcPort), FIREFOX, targetHostUrl);
			}
			else if(browser.compareTo("chrome") == 0){
				STEVIA_TEST_BASE_LOG.info("Using Chrome with selenium RC");
				selenium = new DefaultSelenium(rcHost, Integer.parseInt(rcPort), CHROME, targetHostUrl);
			}
			else if(browser.compareTo("iexplorer") == 0){
				STEVIA_TEST_BASE_LOG.info("Using Chrome with selenium RC");
				selenium = new DefaultSelenium(rcHost, Integer.parseInt(rcPort), IEXPLORER, targetHostUrl);
			}
			else if(browser.compareTo("safari") == 0){
				STEVIA_TEST_BASE_LOG.info("Using Safari with selenium RC");
				selenium = new DefaultSelenium(rcHost, Integer.parseInt(rcPort), SAFARI, targetHostUrl);
			}
			else if(browser.compareTo("opera") == 0){
				STEVIA_TEST_BASE_LOG.info("Using Opera with selenium RC");
				selenium = new DefaultSelenium(rcHost, Integer.parseInt(rcPort), OPERA, targetHostUrl);
			}
			else{
				throw new Exception(WRONG_BROWSER_PARAMETER);
			}
			selenium.start();
				//selenium.windowMaximize();
				selenium.open("");
				WebController controller = (WebController) applicationContext.getBean("seleniumController" );
				SeleniumWebController selController = (SeleniumWebController)controller;
				selController.setSelenium(selenium);
				SteviaContext.setWebController(selController);
		}
		else{
			throw new Exception("Wrong value for 'driverType' parameter was defined");
		}
	
	}
	
	
	/**
	 * Stop RC server if it's running.
	 */
	@AfterSuite(alwaysRun = true)
	protected void stopRCServer() {		
		if (isRCStarted)
			seleniumServer.stop();
	}

	/**
	 * Start RC server programmatic.
	 *
	 * @throws Exception the exception
	 */
	private void startRCServer() throws Exception{
		STEVIA_TEST_BASE_LOG.info("Selenium RC mode run in local enviroment; First start Selenium RC Server");
		RemoteControlConfiguration rc = new RemoteControlConfiguration();	
		seleniumServer = new SeleniumServer(rc);
		seleniumServer.start();
		isRCStarted=true;
	}
	
	
	/**
	 * Gets the suite output dir.
	 *
	 * @return the suite output dir
	 */
	public static String getSuiteOutputDir() {
		return suiteOutputDir;
	}


	/**
	 * Sets the suite output dir.
	 *
	 * @param suiteOutputDir the new suite output dir
	 */
	public static void setSuiteOutputDir(String suiteOutputDir) {
		SteviaTestBase.suiteOutputDir = suiteOutputDir;
	}

}
