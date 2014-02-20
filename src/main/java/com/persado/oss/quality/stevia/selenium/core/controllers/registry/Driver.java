package com.persado.oss.quality.stevia.selenium.core.controllers.registry;

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
