package de.gfelbing.microservice.core.common;


import de.gfelbing.microservice.core.api.HealthState;

/**
 * Classes implementing this interface have a defined life cycle defined by the methods of this interface.
 *
 * @see State
 */
public interface LifeCycle {
    /* ------------------------------------------------------------ */

    /**
     * Starts the component.
     *
     * @throws Exception if the component fails to start
     */
    void start() throws Exception;

    /**
     * Stops the component.
     *
     * @throws Exception if the component fails to stop
     */
    void stop()
            throws Exception;

    /**
     * @return the current state.
     */
    State getState();

    /**
     * Represents all states in the lifecycle.
     */
    public enum State {
        /**
         * Component created, but start() was not invoked yet.
         */
        CREATED,
        /**
         * start() method was invoked but is not finished yet.
         */
        STARTING,
        /**
         * start() method was invoked and finished successfully.
         */
        UP,
        /**
         * stop() method was invoked but is not finished yet.
         */
        STOPPING,
        /**
         * stop() method was invoked and finished successfully.
         */
        STOPPED,
        /**
         * start() or stop() failed.
         */
        ERROR
    }

    public static HealthState map(final LifeCycle.State state) {
        switch (state) {
            case ERROR:
                return HealthState.ERROR;
            case STOPPED:
                return HealthState.STOPPED;
            case STOPPING:
                return HealthState.STOPPING;
            case UP:
                return HealthState.OK;
            case STARTING:
                return HealthState.STARTING;
            case CREATED:
                return HealthState.CREATED;
            default:
                return HealthState.$UNKNOWN;
        }
    }
}
