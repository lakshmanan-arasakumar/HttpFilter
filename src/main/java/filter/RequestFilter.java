package filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;


@Component
public class RequestFilter implements Filter{
    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);
    public static final String CORRELATION_ID_KEY = "correlationId";
    public static final String APP_ID_KEY = "X-Appid";

    //@Value("${X-Appid}")
    String appId = "dummyAppId";
    
    /*@Value("${enable.http.filter}")
    boolean enableHttpFilter;*/

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("*** Filter registered and initialized..");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //if (enableHttpFilter == true) {
        	try{
                //get the correlationId if its already there
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
        /*} else {
        	logger.debug("enableHttpFilter is false, skipping request filter");
        }*/

    }

    private String getCorrelationId(HttpServletRequest request) {
        String newCorrelationId  = request.getHeader(CORRELATION_ID_KEY);
        if(newCorrelationId == null){
            // X-correlationid is not present in header, generate new
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
