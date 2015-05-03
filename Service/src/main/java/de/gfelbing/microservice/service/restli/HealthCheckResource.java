package de.gfelbing.microservice.service.restli;

import com.google.inject.Inject;
import com.linkedin.restli.server.annotations.RestLiSimpleResource;
import com.linkedin.restli.server.resources.SimpleResourceTemplate;
import de.gfelbing.microservice.api.HealthCheck;
import de.gfelbing.microservice.api.HealthState;
import de.gfelbing.microservice.service.Service;

import java.util.Collection;

/**
 * A HealthCheck which can be used to check the services health.
 *
 * @author gfelbing@github.com
 */
@RestLiSimpleResource(name = "health", namespace = "de.gfelbing.microservice.restli")
public final class HealthCheckResource extends SimpleResourceTemplate<HealthCheck> {

    private final Service service;

    /**
     * Default constructor.
     * @param mainService The Service which handles all lifecycles.
     */
    @Inject HealthCheckResource(final Service mainService) {
        this.service = mainService;
    }

    /**
     * SimpleResource for the HealthCheck.
     * @return Cummulated state of all LifeCycle-Classes. The worst state will cause the result.
     * "worse" states in descending order: ERROR, SHUTDOWN, STARTING, OK
     */
    @Override
    public HealthCheck get() {
        Collection<HealthState> states = service.getHealthState();

        if (states.contains(HealthState.ERROR)) {
            return new HealthCheck().setStatus(HealthState.ERROR);
        } else if (states.contains(HealthState.SHUTDOWN)) {
            return new HealthCheck().setStatus(HealthState.SHUTDOWN);
        } else if (states.contains(HealthState.STARTING)) {
            return new HealthCheck().setStatus(HealthState.STARTING);
        } else {
            return new HealthCheck().setStatus(HealthState.OK);
        }
    }
}
