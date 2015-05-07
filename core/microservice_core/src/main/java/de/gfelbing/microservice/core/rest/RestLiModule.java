package de.gfelbing.microservice.core.rest;

import com.google.inject.AbstractModule;
import de.gfelbing.microservice.core.rest.parseq.ParseqModule;
import de.gfelbing.microservice.core.rest.restli.client.RestLiClientModule;
import de.gfelbing.microservice.core.rest.restli.server.RestLiServerModule;

/**
 * This Module provides a RestLi Server and Client with Parseq, see #link{configure()}
 *
 * @author gfelbing@github.com on 02.05.15.
 */
public final class RestLiModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ParseqModule());
        install(new RestLiClientModule());
        install(new RestLiServerModule());
    }
}
