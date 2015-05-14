package de.gfelbing.microservice.core.rest.swagger.jetty;

/**
 * Configuration Interface needed by SwaggerContextHandlerModule.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public interface SwaggerContextHandlerConfiguration {
    /**
     * The rest context the ContextHandler will be bound to.
     * @return the context as String, i.e. api.v1
     */
    String getRestContext();
}
