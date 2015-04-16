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


import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.spring.SteviaTestBase;


/**
 * The Class WebControllerBase.
 */
public abstract class WebControllerBase implements WebController {

	/** The Constant LOG. */
	private static final Logger WEB_CONTROLLER_BASE_LOG = LoggerFactory.getLogger(WebControllerBase.class);
	/**
	 * uses Reporter to log the screenshot of a failed log entry (error(...)) to
	 * the logs
	 * 
	 * @param file
	 *            file must exist
	 */
	protected void reportLogScreenshot(File file) {
		Reporter.log("<a href=\"" + makeRelativePath(file) + "\"><p align=\"left\">Error screenshot at " + new Date() + "</p>");
		Reporter.log("<p><img width=\"1024\" src=\"" + makeRelativePath(file) + "\" alt=\"screenshot at " + new Date() + "\"/></p></a><br />");
		WEB_CONTROLLER_BASE_LOG.warn("Screenshot has been generated, path is {}", file.getAbsolutePath());
		// LOG.warn("Screenshot generated from this ", new Exception
		// ("STACKTRACE"));
	}

	/**
	 * Info.
	 * 
	 * @param message
	 *            the message
	 */
	public void info(String message) {
		WEB_CONTROLLER_BASE_LOG.info(message);
		Reporter.log("<p class=\"testOutput\" style=\"font-size:1em;\">" + message + "</p>");

	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 */
	public void warn(String message) {
		WEB_CONTROLLER_BASE_LOG.warn(message);
		Reporter.log("<p class=\"testOutput\" style=\"color:orange; font-size:1em;\">" + message + "</p>");
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 */
	public void error(String message) {
		WEB_CONTROLLER_BASE_LOG.error(message);
		Reporter.log("<p class=\"testOutput\" style=\"color:red; font-size:1em;\">" + message + "</p>");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#sleep(
	 * long)
	 */
	@Override
	public void sleep(long milliseconds) {
		try {
			WEB_CONTROLLER_BASE_LOG.info("About to sleep for " + milliseconds + " msecs");
			Reporter.log("About to sleep for " + milliseconds + " msecs<br>");
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			WEB_CONTROLLER_BASE_LOG.error(e.getMessage());
		}

	}

	/**
	 * make a filename within the reportng folders so we can use relative paths.
	 * 
	 * @return the file
	 */
	protected File createScreenshotFile() {
		if (SteviaTestBase.getSuiteOutputDir() != null) {
			File outputDir = new File(new File(SteviaTestBase.getSuiteOutputDir()).getParent(), "screenshots");
			try {
				outputDir.mkdirs();
			} catch (SecurityException e) {
				WEB_CONTROLLER_BASE_LOG.error(e.getMessage());
				return null;
			}
			return new File(outputDir, "screenshot-" + System.nanoTime() + ".png");
		}
		return new File("screenshot-" + System.nanoTime() + ".png");
	}

	/**
	 * Make relative path.
	 * 
	 * @param file
	 *            the file instance
	 * @return the file path
	 */
	private String makeRelativePath(File file) {
		String outputDir = new File(SteviaTestBase.getSuiteOutputDir()).getParent();
		if (outputDir != null) {
			String absolute = file.getAbsolutePath();
			int beginIndex = absolute.indexOf(outputDir) + outputDir.length();
			String relative = absolute.substring(beginIndex);
			return ".." + relative.replace('\\', '/');
		}
		return file.getPath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.persado.oss.quality.stevia.selenium.core.WebController#getTableInfo
	 * (java.lang.String, int)
	 */
	@Override
	public Map<String, Map<String, String>> getTableInfo(String locator, int numberOfColumns) {
		Map<String, Map<String, String>> tableData = new HashMap<String, Map<String, String>>();
		Map<String, String> tableColumns = null;
		waitForElement(locator);
		int rowNumber = 1;
		for (int counter = 1; counter <= getNumberOfTotalRows(locator); counter++) {
			tableColumns = new HashMap<String, String>();
			for (int columns = 1; columns <= numberOfColumns; columns++) {
				if (locator.startsWith("css")) {
					tableColumns.put("column_" + Integer.toString(columns), getText(locator + " *:nth-child(" + counter + ") *:nth-child(" + columns + ")"));
				} else if (locator.startsWith("//") || locator.startsWith("xpath")) {
					tableColumns.put("column_" + Integer.toString(columns), getText(locator + "//*[" + counter + "]//*[" + columns + "]"));
				} else {
					tableColumns.put("column_" + Integer.toString(columns), getText("css=#" + locator + " *:nth-child(" + counter + ") *:nth-child(" + columns + ")"));

				}
			}
			tableData.put("row_" + Integer.toString(rowNumber), tableColumns);
			rowNumber = rowNumber + 1;
		}
		return tableData;
	}

}
