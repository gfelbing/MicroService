package de.gfelbing.microservice.service;

import de.gfelbing.microservice.api.HealthState;

/**
 * Interface for classes having a lifecycle. With this interface they will be under better control.
 *
 * @author gfelbing@github.com on 03.05.15.
 */
public interface LifeCycle {

    /**
     * Starts the Lifecycle. After a successful start getHealthState() should deliver OK.
     *
     * @throws Exception if something goes wrong.
     */
    void start() throws Exception;

    /**
     * Stops the Lifecycle. After a successful start getHealthState() should deliver OK.
     *
     * @throws Exception if something goes wrong.
     */
    void stop() throws Exception;

    /**
     * @return the current State of the Lifecycle.
     */
    HealthState getHealthState();
}
