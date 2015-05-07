package de.gfelbing.microservice.vertical.foo.resources;

import com.google.inject.Inject;
import com.linkedin.restli.server.annotations.RestLiSimpleResource;
import com.linkedin.restli.server.resources.SimpleResourceTemplate;
import de.gfelbing.microservice.core.api.HealthCheck;
import de.gfelbing.microservice.core.common.LifeCycle;
import de.gfelbing.microservice.core.service.FullStackRestLiService;

/**
 * A HealthCheck which can be used to check the services health.
 *
 * @author gfelbing@github.com
 */
@RestLiSimpleResource(name = "health", namespace = "de.gfelbing.microservice.restli")
public final class HealthCheckResource extends SimpleResourceTemplate<HealthCheck> {

    private final FullStackRestLiService service;

    /**
     * Default constructor.
     *
     * @param mainService The Service which handles all lifecycles.
     */
    @Inject
    HealthCheckResource(final FullStackRestLiService mainService) {
        this.service = mainService;
    }

    /**
     * SimpleResource for the HealthCheck.
     *
     * @return the state of the Service
     */
    @Override
    public HealthCheck get() {
        return new HealthCheck().setStatus(LifeCycle.map(service.getState()));
    }

}
