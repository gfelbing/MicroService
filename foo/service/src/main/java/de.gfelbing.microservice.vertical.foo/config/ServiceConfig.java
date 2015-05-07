package de.gfelbing.microservice.vertical.foo.config;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import de.gfelbing.microservice.core.concurrency.ExecutorServiceConfiguration;
import de.gfelbing.microservice.core.http.jetty.handler.RestliContextHandlerConfiguration;
import de.gfelbing.microservice.core.http.jetty.handler.StaticContextHandlerConfiguration;
import de.gfelbing.microservice.core.http.jetty.server.JettyServerConfiguration;
import de.gfelbing.microservice.core.rest.restli.server.RestLiServerConfiguration;
import de.gfelbing.microservice.core.util.GuavaCollect;
import de.gfelbing.microservice.vertical.foo.resources.HealthCheckResource;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import java.util.Collection;

/**
 * Provides configured and generated values.
 *
 * @author gfelbing@github.com on 04.05.15.
 */
@Singleton
public final class ServiceConfig extends AbstractModule {

    private final PropertiesConfiguration configuration;

    /**
     * Reads the service.properties file.
     * @throws ConfigurationException if parsing of the file service.properties fails.
     */
    public ServiceConfig() throws ConfigurationException {
        this.configuration =  new PropertiesConfiguration("service.properties");
    }


    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    ExecutorServiceConfiguration executorServiceConfiguration() {
        return () -> configuration.getInt("service.threadpoolsize");
    }

    @Provides
    @Singleton
    RestliContextHandlerConfiguration restliContextHandlerConfiguration() {
        return () -> configuration.getString("rest.context");
    }

    @Provides
    @Singleton
    RestLiServerConfiguration restLiServerConfiguration() {
        return () -> ImmutableList.of(HealthCheckResource.class.getPackage().getName());
    }

    @Provides
    @Singleton
    StaticContextHandlerConfiguration staticContextHandlerConfiguration() {
        return new StaticContextHandlerConfiguration() {
            @Override
            public String getStaticResources() {
                return configuration.getString("static.resources");
            }

            @Override
            public String getStaticContext() {
                return configuration.getString("static.context");
            }
        };
    }

    @Provides
    @Singleton
    JettyServerConfiguration jettyServerConfiguration() {
        return () -> {
            final ImmutableList<String> hosts = asStringList(configuration.getList("http.hosts"));
            final Integer port = configuration.getInt("http.port");
            return createConnectors(hosts, port);
        };
    }

    private static ImmutableList<Connector> createConnectors(final ImmutableList<String> hosts, final Integer port) {
        return hosts.stream().map(host -> {
            Connector connector = new SelectChannelConnector();
            connector.setHost(host);
            connector.setPort(port);
            return connector;
        }).collect(GuavaCollect.immutableList());
    }

    private static ImmutableList<String> asStringList(final Collection<Object> objects) {
        return objects.stream().map(p -> ((String) p)).collect(GuavaCollect.immutableList());
    }
}
