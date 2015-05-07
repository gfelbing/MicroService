package de.gfelbing.microservice.core.rest.restli.server;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.parseq.Engine;
import com.linkedin.r2.filter.FilterChains;
import com.linkedin.r2.filter.transport.FilterChainDispatcher;
import com.linkedin.r2.transport.common.bridge.server.TransportDispatcher;
import com.linkedin.r2.transport.http.server.HttpDispatcher;
import com.linkedin.restli.server.DelegatingTransportDispatcher;
import com.linkedin.restli.server.RestLiConfig;
import com.linkedin.restli.server.RestLiServer;
import com.linkedin.restli.server.resources.PrototypeResourceFactory;

import java.net.URI;

/**
 * Module for rest.li server.
 * <p>
 * With this module you are able to inject:
 * - A #{RestLiConfig} based on a #{ServiceConfiguration}
 * - RestLi #link{HttpDispatcher} for usage in a Http-Server
 * <p>
 * In order to use this module, you have to provide a implementation of #link{RestLiServerConfiguration}
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public class RestLiServerModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    @Inject
    RestLiConfig getRestLiConfig(final RestLiServerConfiguration serviceConfiguration) {
        final RestLiConfig config = new RestLiConfig();
        config.setServerNodeUri(URI.create("/"));
        config.addResourcePackageNames(serviceConfiguration.getRestPackages());
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
