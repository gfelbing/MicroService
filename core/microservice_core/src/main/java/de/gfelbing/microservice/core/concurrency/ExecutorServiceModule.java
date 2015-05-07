package de.gfelbing.microservice.core.concurrency;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Module for ExecutorServices.
 * <p>
 * With this module you are able to inject:
 * - A #link{ScheduledExecutorService} based on the #{ServiceConfiguration}
 * - A #link{ExecutorService} which is basically the ScheduledExecutorService
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public class ExecutorServiceModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Inject
    ExecutorService executorService(final ScheduledExecutorService scheduledExecutorService) {
        return scheduledExecutorService;
    }

    @Provides
    @Singleton
    @Inject
    ScheduledExecutorService scheduledExecutorService(final ExecutorServiceConfiguration serviceConfiguration) {
        return Executors.newScheduledThreadPool(serviceConfiguration.getThreadpoolsize());
    }
}
