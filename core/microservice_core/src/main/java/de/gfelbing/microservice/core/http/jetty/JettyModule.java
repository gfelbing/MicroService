package de.gfelbing.microservice.core.http.jetty;

import com.google.inject.AbstractModule;
import de.gfelbing.microservice.core.http.jetty.handler.RestliContextHandlerModule;
import de.gfelbing.microservice.core.http.jetty.handler.StaticContextHandlerModule;
import de.gfelbing.microservice.core.http.jetty.server.JettyServerModule;

/**
 * Module for Jetty HTTP Server wrapping all Jetty modules, see #link{configure}
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public class JettyModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new RestliContextHandlerModule());
        install(new StaticContextHandlerModule());
        install(new JettyServerModule());
    }

}
