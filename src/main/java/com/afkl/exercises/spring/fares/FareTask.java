package com.afkl.exercises.spring.fares;

import com.afkl.exercises.spring.locations.AirportRepository;
import com.afkl.exercises.spring.locations.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static java.math.RoundingMode.HALF_UP;
import static java.util.Locale.ENGLISH;

/**
 * This class executes {@link Fare} task concurrently.
 */
@Slf4j
public class FareTask {

    private static final String ORIGIN_LABEL = "origin";

    private static final String DESTINATION_LABEL = "destination";

    private final AtomicInteger counter;
    private final DeferredResult<Fare> result;
    private Map<String, Location> locationsMap;
    private Currency currency;

    public FareTask(final DeferredResult<Fare> result, final Currency currency, final int taskCount) {
        this.counter = new AtomicInteger(taskCount);
        this.locationsMap = new HashMap<>();
        this.result = result;
        this.currency = currency;
    }

    private void success(final String locationName, final Location location) {
        if (ORIGIN_LABEL.equalsIgnoreCase(locationName)) {
            locationsMap.put(ORIGIN_LABEL, location);
        } else if (DESTINATION_LABEL.equalsIgnoreCase(locationName)) {
            locationsMap.put(DESTINATION_LABEL, location);
        }
    }

    private boolean isDone() {
        synchronized (counter) {
            return counter.decrementAndGet() == 0;
        }
    }

    /**
     * This method will launch new thread for retrieving origin data.
     *
     * @param location      location for which we need to get data from repository
     * @param locationLabel label of the location e.g: <em>origin or destination</em>
     * @param repository    {@link AirportRepository}
     */
    protected void execute(final String location, final String locationLabel, final AirportRepository repository) {
        CompletableFuture.supplyAsync(() -> {
            Thread.currentThread().setName("async-task-" + Thread.currentThread().getId());
            log.info(Thread.currentThread().getName());
            final Location loc = repository.get(ENGLISH, location).orElseThrow(IllegalArgumentException::new);
            success(locationLabel, loc);
            return true;
        }).whenCompleteAsync((u, throwable) -> {
            if (isDone()) {
                final BigDecimal fare = new BigDecimal(ThreadLocalRandom.current().nextDouble(100, 3500))
                        .setScale(2, HALF_UP);
                final Fare resultFare = new Fare(fare.doubleValue(), currency, locationsMap.get(ORIGIN_LABEL), locationsMap.get(DESTINATION_LABEL));
                result.setResult(resultFare);
            }
        });
    }
}
