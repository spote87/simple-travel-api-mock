package com.afkl.exercises.spring.actuator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Setter
@Getter
public class ResponseTimeData {

    private BigDecimal allRequestsTime = BigDecimal.ZERO;

    private BigDecimal minRequestTime = BigDecimal.ZERO;

    private BigDecimal maxRequestTime = BigDecimal.ZERO;
}
