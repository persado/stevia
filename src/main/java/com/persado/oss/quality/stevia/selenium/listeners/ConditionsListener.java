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

import com.persado.oss.quality.stevia.annotations.RunsConditionsWithController;
import com.persado.oss.quality.stevia.annotations.RunsWithController;
import com.persado.oss.quality.stevia.annotations.RunsWithControllerHelper;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;

public class ConditionsListener implements IInvokedMethodListener2 {

	private static final Logger LOG = LoggerFactory.getLogger(ConditionsListener.class);
	
	public ConditionsListener() {
		
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		Method rmethod = method.getTestMethod().getConstructorOrMethod().getMethod();
		if (rmethod.getAnnotation(Test.class) != null) {
			if (rmethod.getAnnotation(RunsConditionsWithController.class) != null) {
				LOG.warn("Method or Class of {} wants pre/post conditions to be checked", rmethod.getName());
				RunsWithControllerHelper p = SteviaContext.getSpringContext().getBean(RunsWithControllerHelper.class);
				try {
					LOG.debug("Mask and Execute Preconditions of method ",rmethod.getName());
					p.maskAndExecPreconditions(rmethod);
					LOG.debug("Reverting to original controller");
					p.revertToOriginalController();
				} catch (Throwable e) {
					throw new IllegalStateException("failed to replace controller",e);
				}
			}
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		Method rmethod = method.getTestMethod().getConstructorOrMethod().getMethod();
		if (rmethod.getAnnotation(Test.class) != null) {
			if (rmethod.getAnnotation(RunsConditionsWithController.class) != null) {
				LOG.warn("Method or Class of {} wants pre/post conditions to be checked", rmethod.getName());
				RunsWithControllerHelper p = SteviaContext.getSpringContext().getBean(RunsWithControllerHelper.class);
				try {
					LOG.debug("Mask and Execute Postconditions of method ",rmethod.getName());
					p.maskAndExecPostconditions(rmethod);
					LOG.debug("Reverting to original controller");
					p.revertToOriginalController();
				} catch (Throwable e) {
					throw new IllegalStateException("failed to replace controller",e);
				}
			}
		}
	}

}
