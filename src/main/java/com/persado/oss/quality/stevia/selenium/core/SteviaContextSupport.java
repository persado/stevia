package com.persado.oss.quality.stevia.selenium.core;

import java.util.HashMap;
import java.util.Map;


public final class SteviaContextSupport {
	
	
	public static SteviaContextParameters getParameters(final Map<String,String> map) {
		return new SteviaContextParameters() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = 18484837231L;

			@Override
			public Map<String, String> getAllParameters() {
				return map;
			}

			@Override
			public void setAllParameters(Map<String, String> params) {
				throw new IllegalArgumentException("this implementation does not allow setting the map!");
			}
			
		};
	}
	
	public static SteviaContextParameters getParameters(String key, String value) {
		Map<String,String> map = new HashMap<String,String>();
		map.put(key, value);
		return getParameters(map);
	}
	
}
