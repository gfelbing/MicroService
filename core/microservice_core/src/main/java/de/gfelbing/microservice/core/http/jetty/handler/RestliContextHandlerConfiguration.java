package de.gfelbing.microservice.core.http.jetty.handler;

/**
 * Configuration of a rest.li context handler, should be provided by the service.
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public interface RestliContextHandlerConfiguration {
    /**
     * @return context path for rest resources.
     */
    String getRestContext();
}
