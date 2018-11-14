package com.afkl.exercises.spring.fares;

import com.afkl.exercises.spring.locations.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/fares/{origin}/{destination}")
public class FareController {

    /**
     * orogin label
     */
    private static final String ORIGIN_LABEL = "origin";

    /**
     * destination label
     */
    private static final String DESTINATION_LABEL = "destination";

    private final AirportRepository repository;

    @Autowired
    public FareController(AirportRepository repository, Environment environment, CounterService counterService) {
        this.repository = repository;
    }

    @RequestMapping(method = GET)
    public DeferredResult<Fare> calculateFare(@PathVariable("origin") String origin,
                                              @PathVariable("destination") String destination,
                                              @RequestParam(value = "currency", defaultValue = "EUR") String currency) {
        DeferredResult<Fare> deferredResult = new DeferredResult<>();
        final FareTask fareTask = new FareTask(deferredResult, Currency.valueOf(currency.toUpperCase()), 2);
        fareTask.execute(origin, ORIGIN_LABEL, repository);
        fareTask.execute(destination, DESTINATION_LABEL, repository);
        return deferredResult;
    }


}
