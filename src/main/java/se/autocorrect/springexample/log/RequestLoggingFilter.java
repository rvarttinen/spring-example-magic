package se.autocorrect.springexample.log;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
public class RequestLoggingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String traceId = UUID.randomUUID().toString();
		MDC.put("traceId", traceId);
		chain.doFilter(request, response);

		// TODO: do some extra logging here if you want to see some info logging
		// (instead of the default debug).
	}
}
