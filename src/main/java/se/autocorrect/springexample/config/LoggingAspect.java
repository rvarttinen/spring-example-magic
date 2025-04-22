package se.autocorrect.springexample.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	@Before("execution(* se.autocorrect.springexample.services.*.*(..))")
	public void logBeforeServiceMethod(JoinPoint joinPoint) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.info("Entering method: " + joinPoint.getSignature());
	}

	@AfterReturning(pointcut = "execution(* se.autocorrect.springexample.services.*.*(..))", returning = "result")
	public void logAfterServiceMethod(JoinPoint joinPoint, Object result) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.info("Method " + joinPoint.getSignature().getName() + " executed with result: " + result);
	}
	
	@Before("execution(* se.autocorrect.springexample.infrastructure.*.*(..))")
	public void logBeforeInfraMethod(JoinPoint joinPoint) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.info("Entering method: " + joinPoint.getSignature());
	}

	@AfterReturning(pointcut = "execution(* se.autocorrect.springexample.infrastructure.*.*(..))", returning = "result")
	public void logAfterInfraMethod(JoinPoint joinPoint, Object result) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.info("Method " + joinPoint.getSignature().getName() + " executed with result: " + result);
	}

	@AfterThrowing(pointcut = "execution(* se.autocorrect.springexample.*.*(..))", throwing = "ex")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.error("Exception in method: " + joinPoint.getSignature().getName(), ex);
	}
}
