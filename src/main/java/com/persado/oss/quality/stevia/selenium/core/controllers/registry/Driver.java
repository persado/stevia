package com.persado.oss.quality.stevia.selenium.core.controllers.registry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.persado.oss.quality.stevia.selenium.core.controllers.factories.WebControllerFactory;

public class Driver<K, V> {

	private final DriverRegistry<K, V> registry;

	private K name;
	private V classInstance;
	

	@Autowired
	public Driver(DriverRegistry<K, V> globalRegistry) {
		registry = globalRegistry;
	}

	public void setName(K name) {
		Assert.hasLength((String) name, "name cannot be null!");
		this.name = name;
	}

	public void setClassName(String className) {
		try {
			Class<?> forName = Class.forName((String) className);
			Object instance = forName.newInstance();
			Assert.isInstanceOf(WebControllerFactory.class, instance, "className must be implementing WebControllerFactory");
			this.classInstance = (V) instance;
		} catch (ClassNotFoundException e) {
			Assert.state(false, "Class "+className+" cannot be instanciated, error = "+e.getMessage());
		} catch (InstantiationException e) {
			Assert.state(false, "Class "+className+" cannot be instanciated, error = "+e.getMessage());
		} catch (IllegalAccessException e) {
			Assert.state(false, "Class "+className+" cannot be instanciated, error = "+e.getMessage());
		}
	}

	@PostConstruct
	public void registerMapping() {
		registry.addMapping(name, classInstance);
	}

	
}
