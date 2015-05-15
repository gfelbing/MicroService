package de.gfelbing.microservice.vertical.foo.api;

import de.gfelbing.microservice.core.common.LifeCycle;

/**
 * API Model for the HealthCheckResource.
 *
 * @author gfelbing@github.com on 15.05.15.
 */
public final class HealthState {

    private LifeCycle.State status;

    /**
     * Represents the services state in the HealthCheck.
     * @return the set State.
     */
    public LifeCycle.State getStatus() {
        return status;
    }

    /**
     * Default setter.
     * @param state the state to set.
     * @return itself for chaining.
     */
    public HealthState setStatus(final LifeCycle.State state) {
        this.status = state;
        return this;
    }
}
