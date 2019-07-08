package filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;


@Component
public class RequestFilter implements Filter{
	private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);
	public static final String CORRELATION_ID_KEY = "correlationId";
	public static final String APP_ID_KEY = "X-Appid";

	//@Value("${X-Appid}")
	String appId = "dummyAppId";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("*** Filter registered and initialized..");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try{
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String newCorrelationId = getCorrelationId(httpRequest);
			httpRequest.setAttribute(CORRELATION_ID_KEY, newCorrelationId);
			MDC.put(CORRELATION_ID_KEY, newCorrelationId);
			MDC.put(APP_ID_KEY, appId);
			chain.doFilter(request, response);
		} finally {
			logger.debug("*** clearing the MDC");
			MDC.clear();
		}
	}

	private String getCorrelationId(HttpServletRequest request) {
		String newCorrelationId  = request.getHeader(CORRELATION_ID_KEY);
		if(newCorrelationId == null){
			UUID uuid = UUID.randomUUID();
			newCorrelationId = uuid.toString();
			logger.info("**** Generating new corelationid: {}" , newCorrelationId);
		}
		return newCorrelationId;
	}

	@Override
	public void destroy() {
		logger.debug("*** Filter Destroyed..");
	}
}
