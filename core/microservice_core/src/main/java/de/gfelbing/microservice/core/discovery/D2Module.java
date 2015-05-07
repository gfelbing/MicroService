package de.gfelbing.microservice.core.discovery;

import com.google.inject.AbstractModule;
import de.gfelbing.microservice.core.discovery.client.D2ClientModule;
import de.gfelbing.microservice.core.discovery.server.D2ServerModule;

/**
 * Module for D2 service discovery.
 * <p>
 * With this module you are able to use #link{D2ClientModule} and #link{D2ServerModule}
 *
 * @author gfelbing@github.com on 02.05.15.
 */
public final class D2Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new D2ClientModule());
        install(new D2ServerModule());
    }


}


