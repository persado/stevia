package com.persado.oss.quality.stevia.spring;

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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

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
import org.testng.annotations.Listeners;

import com.persado.oss.quality.stevia.selenium.core.Constants;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.SteviaContextSupport;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.SteviaWebControllerFactory;
import com.persado.oss.quality.stevia.selenium.listeners.ConditionsListener;
import com.persado.oss.quality.stevia.selenium.listeners.ControllerMaskingListener;
import com.persado.oss.quality.stevia.selenium.listeners.TestListener;

/**
 * The base class that is responsible for initializing Stevia contexts on start and shutting down on
 * test ends. It is parallel-aware and has options to start RC server locally if needed via XML 
 * configuration parameters.
 */
@ContextConfiguration(locations = { "classpath:META-INF/spring/stevia-boot-context.xml" })
@Listeners({ControllerMaskingListener.class, ConditionsListener.class,TestListener.class})
public class SteviaTestBase extends AbstractTestNGSpringContextTests implements Constants {

	/** The Constant STEVIA_TEST_BASE_LOG. */
	private static final Logger STEVIA_TEST_BASE_LOG = LoggerFactory.getLogger(SteviaTestBase.class);
	
	/** The selenium server. */
	private static Object[] seleniumServer;
	
	/** Determined if RC server is started programmatically. */
	private static boolean isRCStarted = false; 

	/** suite-global output directory. */
	private static String suiteOutputDir;

	
	
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
	protected final void springTestContextPrepareTestInstance() throws Exception {
		super.springTestContextPrepareTestInstance();
	} 
	
	
	/**
	 * Start rc server.
	 *
	 * @param testContext the test context
	 * @throws Exception the exception
	 */
	@BeforeSuite(alwaysRun = true)
	protected final void configureSuiteSettings(ITestContext testContext) throws Exception {	
		Map<String,String> parameters = testContext.getSuite().getXmlSuite().getAllParameters();
		
		//if the suite needs RC server, we start it here 
		if (parameters.get("driverType").compareTo("webdriver") != 0 && parameters.get("debugging").compareTo(TRUE)==0 && !isRCStarted){
			startRCServer();
		}
		setSuiteOutputDir(testContext.getSuite().getOutputDirectory());
		
		//stevia context init

		STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
		STEVIA_TEST_BASE_LOG.warn("*** SUITE initialisation phase START                                              ***");
		STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
		
		boolean initContext = true; 
		if (parameters.get("suite.init.context") != null && parameters.get("suite.init.context").startsWith("false")) {
			initContext = false;
			STEVIA_TEST_BASE_LOG.info("suite initialisation via suiteInitialisation() override will not use a Stevia Context");
		}
		
		if (initContext) {
			initializeStevia(parameters);
		}
		// user code
		suiteInitialisation(testContext);
		if (initContext) {
			//stevia context clean
			SteviaContext.clean();
		}
		STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
		STEVIA_TEST_BASE_LOG.warn("*** SUITE initialisation phase END                                                ***");
		STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
	}

	/**
	 * Suite-Level initialisation callback; this method should be overrriden to
	 * allow suite-level configuration to happen - preferrably at the Base class
	 * of the tests (overriden versions of this method will be called from the
	 * class extending this Base, at Suite initialisation, best place for this
	 * method to be overriden is at the class extending this Base class).
	 * 
	 * @param context
	 *            test context
	 */
	protected void suiteInitialisation(ITestContext context) {
		STEVIA_TEST_BASE_LOG.warn("***************************************************************************************");
		STEVIA_TEST_BASE_LOG.warn("*** suiteInitialisation() not overriden. Check your code and javadoc of method      ***");
		STEVIA_TEST_BASE_LOG.warn("*** NOTE: suiteInitialisation() by default has a SteviaContext to work with.        ***");
		STEVIA_TEST_BASE_LOG.warn("***       If you don't want this (one extra browser to start/stop) define           ***"); 
		STEVIA_TEST_BASE_LOG.warn("***       parameter 'suite.init.context' with value 'false'                         ***");
		STEVIA_TEST_BASE_LOG.warn("***************************************************************************************");
	}

	/**
	 * Before test.
	 *
	 * @param testContext the test context
	 * @throws Exception the exception
	 */
	@BeforeTest(alwaysRun = true)
	protected final void contextInitBeforeTest(ITestContext testContext) throws Exception {
		
		Map<String,String> parameters = testContext.getCurrentXmlTest().getParameters();
		
		// we check here **again** if the test needs the RC server and start it.
		if (parameters.get("driverType").compareTo("webdriver") != 0 && parameters.get("debugging").compareTo(TRUE)==0 && !isRCStarted){
			startRCServer();
		}
		String parallelSetup = testContext.getSuite().getParallel();
		if (parallelSetup == null || parallelSetup.equalsIgnoreCase("none") || parallelSetup.isEmpty() || parallelSetup.equalsIgnoreCase("false") || parallelSetup.equalsIgnoreCase("tests")) {

			STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
			STEVIA_TEST_BASE_LOG.warn("*** Driver initialisation phase, current parallel level is @BeforeTest            ***");
			STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
			
			initializeStevia(parameters);
		}
	}
	
	/**
	 * Before class.
	 *
	 * @param testContext the test context
	 * @throws Exception the exception
	 */
	@BeforeClass(alwaysRun = true)
	protected final void contextInitBeforeClass(ITestContext testContext) throws Exception {
		
		Map<String,String> parameters = testContext.getSuite().getXmlSuite().getAllParameters();
		
		if (testContext.getSuite().getParallel().equalsIgnoreCase("classes")) {

			STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
			STEVIA_TEST_BASE_LOG.warn("*** Driver initialisation phase, current parallel level is @BeforeClass**************");
			STEVIA_TEST_BASE_LOG.warn("*************************************************************************************");
			
			initializeStevia(parameters);
		}
	}
	
