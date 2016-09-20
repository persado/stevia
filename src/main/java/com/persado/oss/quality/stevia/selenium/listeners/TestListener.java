package com.persado.oss.quality.stevia.selenium.listeners;

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

import com.persado.oss.quality.stevia.network.IO;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.controllers.WebDriverWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

/**
 * a simple test listener to keep track of tests being executed.
 * @author t.angelatos
 *
 */
public class TestListener implements ITestListener,IConfigurationListener {

	private static final Logger TEST_LISTENER_LOG = LoggerFactory.getLogger(TestListener.class);
	private boolean takeScreenshot = true;
	
	public void createScreenshot(ITestResult tr) {
		if (!takeScreenshot || SteviaContext.getWebController() == null) {
			TEST_LISTENER_LOG.warn("Screenshot is disabled or Stevia is not yet initialised!!");
			return;
		}
		try {
			if (SteviaContext.isWebDriver()) {
				WebDriverWebController controller = (WebDriverWebController) SteviaContext.getWebController();
				controller.takeScreenShot();
			}
		} catch (Exception e) {
			TEST_LISTENER_LOG.error("failure! ", e);
		}
	}

	
	@Override
	public void onStart(ITestContext testContext) {
        String takeIt = testContext.getSuite().getParameter("tests.takeScreenshot");
		if (takeIt !=null && takeIt.equalsIgnoreCase("false")) {
        		takeScreenshot = false;
        }
        try {
        	 File currentPath = new File("./target/screenshots");
			try {
				currentPath.mkdirs();
			} catch (SecurityException e) {
				TEST_LISTENER_LOG.error(e.getMessage());
			}
			if (currentPath.isDirectory()) {
				currentPath = currentPath.getCanonicalFile();
			}
			TEST_LISTENER_LOG.info("Current execution path is "+currentPath);
			IO.verifyToken();
        } catch (Exception e) {
            TEST_LISTENER_LOG.error("Failed to configure screenshots, error is " + e.getMessage(), e);
        } 
	}

	@Override
	public void onFinish(ITestContext testContext) {
	}

	
	@Override
	public void onTestSuccess(ITestResult tr) {
	}

	
	@Override
	public void onTestSkipped(ITestResult tr) {
	}
	
	
	@Override
	public void onTestFailure(ITestResult tr) {
		createScreenshot(tr);
	}

	
	@Override
	public void onConfigurationFailure(ITestResult tr) {
		createScreenshot(tr);
	}


	@Override
	public void onConfigurationSuccess(ITestResult itr) {
		
	}

	@Override
	public void onConfigurationSkip(ITestResult itr) {
		
	}

	@Override
	public void onTestStart(ITestResult result) {
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	}
}
