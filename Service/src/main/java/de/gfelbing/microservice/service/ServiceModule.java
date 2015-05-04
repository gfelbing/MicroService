package de.gfelbing.microservice.service;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import de.gfelbing.microservice.service.config.Configuration;
import de.gfelbing.microservice.service.d2.D2Module;
import de.gfelbing.microservice.service.http.HttpJettyServerModule;
import de.gfelbing.microservice.service.r2.R2Module;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Root Module, installing the R2/D2/Jetty-Modules and provides the ExecutorService.
 *
 * @author gfelbing@github.com on 02.05.15.
 */
public final class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new D2Module());
        install(new HttpJettyServerModule());
        install(new R2Module());
    }

    @Provides
    @Singleton
    @Inject
    ExecutorService executorService(final ScheduledExecutorService scheduledExecutorService) {
        return (ExecutorService) scheduledExecutorService;
    }

    @Provides
    @Singleton
    @Inject
    ScheduledExecutorService scheduledExecutorService(final Configuration configuration) {
        return Executors.newScheduledThreadPool(configuration.getThreadpoolsize());
    }
}
