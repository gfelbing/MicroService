package de.gfelbing.microservice.service.config;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import de.gfelbing.microservice.service.util.GuavaCollect;
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
public final class Configuration {

    private final Integer threadpoolsize;

    private final ImmutableList<Connector> httpConnectors;

    private final ImmutableList<String> restPackages;
    private final String restContext;

    private final String staticResources;
    private final String staticContext;

    /**
     * Package-Local Constructor used by guice.
     * @throws ConfigurationException if parsing of the file service.properties fails.
     */
    Configuration() throws ConfigurationException {
        final PropertiesConfiguration config = new PropertiesConfiguration("service.properties");
        this.threadpoolsize = config.getInt("service.threadpoolsize");
        this.httpConnectors = createConnectors(asStringList(config.getList("http.hosts")), config.getInt("http.port"));
        this.restPackages = asStringList(config.getList("rest.packages"));
        this.restContext = config.getString("rest.context");
        this.staticResources = config.getString("static.resources");
        this.staticContext = config.getString("static.context");
    }

    private static ImmutableList<String> asStringList(final Collection<Object> objects) {
        return objects.stream().map(p -> ((String) p)).collect(GuavaCollect.immutableList());
    }

    private static ImmutableList<Connector> createConnectors(final ImmutableList<String> hosts, final Integer port) {
        return hosts.stream().map(host -> {
            Connector connector = new SelectChannelConnector();
            connector.setHost(host);
            connector.setPort(port);
            return connector;
        }).collect(GuavaCollect.immutableList());
    }

    /**
     * @return size of the thread pool used by restli and jetty.
     */
    public Integer getThreadpoolsize() {
        return threadpoolsize;
    }

    /**
     * @return connectors, the webserver will bind the socket to.
     * Can be multiple ones for different IPs, but all of them will have the same port
     */
    public ImmutableList<Connector> getHttpConnectors() {
        return httpConnectors;
    }

    /**
     * @return name of the packages, where the rest.li resources are located.
     */
    public ImmutableList<String> getRestPackages() {
        return restPackages;
    }

    /**
     * @return context path for rest resources.
     */
    public String getRestContext() {
        return restContext;
    }

    /**
     * @return path to the static resource-folder.
     */
    public String getStaticResources() {
        return staticResources;
    }

    /**
     * @return context path for static resources.
     */
    public String getStaticContext() {
        return staticContext;
    }
}
