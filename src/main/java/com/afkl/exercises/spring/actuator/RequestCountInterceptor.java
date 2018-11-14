package com.afkl.exercises.spring.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This is interceptor for monitoring application metrics data.
 */
@Component
public class RequestCountInterceptor extends HandlerInterceptorAdapter {

    private static final String REQUESTS_PROCESSED = "requests.processed";

    private static final String REQ_PARAM_TOTAL_REQUEST_TIME = "totalTime";

    private CounterService counterService;

    private ResponseTimeData responseTimeData;

    private Environment environment;

    private BigDecimal allRequestsTime = BigDecimal.ZERO;

    private BigDecimal minRequestTime = BigDecimal.ZERO;

    private BigDecimal maxRequestTime = BigDecimal.ZERO;

    private boolean isFirst = true;

    @Autowired
    public RequestCountInterceptor(CounterService counterService, Environment environment, ResponseTimeData responseTimeData) {
        this.counterService = counterService;
        this.environment = environment;
        this.responseTimeData = responseTimeData;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(REQ_PARAM_TOTAL_REQUEST_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        Long timingAttr = (Long) request.getAttribute(REQ_PARAM_TOTAL_REQUEST_TIME);
        long requestTime = System.currentTimeMillis() - timingAttr;
        allRequestsTime = allRequestsTime.add(BigDecimal.valueOf(requestTime).divide(BigDecimal.valueOf(60), 5, RoundingMode.HALF_UP));
        if (isFirst) {
            minRequestTime = minRequestTime.add(BigDecimal.valueOf(requestTime));
            maxRequestTime = maxRequestTime.add(BigDecimal.valueOf(requestTime));
            isFirst = false;
        } else {
            minRequestTime = minRequestTime.min(BigDecimal.valueOf(requestTime));
            maxRequestTime = maxRequestTime.max(BigDecimal.valueOf(requestTime));
        }

        responseTimeData.setAllRequestsTime(allRequestsTime);
        responseTimeData.setMaxRequestTime(maxRequestTime);
        responseTimeData.setMinRequestTime(minRequestTime);

        counterService.increment(environment.getProperty(REQUESTS_PROCESSED));
    }
}
