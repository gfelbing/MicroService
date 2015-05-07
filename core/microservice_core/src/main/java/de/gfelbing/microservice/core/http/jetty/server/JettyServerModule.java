package de.gfelbing.microservice.core.http.jetty.server;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import de.gfelbing.microservice.core.util.GuavaCollect;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpJettyServerModule.
 * <p>
 * With this module you are able to inject:
 * - A #{HttpJettyServer} wrapping a Jetty Server implementing #link{LifeCycle}
 * <p>
 * In order to use this module, you have to provide a implementation of #link{JettyServerConfiguration}
 *
 * @author gfelbing@github.com on 02.05.15.
 */
public final class JettyServerModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(JettyServerModule.class);
    private final ImmutableList.Builder<TypeLiteral<ContextHandler>> contextHandlerBuilder = new ImmutableList.Builder<>();

    @Override
    protected void configure() {
    }

    /**
     * @return a Jetty-Http-Server containing a static and a rest context handler.
     */
    @Provides
    @Singleton
    @Inject
    JettyServer getHttpJettyServer(
            final ImmutableList<ContextHandler> contextHandler,
            final JettyServerConfiguration serviceConfiguration) {
        final ImmutableList<Connector> httpConnectors = serviceConfiguration.getHttpConnectors();
        return new JettyServer(httpConnectors.toArray(new Connector[httpConnectors.size()]))
                .addAll(contextHandler);
    }

    /**
     * @return all Bindings of ContextHandler
     */
    @Provides
    @Singleton
    @Inject
    ImmutableList<ContextHandler> contextHandler(final Injector injector) {
        final ImmutableList<Binding<ContextHandler>> contextHandlerBindings = injector.getAllBindings().entrySet().stream()
                .filter(entry -> ContextHandler.class.equals(entry.getKey().getTypeLiteral().getRawType()))
                .map((keyBindingEntry) -> (Binding<ContextHandler>) keyBindingEntry.getValue())
                .collect(GuavaCollect.immutableList());
        return contextHandlerBindings.stream()
                .map(binding -> injector.getInstance(binding.getKey()))
                .collect(GuavaCollect.immutableList());
    }
}
