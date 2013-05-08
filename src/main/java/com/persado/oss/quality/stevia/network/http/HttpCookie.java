package com.persado.oss.quality.stevia.network.http;

import java.io.Serializable;


/**
 * Basic common Cookie support
 * @author panagiotis.tsiakos
 *
 */
public class HttpCookie implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;

	public HttpCookie(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}


}
