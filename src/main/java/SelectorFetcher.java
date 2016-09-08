/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2014 Persado Intellectual Property Limited
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import us.codecraft.xsoup.Xsoup;
import us.codecraft.xsoup.xevaluator.XElements;


public class SelectorFetcher {

	
	public static void main(String args[]) throws MalformedURLException, IOException {
		
		//Connection connect = Jsoup.connect("https://www.edline.net/InterstitialLogin.page");
		//Connection connect = Jsoup.connect("http://stgweb01.ath.persado.com:8080/mcs-management-web/login");
		Connection connect = Jsoup.connect("http://woot.com");
		connect.followRedirects(true);
		connect.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:29.0) Gecko/20100101 Firefox/29.0");
		connect.timeout(60000);
		//TODO add cookies
		//TODO add headers
		
		
		Document doc = connect.get();
		//System.out.println(doc.outerHtml());
		
		

		for (Element e : doc.getAllElements()) {
			if (e.tagName().contains("root")) continue; //skip the html element!
			if (e.tag().isFormSubmittable() || e.tag().getName().equals("a") || e.tag().getName().equals("button")) {
				//System.out.println("---> "+e);
				if ( e.tag().isFormSubmittable() ) {
					String loc = e.nodeName();
					if (e.parent() != null) {
						loc = e.parent().nodeName() + " > " + e.nodeName();
					}
					if (e.hasAttr("id")) {
						try {
							//System.out.println("CSS3: "+verifyLocator(e,loc+"#"+e.attr("id")));
							verifyLocator(e,loc+"#"+e.attr("id"));
						} catch (Exception e1) {
							System.out.println("CSS3: cannot get locator for "+e+"! not verifiable, cause = "+e1.getMessage());
						}
					} else if (e.hasAttr("name")) {
						try {
							//System.out.println("CSS3: "+verifyLocator(e,loc+"[name="+e.attr("name")+"]"));
							verifyLocator(e,loc+"[name="+e.attr("name")+"]");
						} catch (Exception e1) {
							System.out.println("CSS3: cannot get locator for "+e+"! not verifiable, cause = "+e1.getMessage());
						}
					}
				}
				computeRecursiveXpath(e);

			}
		}
		
		
		System.out.println("\n\nLocators found (unique only!)");
		for (Node node : uniqueLocators.keySet()) {
			System.out.println("----------------------------");
			System.out.println("HTML Element: "+node);
			System.out.println("Locator: "+uniqueLocators.get(node));
		}
		
	}

	static Map<Node, String> uniqueLocators = new HashMap<Node, String>();
	
	
	/**
	 * verify an element locator as unique
	 * @param e
	 * @param locator
	 * @return
	 * @throws Exception 
	 */
	private static String verifyLocator( Element e, String locator) throws Exception {
		Element rootElement = e.parents().last();
		if(!locator.startsWith("//")) {
			Elements selected = rootElement.select(locator);
			if (selected.size() == 1) {
				if (!uniqueLocators.containsKey(e)) {
					uniqueLocators.put(e, locator);
				}
				return locator + " UNIQUE = "+selected.first();
			} else if (selected.size() > 1) {
				return locator + " NON-UNIQUE = "+selected;
			} else {
				return locator +" NOT FOUND - PROBLEM";
			}
		} else if(locator.startsWith("//")) { //xpath 
		    XElements elements = Xsoup.select(rootElement, locator);
		    if (elements.getElements().size() > 1) {
		    	return locator + " NON-UNIQUE!!! ";
		    } else if (elements.getElements().size() == 0) {
		    	return locator +" NOT FOUND - PROBLEM";
		    }
		    if (!uniqueLocators.containsKey(e)) {
				uniqueLocators.put(e, locator);
			}
		    return locator + " UNIQUE = "+ elements.getElements().get(0);
		    
		}
		
		return locator + " XPATH?";
	}

	private static void computeRecursiveXpath( Element e) {
		try {
			if (e.hasAttr("id")) {
				String xpath = "//"+reversePath(e)+"[@id='"+e.attr("id")+"']";
				//System.out.println("XPATH: "+verifyLocator(e,xpath));
				verifyLocator(e,xpath);
			}

			if (e.hasAttr("name")) {
				String xpath = "//"+reversePath(e)+"[@name='"+e.attr("name")+"']";
				//System.out.println("XPATH: "+verifyLocator(e,xpath));
				verifyLocator(e,xpath);
			}

			if (e.hasAttr("class") && e.attr("class").length() > 0){
				String xpath = "//"+reversePath(e)+"[@class='"+e.attr("class")+"']";
				//System.out.println("XPATH: "+verifyLocator(e,xpath));				
				verifyLocator(e,xpath);

			}

			if (e.hasAttr("href") && !e.attr("href").equals("#")) {
				String xpath = "//"+reversePath(e)+"[@href='"+e.attr("href")+"']";
				//System.out.println("XPATH: "+verifyLocator(e,xpath));			
				verifyLocator(e,xpath);
			} 
		}catch (Exception ex) { // try a less dangerous xpath
			String locator = "";
			
			locator = "//"+reversePath(e);
			try {
				//System.out.println("XPATH: "+verifyLocator(e,locator));			
				verifyLocator(e,locator);
			} catch (Exception e1) { // failed totally
				System.out.println("cannot detect locator for "+e.tagName()+" cause = "+e1.getMessage());
			}
			
		}
	}

	private static String reversePath(Element e) {
		String loc = e.tagName();
		int div = 0;
		while (e.parent()!=null) {
			String tag = e.parent().tagName().toLowerCase();
			if (tag.compareTo("body")==0) {
				break;
			} else {
				loc = tag + "/" + loc;
			}
			
			if (tag.contentEquals("div")) {
				div++;
				if (div == 2) {
					break;
				}
			} else if (tag.contentEquals("form")) {
				break;
			}
			
			e = e.parent();
		}
		return loc;
	}
}
