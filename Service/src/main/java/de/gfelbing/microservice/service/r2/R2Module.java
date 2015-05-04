package de.gfelbing.microservice.service.r2;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.r2.filter.FilterChains;
import com.linkedin.r2.filter.transport.FilterChainDispatcher;
import com.linkedin.r2.transport.common.bridge.server.TransportDispatcher;
import com.linkedin.r2.transport.http.server.HttpDispatcher;
import com.linkedin.restli.server.DelegatingTransportDispatcher;
import com.linkedin.restli.server.RestLiConfig;
import com.linkedin.restli.server.RestLiServer;
import com.linkedin.restli.server.resources.PrototypeResourceFactory;
import de.gfelbing.microservice.service.config.Configuration;

import java.net.URI;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This Module provides a R2-HTTP-Server, which can be used for a Server on HTTP-Layer.
 *
 * @author gfelbing@github.com on 02.05.15.
 */
public final class R2Module extends AbstractModule {
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

    @Provides
    @Singleton
    @Inject
    RestLiConfig getRestLiConfig(final Configuration configuration) {
        final RestLiConfig config = new RestLiConfig();
        config.setServerNodeUri(URI.create("/"));
        config.addResourcePackageNames(configuration.getRestPackages());
        return config;
    }

    @Provides
    @Singleton
    @Inject
    HttpDispatcher restliHttpDispatcher(final RestLiConfig config, final Engine engine) {
        final RestLiServer restLiServer = new RestLiServer(config, new PrototypeResourceFactory(), engine);
        final TransportDispatcher delegatingTransportDispatcher = new DelegatingTransportDispatcher(restLiServer);
        final TransportDispatcher filterDispatcher =
                new FilterChainDispatcher(delegatingTransportDispatcher, FilterChains.empty());
        return new HttpDispatcher(filterDispatcher);
    }
}
