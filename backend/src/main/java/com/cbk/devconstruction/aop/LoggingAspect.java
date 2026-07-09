package com.cbk.devconstruction.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.validation.Errors;

import com.cbk.devconstruction.utils.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

	@Pointcut("execution(* com.cbk.devconstruction.controller..*(..))")
	private void controllerPointCut() {
	}

	@Before("controllerPointCut()")
	public void beforeControllerMethod(JoinPoint joinPoint) {
		String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		log.debug("Start {}.{}() .....", className, methodName);
		log.debug("Current User: {}", CommonUtil.getUsernameFromAuthentication());
	}

	@After("controllerPointCut()")
	public void afterControllerMethod(JoinPoint joinPoint) {
		String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		log.debug("End {}.{}() .....", className, methodName);
	}

	@Around("controllerPointCut()")
	public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object result = joinPoint.proceed(); // Execute the method and capture the result
		stopWatch.stop();
		log.debug("Execution time: {}ms", stopWatch.getTotalTimeMillis());
		return result;
	}

	@Pointcut("execution(* *(.., @javax.validation.Valid (*), org.springframework.validation.Errors , ..)) && args(validObj, errors, ..)")
	public void validateMethodCall(Object validObj, Errors errors) {
	}

	@Before("validateMethodCall(validObj, errors)")
	public void logValidationErrors(JoinPoint joinPoint, Object validObj, Errors errors) {
		if (errors.hasErrors()) {
			log.error(errors.toString());
		}
	}

}
