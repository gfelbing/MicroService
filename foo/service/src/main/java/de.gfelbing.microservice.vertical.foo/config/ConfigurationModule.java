package de.gfelbing.microservice.vertical.foo.config;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import de.gfelbing.microservice.core.discovery.client.D2ClientConfiguration;
import de.gfelbing.microservice.core.discovery.server.D2ServerConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Binds the Configuration implementations to its Interfaces for guice.
 *
 * @author gfelbing@github.com on 05.05.15.
 */
public final class ConfigurationModule extends AbstractModule {

    Logger LOG = LoggerFactory.getLogger(ConfigurationModule.class);

    @Override
    protected void configure() {
        install(getConfigModule());
        bind(D2ClientConfiguration.class).to(D2ClientConfig.class);
        bind(D2ServerConfiguration.class).to(D2ServerConfig.class);
    }

    public Module getConfigModule() {
        try {
            return new ServiceConfig();
        } catch (ConfigurationException e) {
            LOG.error("Could not build ServiceConfiguration", e);
            return null;
        }
    }
}
