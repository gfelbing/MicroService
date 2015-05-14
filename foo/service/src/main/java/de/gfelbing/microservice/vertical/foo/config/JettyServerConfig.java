package de.gfelbing.microservice.vertical.foo.config;

import com.google.common.collect.ImmutableList;
import de.gfelbing.microservice.core.http.jetty.server.JettyServerConfiguration;
import de.gfelbing.microservice.core.util.GuavaCollect;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.server.Connector;

import javax.inject.Inject;

/**
 * TODO: Add statement here.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public class JettyServerConfig implements JettyServerConfiguration {

    private final PropertiesConfiguration configuration;

    @Inject
    JettyServerConfig(PropertiesConfiguration configuration) {
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