	/**
	 * Before method.
	 *
	 * @param testContext the test context
	 * @throws Exception the exception
	 */
	@BeforeMethod(alwaysRun = true)
	protected final void contextInitBeforeMethod(ITestContext testContext) throws Exception {
		Map<String,String> parameters = testContext.getSuite().getXmlSuite().getAllParameters();
		
		if (testContext.getSuite().getParallel().equalsIgnoreCase("methods")) {

			STEVIA_TEST_BASE_LOG.warn("***************************************************************************************");
			STEVIA_TEST_BASE_LOG.warn("*** Driver initialisation phase, current parallel level is @BeforeMethod[PANICMODE] ***");
			STEVIA_TEST_BASE_LOG.warn("***************************************************************************************");
			
			initializeStevia(parameters);
		}
	}
	
	/**
	 * Clean context on class.
	 *
	 * @param testContext the test context
	 */
	@AfterClass(alwaysRun = true)
	protected final void afterClassClean(ITestContext testContext) {
		if (testContext.getSuite().getParallel().equalsIgnoreCase("classes")) {
			SteviaContext.clean();
		}
		cleanUpAfterClass();
	}

	/**
	 * Method used to clean up or perform up various tasks at the end
	 * of the class (test) execution.
	 * 
	 */
	protected void cleanUpAfterClass() {
		// needs to be overriden
	}

	
	/**
	 * Clean context on test.
	 *
	 * @param testContext the test context
	 */
	@AfterTest(alwaysRun = true)
	protected final void cleanContextOnTest(ITestContext testContext) {
		String parallelSetup = testContext.getSuite().getParallel();
		if (parallelSetup == null || parallelSetup.isEmpty()
				|| parallelSetup.equalsIgnoreCase("false")
				|| parallelSetup.equalsIgnoreCase("none")
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
	protected final void cleanContextOnMethod(ITestContext testContext) {
		if (testContext.getSuite().getParallel().equalsIgnoreCase("methods")) {
			SteviaContext.clean();
		}
	}
	

	/**
	 * Initialize driver.
	 * 
	 * @param params
	 * @throws Exception
	 */
	protected final void initializeStevia(Map<String,String> params) throws Exception {
		if (applicationContext == null) {
			super.springTestContextPrepareTestInstance();
		}

		SteviaContext.registerParameters(SteviaContextSupport.getParameters( params ));
		if (applicationContext == null) {
			throw new IllegalStateException("ApplicationContext not set - Stevia cannot continue"); 
		}
		SteviaContext.attachSpringContext(applicationContext);
		
		WebController controller = SteviaWebControllerFactory.getWebController(applicationContext);
		SteviaContext.setWebController(controller);
		
	}


	
	/**
	 * Stop RC server if it's running.
	 */
	@AfterSuite(alwaysRun = true)
	private void stopRCServer() {		
		if (isRCStarted) {
			
			Object server = seleniumServer[0];
			Method stopMethod = (Method) seleniumServer[1];
			try {
				stopMethod.invoke(server);
			} catch (Exception e) {
				
				STEVIA_TEST_BASE_LOG.warn("Failed to shutdown the Selenium Server",e);
			}

		}
	}

	/**
	 * Start RC server programmatic.
	 *
	 * @throws Exception the exception
	 */
	private void startRCServer() throws Exception{
		STEVIA_TEST_BASE_LOG.info("Selenium RC mode run in local enviroment; First start Selenium RC Server");
		
		if(seleniumServerInClassPath()) {
			seleniumServerLoadAndStart();
		} else {
			STEVIA_TEST_BASE_LOG.error("Selenium server is not in the classpath, please modify and retry");
		}
		/*RemoteControlConfiguration rc = new RemoteControlConfiguration();	
		seleniumServer = new SeleniumServer(rc);
		seleniumServer.start();
		isRCStarted=true;*/
	}
	

	@SuppressWarnings({"rawtypes","unchecked"})
	private void seleniumServerLoadAndStart() {
		try {
			Class seleniumServerClazz = Class.forName("org.openqa.selenium.server.SeleniumServer");
			Class remoteControlConfigurationClazz = Class.forName("org.openqa.selenium.server.RemoteControlConfiguration");
			Object remoteControlConf = remoteControlConfigurationClazz.newInstance();
			Constructor constructor = seleniumServerClazz.getConstructor(remoteControlConfigurationClazz);
			constructor.setAccessible(true);
			Object seleniumServerObj = constructor.newInstance(remoteControlConf);
			Method startMethod = seleniumServerClazz.getMethod("start");
			startMethod.invoke(seleniumServerObj);
			SteviaTestBase.seleniumServer = new Object [] {seleniumServerObj, seleniumServerClazz.getMethod("stop")};
			isRCStarted=true;
		} catch (Exception e) {
			STEVIA_TEST_BASE_LOG.error("Selenium Server cannot be started, the class path does not contain it. Modify your pom.xml to include it",e);
		}
	}


	private boolean seleniumServerInClassPath() {
		try {
			Class.forName("org.openqa.selenium.server.SeleniumServer");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
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
	public final static void setSuiteOutputDir(String suiteOutputDir) {
		SteviaTestBase.suiteOutputDir = suiteOutputDir;
	}

}
