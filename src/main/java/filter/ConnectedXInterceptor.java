package filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ConnectedXInterceptor implements ClientHttpRequestInterceptor{
    private static final Logger logger = LoggerFactory.getLogger(ConnectedXInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String corelationId = MDC.getCopyOfContextMap().get(RequestFilter.CORRELATION_ID_KEY);

        logger.info("Adding corelationId inheader: {}" , corelationId);

        HttpHeaders headers = request.getHeaders();
        headers.add("Principal", "mmotajn1");

        if(headers.get(RequestFilter.CORRELATION_ID_KEY) == null){
            headers.add(RequestFilter.CORRELATION_ID_KEY, corelationId);
        }
        return execution.execute(request, body);
    }
}
