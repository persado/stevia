/*
 * 
 */
package com.persado.oss.quality.stevia.network.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.io.ByteArrayBuffer;


/**
 * The Class HttpClient.
 */
public class HttpClient {

	/** The client. */
	private static org.eclipse.jetty.client.HttpClient client = new org.eclipse.jetty.client.HttpClient();

	{
		try {
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
	 * Gets the.
	 *
	 * @param url the URL the GET request is sent
	 * @param numberOfTimes the number of GET requests
	 * @return the a list of HTTP Responses
	 * @throws IOException, InterruptedException 
	 */
	public List<HttpResponse> get(String url, int numberOfTimes) throws IOException, InterruptedException {
		List<HttpResponse> responses = new ArrayList<HttpResponse>(numberOfTimes);
		HttpResponse httpResponse;
		ContentExchange exchange;
		for (int i = 0; i < numberOfTimes; i++) {
			exchange = new ContentExchange();
			exchange.setURL(url);
			exchange.setMethod("GET");
			client.send(exchange);
			exchange.waitForDone();
			httpResponse = new HttpResponse(exchange);
			responses.add(httpResponse);
		}
		return responses;
	}

	/**
	 * Post.
	 *
     * @param url the URL the POST request is sent
	 * @param postData list of input data for the POST requests
	 * @param numberOfTimes the number of POST requests
	 * @return the a list of HTTP Responses
	 * @throws IOException, InterruptedException 
	 */
	public List<HttpResponse> post(String url, List<HttpPostData> postData, int numberOfTimes) throws IOException, InterruptedException{
		List<HttpResponse> responses = new ArrayList<HttpResponse>(numberOfTimes);
		HttpResponse httpResponse;
		ContentExchange exchange;
		for (int i = 0; i < numberOfTimes; i++) {
			exchange = new ContentExchange();
			exchange.setURL(url);
			exchange.setMethod("POST");
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

}
