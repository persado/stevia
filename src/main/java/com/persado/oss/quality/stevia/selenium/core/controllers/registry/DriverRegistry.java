package com.persado.oss.quality.stevia.selenium.core.controllers.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A single location to hold all drivers registered with Stevia
 * 
 * It can be easily modified via the Registrar bean to add mappings in other
 * spring.xml files that are added on Stevia usage by 3rd parties.
 * 
 * By default 2 drivers exist, the WebDriver and Selenium RC ones.
 * 
 * @author tangelatos
 * 
 * @param <K>
 * @param <V>
 */
public class DriverRegistry<K, V> {

	private static final Logger LOG = LoggerFactory.getLogger(DriverRegistry.class);
	
	private final Map<K, V> mappings = new HashMap<K, V>();

	public void addMapping(K key, V value) {
		LOG.info("Registering driver {} with driver factory {}", key, value);
		mappings.put(key, value);
	}

	public Map<K, V> getMappings() {
		return Collections.unmodifiableMap(mappings);
	}
}
