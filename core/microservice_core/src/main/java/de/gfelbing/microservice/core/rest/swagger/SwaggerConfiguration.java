package de.gfelbing.microservice.core.rest.swagger;

import com.google.common.collect.ImmutableList;

/**
 * Configuration interface used by @link{SwaggerModule}.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public interface SwaggerConfiguration {

    /**
     * List of packages jersey and swagger will scan for annotated classes.
     * @return immutable list of packages, i.e. ImmutableList.of("com.foo.bar.resources").
     */
    ImmutableList<String> getResourcePackages();
}
