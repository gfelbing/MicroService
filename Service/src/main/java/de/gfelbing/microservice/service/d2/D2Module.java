package de.gfelbing.microservice.service.d2;

import com.google.inject.AbstractModule;
import de.gfelbing.microservice.service.d2.client.D2ClientModule;
import de.gfelbing.microservice.service.d2.server.D2ServerModule;

/**
 * Configurator for d2 service discovery.
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


