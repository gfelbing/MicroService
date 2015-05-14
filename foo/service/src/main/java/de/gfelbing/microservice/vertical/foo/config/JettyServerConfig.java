package de.gfelbing.microservice.vertical.foo.config;

import com.google.common.collect.ImmutableList;
import de.gfelbing.microservice.core.http.jetty.server.JettyServerConfiguration;
import de.gfelbing.microservice.core.util.GuavaCollect;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.inject.Inject;

/**
 * Implementation of @Link{JettyServerConfiguration} used by @link{JettyServerModule}.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public final class JettyServerConfig implements JettyServerConfiguration {

    private final PropertiesConfiguration configuration;

    @Inject
    JettyServerConfig(final PropertiesConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ImmutableList<String> getHosts() {
        return configuration.getList("http.hosts").stream()
                .map(Object::toString)
                .collect(GuavaCollect.immutableList());
    }

    @Override
    public Integer getPort() {
        return configuration.getInt("http.port");
    }
}
