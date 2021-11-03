package com.persado.oss.quality.stevia.selenium.core.controllers.webdriverapi;

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


import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.controllers.WebDriverWebController;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public abstract class BySizzle extends By {

	private static final Logger LOG = LoggerFactory.getLogger(BySizzle.class);

	/**
	 * Finds elements via the driver's underlying W3 Selector engine. If the
	 * browser does not implement the Selector API, a best effort is made to
	 * emulate the API. In this case, we strive for at least CSS2 support, but
	 * offer no guarantees.
	 * @param sizzleCssSelector selector
	 * @return css selector
	 */
	public static By css(final String sizzleCssSelector) {
		if (sizzleCssSelector == null)
			throw new IllegalArgumentException(
					"Cannot find elements when the selector is null");

		return new BySizzleCssSelector(sizzleCssSelector);

	}


	public static class BySizzleCssSelector extends By implements Serializable {

		private static final String HTTPS = "https://";
		private static final String HTTP = "http://";
		private static final String DEFAULT_SIZZLE_URL = "http://cdnjs.cloudflare.com/ajax/libs/sizzle/2.3.3/sizzle.min.js";
		
		private String ownSelector;

		public BySizzleCssSelector(String selector) {
			ownSelector = selector;
		}

		@Override
		public WebElement findElement(SearchContext context) {
			return findElementBySizzleCss(context, ownSelector);
		}

		@Override
		public List<WebElement> findElements(SearchContext context) {
			return findElementsBySizzleCss(context, ownSelector);
		}

		@Override
		public String toString() {
			return "ByExtended.selector: " + ownSelector;
		}


		 /********************************* SIZZLE SUPPORT CODE**************************************/
	

		/**
		 * Find element by sizzle css.
		 * @param context context
		 * 
		 * @param cssLocator
		 *            the cssLocator
		 * @return the web element
		 */
		public WebElement findElementBySizzleCss(SearchContext context, String cssLocator) {
			List<WebElement> elements = findElementsBySizzleCss(context, cssLocator);
			if (elements != null && elements.size() > 0 ) {
				return elements.get(0);
			}			
			// if we get here, we cannot find the element via Sizzle.
			throw new NoSuchElementException("selector '"+cssLocator+"' cannot be found in DOM");
		}

		private void fixLocator(SearchContext context, String cssLocator,
				WebElement element) {

			if (element instanceof RemoteWebElement) {
				try {
					@SuppressWarnings("rawtypes")
					Class[] parameterTypes = new Class[] { SearchContext.class,
							String.class, String.class };
					Method m = element.getClass().getDeclaredMethod(
							"setFoundBy", parameterTypes);
					m.setAccessible(true);
					Object[] parameters = new Object[] { context,
							"css selector", cssLocator };
					m.invoke(element, parameters);
				} catch (Exception fail) {
					//NOOP Would like to log here? 
				}
			}
		}

		private WebDriver getDriver() {
			WebDriverWebController controller = ((WebDriverWebController) SteviaContext.getWebController());
			return controller.getDriver();
		}

		/**
		 * Find elements by sizzle css.
		 * 
		 * @param cssLocator the cssLocator
		 *@param context context
		 * @return the list of the web elements that match this locator
		 */
		public List<WebElement> findElementsBySizzleCss(SearchContext context, String cssLocator) {
			injectSizzleIfNeeded();
			String javascriptExpression = createSizzleSelectorExpression(cssLocator);
			List<WebElement> elements = executeRemoteScript(javascriptExpression);
			if (elements.size() > 0) {
				for (WebElement el : elements) { 
					fixLocator(context, cssLocator, el);
				}
			}
			return elements;
		}

		@SuppressWarnings("unchecked")
		private final List<WebElement> executeRemoteScript(String javascriptExpression) {
			List<WebElement> list = null;
			JavascriptExecutor executor = (JavascriptExecutor) getDriver();

			try {
				list = (List<WebElement>) executor
					.executeScript(javascriptExpression);
			} catch (WebDriverException wde) {
				if (wde.getMessage().contains("Sizzle is not defined")) {
					LOG.error("Attempt to execute the code '"+javascriptExpression+"' has failed - Sizzle was not detected. Trying once more");
					// we wait for 1/2 sec
					try { Thread.sleep(500); } catch (InterruptedException e) { }
					// try to inject sizzle once more.
					injectSizzleIfNeeded();
					// now, try again to execute
					list = (List<WebElement>) executor
							.executeScript(javascriptExpression);
				} else { // not a Sizzle case, just throw it
					throw wde;
				}
			} finally {
				if (list == null) {
					list = Collections.emptyList();
				}
			}
			return list;
		}

		/**
		 * Creates the sizzle selector expression.
		 * 
		 * @param cssLocator
		 *            the cssLocator
		 * @return string that represents the sizzle selector expression.
		 */
		private String createSizzleSelectorExpression(String cssLocator) {
			return "return Sizzle(\"" + cssLocator + "\")";
		}

		/**
		 * Inject sizzle if needed.
		 */
		private void injectSizzleIfNeeded() {
			if (!sizzleLoaded()) {
				injectSizzle();
			} else {
				return; // sizzle is ready
			}
			
			for (int i = 0; i<40; i++ ) {
				if(sizzleLoaded() ) {
					return; // sizzle is loaded
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// FIX: nothing to print here
				}
				if (i % 10 == 0) {
					LOG.warn("Attempting to re-load SizzleCSS from {}",getSizzleUrl());
					injectSizzle();
				}
			}
			
			//Try on last time
			if (!sizzleLoaded()) {
				LOG.error("After so many tries, sizzle does not appear in DOM");
			} 
			// sizzle is not loaded yet 
			throw new RuntimeException("Sizzle loading from ("+ getSizzleUrl() +") has failed - " +
					"provide a better sizzle URL via -DsizzleUrl");
		}

        private String getSizzleUrl() {
            return System.getProperty("sizzleUrl",DEFAULT_SIZZLE_URL );
        }

        /**
		 * Check if the Sizzle library is loaded.
		 * 
		 * @return the true if Sizzle is loaded in the web page 
		 */
		public Boolean sizzleLoaded() {
			Boolean loaded = true;
			try {
				loaded = (Boolean) ((JavascriptExecutor) getDriver())
						.executeScript("return (window.Sizzle != null);");
				
			} catch (WebDriverException e) {
				LOG.error("while trying to verify Sizzle loading, WebDriver threw exception {} {}",e.getMessage(),e.getCause() != null ? "with cause "+e.getCause() : "");
				loaded = false;
			}
			LOG.debug("Sizzle is loaded : "+loaded);
			return loaded;
		}

		/**
		 * Inject sizzle 1.8.2
		 */
		public void injectSizzle() {
			String sizzleUrl = getSizzleUrl();
			if (sizzleUrl.startsWith(HTTP)) {
				sizzleUrl = sizzleUrl.substring(HTTP.length());
			} else if  (sizzleUrl.startsWith(HTTPS)) {
				sizzleUrl = sizzleUrl.substring(HTTPS.length());
			}
			
			StringBuilder script = new StringBuilder()
				.append(" var bodyTag = document.getElementsByTagName('body')[0];")
				.append("if (bodyTag) {")
				.append("  var sizzl = document.createElement('script');")
				.append("  sizzl.type = 'text/javascript';")
				.append("  sizzl.src = document.location.protocol + '//").append(sizzleUrl).append("';")
				.append("  bodyTag.appendChild(sizzl);")
				.append("} else if (window.jQuery) { ")
				.append("	 $.getScript(document.location.protocol + '//").append(sizzleUrl).append("');")
				.append("}");
			final String stringified = script.toString();
			LOG.debug("Executing injection script: {}",stringified);
			((JavascriptExecutor) getDriver()).executeScript(stringified);
		}
		/**
		 * ******************** SIZZLE SUPPORT CODE
		 */

	}
}
