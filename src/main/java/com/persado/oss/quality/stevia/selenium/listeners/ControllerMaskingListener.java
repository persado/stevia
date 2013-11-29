package com.persado.oss.quality.stevia.selenium.listeners;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.persado.oss.quality.stevia.annotations.RunsWithController;
import com.persado.oss.quality.stevia.annotations.RunsWithControllerHelper;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;

public class ControllerMaskingListener implements IInvokedMethodListener2 {

	public static final Logger LOG = LoggerFactory.getLogger(ControllerMaskingListener.class);

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
	}

	boolean masked = false;
	
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		Method rmethod = method.getTestMethod().getConstructorOrMethod().getMethod();
		if (rmethod.getAnnotation(Test.class) != null || 
			rmethod.getAnnotation(BeforeClass.class) != null || 
			rmethod.getAnnotation(BeforeTest.class) != null) {
			if (rmethod.getAnnotation(RunsWithController.class) != null || 
				rmethod.getDeclaringClass().getAnnotation(RunsWithController.class) != null) {
				LOG.warn("Method or Class of {} asks Controller to be masked", rmethod.getName());
				RunsWithControllerHelper p = SteviaContext.getSpringContext().getBean(RunsWithControllerHelper.class);
				try {
					p.maskExistingController(rmethod);
					masked = true;
				} catch (Throwable e) {
					throw new IllegalStateException("failed to replace controller",e);
				}
			}
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		if (masked) {
			RunsWithControllerHelper p = SteviaContext.getSpringContext().getBean(RunsWithControllerHelper.class);
			try {
				p.revertToOriginalController();
				masked = false;
			} catch (Throwable e) {
				throw new IllegalStateException("failed to replace masked controller",e);
			}
		}
	}

}
