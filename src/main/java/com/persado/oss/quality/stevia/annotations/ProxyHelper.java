package com.persado.oss.quality.stevia.annotations;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

public class ProxyHelper {

	@SuppressWarnings("rawtypes")
	public static <T> T create(final Object obj, final ProxyCallback onSuccess, final ProxyCallback onException) throws Exception {
		ProxyFactory factory = new ProxyFactory();
		Class<T> origClass = (Class<T>) obj.getClass();
		factory.setSuperclass(origClass);
		Class clazz = factory.createClass();
		MethodHandler handler = new MethodHandler() {

			@Override
			public Object invoke(Object self, Method overridden, Method forwarder,
					Object[] args) throws Throwable {
				try {
					Object returnVal = overridden.invoke(obj, args);
					if (null != onSuccess) onSuccess.execute();
					return returnVal;
				} catch (Throwable tr) {
					if (null != onException) onException.execute();
					throw tr;
				}
			}
		};
		Object instance = clazz.newInstance();
		((ProxyObject) instance).setHandler(handler);
		return (T) instance;
	}
	
	public static void main(String[] args) throws Exception {
		
		Foo f = new Foo();
		
		Foo fProxy = ProxyHelper.create(f, new ProxyCallback() {

			@Override
			public void execute() {
				System.out.println("proxyfied on success");
				
			}
			
		}, null);
		System.out.println("original object: ") ;
		f.testMethod();
		System.out.println("proxified object: ") ;
		fProxy.testMethod();
	}
	
	
}

class Foo {
	int counter = 0;
	public void testMethod() {
		System.out.println("Helloworld = "+ (++counter));
	}
}
