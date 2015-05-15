package de.gfelbing.microservice.vertical.foo.config;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import de.gfelbing.microservice.core.http.jetty.handler.StaticContextHandlerConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Implementation of @link{StaticContextHandlerConfiguration}.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public final class StaticContextHandlerConfig implements StaticContextHandlerConfiguration {

    public static final String RESOURCES_CLASSPATH_PREFIX = "resources://";
    private final PropertiesConfiguration configuration;

    @Inject
    StaticContextHandlerConfig(final PropertiesConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getStaticResources() {
        final String staticResources = configuration.getString("static.resources");
        if (staticResources.startsWith(RESOURCES_CLASSPATH_PREFIX)) {
            return Resources.getResource(staticResources.replaceFirst(RESOURCES_CLASSPATH_PREFIX,"")).getPath();
        } else {
            return staticResources;
        }
    }

    @Override
    public String getStaticContext() {
        return configuration.getString("static.context");
    }
}
