package de.gfelbing.microservice.core.rest.restli.server;

import com.google.common.collect.ImmutableList;

/**
 * The Configuration of a rest.li server, should be provided by the service.
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public interface RestLiServerConfiguration {
    /**
     * @return name of the packages, where the rest.li resources are located.
     */
    ImmutableList<String> getRestPackages();
}
