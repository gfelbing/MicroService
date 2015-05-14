package de.gfelbing.microservice.vertical.foo.config;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import de.gfelbing.microservice.core.concurrency.ExecutorServiceConfiguration;
import de.gfelbing.microservice.core.discovery.client.D2ClientConfiguration;
import de.gfelbing.microservice.core.discovery.server.D2ServerConfiguration;
import de.gfelbing.microservice.core.http.jetty.handler.StaticContextHandlerConfiguration;
import de.gfelbing.microservice.core.http.jetty.server.JettyServerConfiguration;
import de.gfelbing.microservice.core.util.GuavaCollect;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Binds the Configuration implementations to its Interfaces for guice.
 *
 * @author gfelbing@github.com on 05.05.15.
 */
public final class ConfigurationModule extends AbstractModule {

    public static final String SERVICE_PROPERTIES_FILE = "service.properties";
    Logger LOG = LoggerFactory.getLogger(ConfigurationModule.class);


    /**
     * Reads the service.properties file.
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
    }

    @Provides
    @Singleton
    PropertiesConfiguration propertiesConfiguration() throws ConfigurationException {
        return new PropertiesConfiguration(SERVICE_PROPERTIES_FILE);
    }

    @Provides
    @Singleton
    ExecutorServiceConfiguration executorServiceConfiguration( final PropertiesConfiguration configuration ) {
        return () -> configuration.getInt("service.threadpoolsize");
    }

}
