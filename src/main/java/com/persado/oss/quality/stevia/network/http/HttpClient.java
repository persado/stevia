/*
 * 
 */
package com.persado.oss.quality.stevia.network.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * The Class HttpClient.
 */
public class HttpClient implements HttpConstants {

	Logger LOG = LoggerFactory.getLogger(HttpClient.class);
	
	/** The client. */
	private static org.eclipse.jetty.client.HttpClient client = new org.eclipse.jetty.client.HttpClient();

	{
		try {
			if (System.getProperty("httpclient.followredirects", null) != null) {
				client.registerListener( SteviaJettyRedirectListener.class.getName() );
				LOG.info("HttpClient will follow redirects for HTTP Codes: 301, 302 and 303");
			} else {
				LOG.warn("HttpClient DOES NOT FOLLOW REDIRECTS. To enable, set system property 'httpclient.followredirects' to 'true'");
			}
			client.start();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	
	
	/**
	 * Instantiates a new http client.
	 */
	public HttpClient() {
	}

	/**
	 * Gets the content of the url given.
	 *
	 * @param url the URL the GET request is sent
	 * @param numberOfTimes the number of GET requests
	 * @return the a list of HTTP Responses
	 * @throws IOException, InterruptedException 
	 */
	public List<HttpResponse> get(String url, int numberOfTimes) throws IOException, InterruptedException {
		return get(url,numberOfTimes,null);
	}
	
	/**
	 * Gets the content of the url given.
	 *
	 * @param url the URL the GET request is sent
	 * @param numberOfTimes the number of GET requests
	 * @param cookies the cookies to set on the request, optional - null is a valid value
	 * @return the a list of HTTP Responses
	 * @throws IOException, InterruptedException 
	 */
	public List<HttpResponse> get(String url, int numberOfTimes, List<HttpCookie> cookies) throws IOException, InterruptedException {
		Assert.hasLength(url, "URL cannot be null");
		Assert.isTrue(numberOfTimes > 0, "numberOfTimes cannot be 0");
		
		List<HttpResponse> responses = new ArrayList<HttpResponse>(numberOfTimes);
		
		
		HttpResponse httpResponse;
		ContentExchange exchange;
		for (int i = 0; i < numberOfTimes; i++) {
			exchange = new ContentExchange();
			exchange.setURL(url);
			exchange.setMethod("GET");
			
			setCookies(exchange,cookies);
			client.send(exchange);
			exchange.waitForDone();
			httpResponse = new HttpResponse(exchange);
			responses.add(httpResponse);
		}
		return responses;
	}

	/**
	 * Posts data to the url given.
	 *
     * @param url the URL the POST request is sent
	 * @param postData list of input data for the POST requests
	 * @param numberOfTimes the number of POST requests
	 * @return the a list of HTTP Responses
	 * @throws IOException, InterruptedException 
	 */
	public List<HttpResponse> post(String url, List<HttpPostData> postData, int numberOfTimes) throws IOException, InterruptedException{
		return post(url,postData,numberOfTimes, null);
	}
	
	/**
	 * Posts data to the url given.
	 *
     * @param url the URL the POST request is sent
	 * @param postData list of input data for the POST requests
	 * @param numberOfTimes the number of POST requests
	 * @param cookies the cookies to set on the request, optional - null is a valid value
	 * @return the a list of HTTP Responses
	 * @throws IOException, InterruptedException 
	 */
	public List<HttpResponse> post(String url, List<HttpPostData> postData, int numberOfTimes, List<HttpCookie> cookies) throws IOException, InterruptedException{
		Assert.hasLength(url, "URL cannot be null");
		Assert.notEmpty(postData,"POST request cannot have empty post data");
		Assert.isTrue(numberOfTimes > 0, "numberOfTimes cannot be 0");
	
		List<HttpResponse> responses = new ArrayList<HttpResponse>(numberOfTimes);
		HttpResponse httpResponse;
		ContentExchange exchange;
		for (int i = 0; i < numberOfTimes; i++) {
			exchange = new ContentExchange();
			exchange.setURL(url);
			exchange.setMethod("POST");
			setCookies(exchange,cookies);
			addParameters(exchange, postData);
			client.send(exchange);
			exchange.waitForDone();
			httpResponse = new HttpResponse(exchange);
			responses.add(httpResponse);
		}
		return responses;
	}

	/**
	 * Adds the parameters.
	 *
	 * @param exchange that retains response content for later use.
	 * @param  list of input data for the POST requests
	 */
	private void addParameters(ContentExchange exchange, List<HttpPostData> data) {
		StringBuffer content = new StringBuffer();
		int size = data.size();
		int last = size - 1;
		String message = "%s=%s";
		HttpPostData datum;
		for (int i = 0; i < size; i++) {
			datum = data.get(i);
			content.append(String.format(message, datum.getKey(), datum.getValue()));
			if (i != last) {
				content.append("&");
			}
		}
		exchange.setRequestContent(new ByteArrayBuffer(content.toString()));
	}

	/**
	 * Shutdown the HTTP Client component
	 */
	public void shutdown() {
		try {
			client.stop();
		} catch (Exception exception) {
			throw new RuntimeException("Failed to shutdown client", exception);
		}
	}

	/**
	 * sets the cookies on the exchange if present
	 * @param exchange the exchange to work with
	 * @param cookies the cookie list; if null nothing happens
	 */
	protected void setCookies(ContentExchange exchange, List<HttpCookie> cookies) {
		if (cookies == null || cookies.size() == 0) {
			return;
		}
		
		for(HttpCookie aCookie : cookies) {
			exchange.addRequestHeader(HTTP_HEADER_COOKIE, aCookie.getKey()+"="+aCookie.getValue());
		}
		
	}

}
