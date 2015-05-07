package de.gfelbing.microservice.core.service;

import com.google.inject.AbstractModule;
import de.gfelbing.microservice.core.concurrency.ExecutorServiceModule;
import de.gfelbing.microservice.core.discovery.D2Module;
import de.gfelbing.microservice.core.http.jetty.JettyModule;
import de.gfelbing.microservice.core.rest.RestLiModule;

/**
 * Module for a full-stack-rest.li-service, see #link{configure()}
 *
 * @author gfelbing@github.com on 02.05.15.
 */
public final class FullStackRestLiModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ExecutorServiceModule());
        install(new D2Module());
        install(new JettyModule());
        install(new RestLiModule());
    }
}
