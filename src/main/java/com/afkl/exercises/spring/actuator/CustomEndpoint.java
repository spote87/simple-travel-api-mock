package com.afkl.exercises.spring.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is endpoint for getting application metrics data. It can be accessed by REST <em>/appStatistics</em>.
 * Endpoint is configurable and can be changed in <em>application.properties</em>.
 */
@Component
public class CustomEndpoint implements Endpoint<Map<String, Object>> {

    private static final String STATISTICS_ENDPOINT_NAME = "statistic.endpoint.name";

    /**
     * total requests processed constant
     */
    private static final String COUNTER_TOTAL_REQUESTS_PROCESSED = "counter.total.requests.processed";

    private static final String COUNTER_TOTAL_OK_REQUESTS = "counter.status.2";

    private static final String COUNTER_TOTAL_CLIENT_ERROR = "counter.status.4";

    private static final String COUNTER_TOTAL_SERVER_ERROR = "counter.status.5";

    private static final String REQUESTS_PROCESSED = "requests.processed";

    private static final String REQUESTS_OK = "requests.ok";

    private static final String REQUESTS_CLIENT_ERROR = "requests.clienterror";

    private static final String REQUESTS_SERVER_ERROR = "requests.servererror";

    private static final String AVERAGE_REQUESTS_TIME = "requests.averageresponsetime";

    private static final String MIN_REQUESTS_TIME = "requests.minresponsetime";

    private static final String MAX_REQUESTS_TIME = "requests.maxresponsetime";

    private ResponseTimeData responseTimeData;

    private Environment environment;

    private MetricsEndpoint metricsEndpoint;

    private Lock lock = new ReentrantLock();

    @Autowired
    public CustomEndpoint(Environment environment, MetricsEndpoint metricsEndpoint, ResponseTimeData responseTimeData) {
        this.environment = environment;
        this.metricsEndpoint = metricsEndpoint;
        this.responseTimeData = responseTimeData;
    }

    @Override
    public String getId() {
        return environment.getProperty(STATISTICS_ENDPOINT_NAME);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isSensitive() {
        return false;
    }

    @Override
    public Map<String, Object> invoke() {
        final Map<String, Object> metrics = metricsEndpoint.invoke();
        final Map<String, Object> result = new HashMap<>();
        final Set<Map.Entry<String, Object>> entrySet = metrics.entrySet();
        BigInteger requestsProcessed = BigInteger.ZERO;
        BigInteger requestsOk = BigInteger.ZERO;
        BigInteger requestsClientError = BigInteger.ZERO;
        BigInteger requestsServerError = BigInteger.ZERO;
        for (Map.Entry entry : entrySet) {
            final String key = String.valueOf(entry.getKey());
            if (!StringUtils.isEmpty(key)) {
                if (key.equalsIgnoreCase(COUNTER_TOTAL_REQUESTS_PROCESSED)) {
                    requestsProcessed = BigInteger.valueOf((long) entry.getValue());
                } else if (key.startsWith(COUNTER_TOTAL_OK_REQUESTS)) {
                    requestsOk = requestsOk.add(BigInteger.ONE);
                } else if (key.startsWith(COUNTER_TOTAL_CLIENT_ERROR)) {
                    requestsClientError = requestsClientError.add(BigInteger.ONE);
                } else if (key.startsWith(COUNTER_TOTAL_SERVER_ERROR)) {
                    requestsServerError = requestsServerError.add(BigInteger.ONE);
                }
            }
        }
        updateResult(result, requestsProcessed, requestsOk, requestsClientError, requestsServerError);
        return result;
    }


    /**
     * Update metrics data in result.
     *
     * @param result
     * @param requestsProcessed
     * @param requestsOk
     * @param requestsClientError
     * @param requestsServerError
     */
    private void updateResult(Map<String, Object> result, BigInteger requestsProcessed, BigInteger requestsOk, BigInteger requestsClientError, BigInteger requestsServerError) {
        lock.lock();
        BigDecimal averageResponseTime = responseTimeData.getAllRequestsTime();
        BigDecimal minResponseTime = responseTimeData.getMinRequestTime();
        BigDecimal maxResponseTime = responseTimeData.getMaxRequestTime();
        if (requestsProcessed.intValue() > 0)
            averageResponseTime = averageResponseTime.divide(BigDecimal.valueOf(requestsProcessed.intValue()), 2, RoundingMode.HALF_UP);
        minResponseTime = minResponseTime.divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        maxResponseTime = maxResponseTime.divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        result.put(environment.getProperty(REQUESTS_PROCESSED), requestsProcessed.intValue());
        result.put(environment.getProperty(REQUESTS_OK), requestsOk.intValue());
        result.put(environment.getProperty(REQUESTS_CLIENT_ERROR), requestsClientError.intValue());
        result.put(environment.getProperty(REQUESTS_SERVER_ERROR), requestsServerError.intValue());
        result.put(environment.getProperty(AVERAGE_REQUESTS_TIME), averageResponseTime.toString() + "s");
        result.put(environment.getProperty(MIN_REQUESTS_TIME), minResponseTime.toString() + "s");
        result.put(environment.getProperty(MAX_REQUESTS_TIME), maxResponseTime.toString() + "s");
        lock.unlock();
    }

}