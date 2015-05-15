package de.gfelbing.microservice.core.concurrency;

/**
 * Configuration of the executor services, should be provided by the service.
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public interface ExecutorServiceConfiguration {

    /**
     * @return size of the thread pool used by restli and jetty.
     */
    Integer getThreadpoolsize();
}
