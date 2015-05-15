package de.gfelbing.microservice.vertical.foo.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.wordnik.swagger.config.SwaggerConfig;
import de.gfelbing.microservice.core.concurrency.ExecutorServiceConfiguration;
import de.gfelbing.microservice.core.discovery.client.D2ClientConfiguration;
import de.gfelbing.microservice.core.discovery.server.D2ServerConfiguration;
import de.gfelbing.microservice.core.http.jetty.handler.StaticContextHandlerConfiguration;
import de.gfelbing.microservice.core.http.jetty.server.JettyServerConfiguration;
import de.gfelbing.microservice.core.rest.swagger.SwaggerConfiguration;
import de.gfelbing.microservice.core.rest.swagger.jetty.SwaggerContextHandlerConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


/**
 * Binds the Configuration implementations to its Interfaces for guice.
 *
 * @author gfelbing@github.com on 05.05.15.
 */
public final class ConfigurationModule extends AbstractModule {

    private static final String SERVICE_PROPERTIES_FILE = "service.properties";

    /**
     * Reads the service.properties file.
     *
     * @throws ConfigurationException if parsing of the file service.properties fails.
     */
    public ConfigurationModule() throws ConfigurationException {
    }

    @Override
    protected void configure() {
        bind(D2ClientConfiguration.class).to(D2ClientConfig.class);
        bind(D2ServerConfiguration.class).to(D2ServerConfig.class);
        bind(JettyServerConfiguration.class).to(JettyServerConfig.class);
        bind(StaticContextHandlerConfiguration.class).to(StaticContextHandlerConfig.class);
        bind(SwaggerContextHandlerConfiguration.class).to(SwaggerConfigurationImpl.class);
        bind(SwaggerConfiguration.class).to(SwaggerConfigurationImpl.class);
        bind(SwaggerConfig.class).to(SwaggerConfigurationImpl.class);
    }

    @Provides
    @Singleton
    PropertiesConfiguration propertiesConfiguration() throws ConfigurationException {
        return new PropertiesConfiguration(SERVICE_PROPERTIES_FILE);
    }

    @Provides
    @Singleton
    ExecutorServiceConfiguration executorServiceConfiguration(final PropertiesConfiguration configuration) {
        return () -> configuration.getInt("service.threadpoolsize");
    }

}
