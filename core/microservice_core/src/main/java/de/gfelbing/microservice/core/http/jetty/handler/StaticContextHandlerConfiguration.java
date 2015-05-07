package de.gfelbing.microservice.core.http.jetty.handler;

/**
 * Configuration of a static context handler, should be provided by the service.
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public interface StaticContextHandlerConfiguration {
    /**
     * @return path to the static resource-folder.
     */
    String getStaticResources();

    /**
     * @return context path for static resources.
     */
    String getStaticContext();
}
