package de.gfelbing.microservice.core.rest.swagger;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.wordnik.swagger.jaxrs.listing.ApiListingResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides a @link{SwaggerServlet} which can be used in any Webserver dealing with @link{HttpServlet}.
 * <p>
 * With this module you are able to inject:
 * - @link{ReflectiveJaxrsScannerCollection} containing swagger scanners for the configured packages.
 * - @link{ResourceConfig} containing configured resource packages and swagger resource packages.
 * <p>
 * In order to use this module, you have to provide implementations of:
 * - @link{SwaggerConfiguration} from this package
 * - @link{com.wordnik.swagger.config.SwaggerConfig} to configure your swagger.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public final class SwaggerModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Inject
    ReflectiveJaxrsScannerCollection reflectiveJaxrsScanner(final SwaggerConfiguration swaggerConfiguration) {
        return new ReflectiveJaxrsScannerCollection()
                .addAllPackages(swaggerConfiguration.getResourcePackages());
    }

    @Provides
    @Singleton
    @Inject
    ResourceConfig resourceConfig(final SwaggerConfiguration swaggerConfiguration) {
        final ImmutableList<String> packages = ImmutableList.<String>builder()
                .add(ApiListingResource.class.getPackage().getName())
                .addAll(swaggerConfiguration.getResourcePackages()).build();
        return new ResourceConfig()
                .packages(packages.toArray(new String[packages.size()]))
                .register(JacksonFeature.class);
    }
}
