package de.gfelbing.microservice.core.http.jetty.server;

import com.google.common.collect.ImmutableList;
import org.eclipse.jetty.server.Connector;

/**
 * Configuration of a Jetty-Server, should be provided by the service.
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public interface JettyServerConfiguration {
    /**
     * @return connectors, the webserver will bind the socket to.
     * Can be multiple ones for different IPs, but all of them will have the same port
     */
    ImmutableList<Connector> getHttpConnectors();
}
