package de.gfelbing.microservice.vertical.foo;

import com.google.inject.AbstractModule;
import de.gfelbing.microservice.core.concurrency.ExecutorServiceModule;
import de.gfelbing.microservice.core.discovery.client.D2ClientModule;
import de.gfelbing.microservice.core.discovery.server.D2ServerModule;
import de.gfelbing.microservice.core.http.jetty.handler.StaticContextHandlerModule;
import de.gfelbing.microservice.core.http.jetty.server.JettyServerModule;
import de.gfelbing.microservice.core.rest.parseq.ParseqModule;
import de.gfelbing.microservice.core.rest.swagger.SwaggerModule;
import de.gfelbing.microservice.core.rest.swagger.jetty.SwaggerContextHandlerModule;
import de.gfelbing.microservice.vertical.foo.config.ConfigurationModule;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Installs all Modules needed by this service.
 *
 * @author gfelbing@github.com on 15.05.15.
 */
public final class ServiceModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceModule.class);

    @Override
    protected void configure() {
        try {
            install(new ConfigurationModule());
        } catch (ConfigurationException e) {
            LOG.error("Unable to load configuration.", e);
        }
        install(new ExecutorServiceModule());
        install(new D2ClientModule());
        install(new D2ServerModule());
        install(new JettyServerModule());
        install(new StaticContextHandlerModule());
        install(new SwaggerModule());
        install(new SwaggerContextHandlerModule());
        install(new ParseqModule());
        bind(Service.class);
    }
}
