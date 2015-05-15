package de.gfelbing.microservice.core.rest.parseq;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Module for linkedins parseq.
 * <p>
 * With this module you are able to inject:
 * - A parseq engine, i.e. for usage in a rest.li #link{HttpDispatcher}
 * <p>
 * In order to use this module, you have to provide a implementation of #link{ScheduledExecutorService}
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public final class ParseqModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    @Inject
    Engine getEngine(final ScheduledExecutorService scheduler) {
        return new EngineBuilder()
                .setTaskExecutor(scheduler)
                .setTimerScheduler(scheduler)
                .build();
    }
}
