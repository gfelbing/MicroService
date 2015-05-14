package de.gfelbing.microservice.vertical.foo.config;

import com.google.inject.Inject;
import de.gfelbing.microservice.core.http.jetty.handler.StaticContextHandlerConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Implementation of @link{StaticContextHandlerConfiguration}.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public class StaticContextHandlerConfig implements StaticContextHandlerConfiguration {

    private final PropertiesConfiguration configuration;

    @Inject StaticContextHandlerConfig(PropertiesConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getStaticResources() {
        return configuration.getString("static.resources");
    }

    @Override
    public String getStaticContext() {
        return configuration.getString("static.context");
    }
}
