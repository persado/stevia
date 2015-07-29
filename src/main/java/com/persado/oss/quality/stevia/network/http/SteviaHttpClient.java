/*
 * 
 */
package com.persado.oss.quality.stevia.network.http;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class HttpClient.
 */
public class SteviaHttpClient implements HttpConstants {

	Logger LOG = LoggerFactory.getLogger(SteviaHttpClient.class);
	static boolean shouldFollowRedirects = false;


	static {

        if (System.getProperty("httpclient.followredirects", null) != null) {
            shouldFollowRedirects = true;
        }
}

	
	
	/**
	 * Instantiates a new http client.
	 */
	public SteviaHttpClient() {
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

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        if (cookies != null)
            for (HttpCookie cookie : cookies) {
                headers.add("Cookie", cookie.getKey()+"="+cookie.getValue());
            }


		for (int i = 0; i < numberOfTimes; i++) {

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
            HttpResponse httpResponse = new HttpResponse(response.getStatusCode(),response.getBody(),response.getHeaders());
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
		return post(url, postData, numberOfTimes, null);
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

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        for (HttpCookie cookie : cookies) {
            headers.add("Cookie", cookie.getKey()+"="+cookie.getValue());
        }


        for (int i = 0; i < numberOfTimes; i++) {
            HttpEntity<String> entity = new HttpEntity<String>(addParameters(postData), headers);

            ResponseEntity<String> response = restTemplate.exchange(url,
                    HttpMethod.POST, entity, String.class);

            HttpResponse httpResponse = new HttpResponse(response.getStatusCode(),response.getBody(),response.getHeaders());
            responses.add(httpResponse);
        }


		return responses;
	}

	/**
	 * Adds the parameters.
	 *
     * @param  list of input data for the POST requests
     */
	private String addParameters(List<HttpPostData> data) {
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
		return content.toString();
	}



}
