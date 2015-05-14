package de.gfelbing.microservice.core.http.jetty.server;

import com.google.common.collect.ImmutableList;

/**
 * Configuration of a Jetty-Server, should be provided by the service.
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public interface JettyServerConfiguration {
    /**
     * Can be multiple ones for different IPs, but all of them will have the same port.
     * @return connectors, the webserver will bind the socket to.
     */
    ImmutableList<String> getHosts();

    /**
     * The port jetty will bind to.
     * @return the Port as Integer.
     */
    Integer getPort();
}
