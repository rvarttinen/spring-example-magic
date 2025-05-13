/*  
 * spring-example-magic
 *  
 * Copyright (C) 2025 Autocorrect Design HB
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *  
 */
package se.autocorrect.springexample.log;

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
        logger.info("Entering method: {}", joinPoint.getSignature());
	}

	@AfterReturning(pointcut = "execution(* se.autocorrect.springexample.services.*.*(..))", returning = "result")
	public void logAfterServiceMethod(JoinPoint joinPoint, Object result) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("Method {} executed with result: {}", joinPoint.getSignature().getName(), result);
	}
	
	@Before("execution(* se.autocorrect.springexample.infrastructure.*.*(..))")
	public void logBeforeInfraMethod(JoinPoint joinPoint) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("Entering method: {}", joinPoint.getSignature());
	}

	@AfterReturning(pointcut = "execution(* se.autocorrect.springexample.infrastructure.*.*(..))", returning = "result")
	public void logAfterInfraMethod(JoinPoint joinPoint, Object result) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("Method {} executed with result: {}", joinPoint.getSignature().getName(), result);
	}

	@AfterThrowing(pointcut = "execution(* se.autocorrect.springexample.*.*(..))", throwing = "ex")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.error("Exception in method: {}", joinPoint.getSignature().getName(), ex);
	}
}
