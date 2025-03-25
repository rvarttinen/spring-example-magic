package se.autocorrect.springexample.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

	@Override
	@Bean(name = "asyncExecutor")
	public Executor getAsyncExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("AsynchThread-");
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler();
	}
	
	static class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

		@Override
		public void handleUncaughtException(Throwable ex, Method method, Object... params) {
			
			// TODO: replace with a logger 
			System.err.println(("Unexpected asynchronous exception at : " + method.getDeclaringClass().getName() + "." + method.getName()) );
		}
	}
}
