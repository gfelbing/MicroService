package de.gfelbing.microservice.vertical.foo.config;

import com.google.common.collect.ImmutableList;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.models.Swagger;
import de.gfelbing.microservice.core.rest.swagger.SwaggerConfiguration;
import de.gfelbing.microservice.core.rest.swagger.jetty.SwaggerContextHandlerConfiguration;
import de.gfelbing.microservice.vertical.foo.resources.HealthCheck;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.inject.Inject;

/**
 * Implementation for @link{SwaggerConfiguration} and @link{SwaggerConfig} used by @link{SwaggerModule}.
 *
 * @author gfelbing@github.com on 15.05.15.
 */
public final class SwaggerConfigurationImpl implements
        SwaggerConfig,
        SwaggerConfiguration,
        SwaggerContextHandlerConfiguration {

    private final PropertiesConfiguration configuration;

    @Inject
    SwaggerConfigurationImpl(final PropertiesConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Configures swagger for service specific purposes.
     *
     * @param swagger the swagger to configure.
     * @return the configured swagger.
     */
    @Override
    public Swagger configure(final Swagger swagger) {
        return swagger.basePath("/" + getRestContext());
    }

    /**
     * List of packages jersey and swagger will scan for annotated classes.
     *
     * @return immutable list of packages, i.e. ImmutableList.of("com.foo.bar.resources").
     */
    @Override
    public ImmutableList<String> getResourcePackages() {
        return ImmutableList.of(HealthCheck.class.getPackage().getName());
    }

    /**
     * The rest context the ContextHandler will be bound to.
     *
     * @return the context as String, i.e. api.v1
     */
    @Override
    public String getRestContext() {
        return configuration.getString("rest.context");
    }
}
