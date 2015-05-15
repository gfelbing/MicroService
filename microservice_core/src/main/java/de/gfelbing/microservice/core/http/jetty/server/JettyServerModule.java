package de.gfelbing.microservice.core.http.jetty.server;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Provides;
import de.gfelbing.microservice.core.util.GuavaCollect;
import org.eclipse.jetty.server.handler.ContextHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

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
            final JettyServerConfiguration configuration) {
        return new JettyServer(configuration.getHosts(), configuration.getPort()).addAll(contextHandler);
    }

    /**
     * @return all Bindings of ContextHandler
     */
    @Provides
    @Singleton
    @Inject
    ImmutableList<ContextHandler> contextHandler(final Injector injector) {
        return injector.getAllBindings().entrySet().stream()
                .filter(entry -> ContextHandler.class.equals(entry.getKey().getTypeLiteral().getRawType()))
                .map((keyBindingEntry) -> (Binding<ContextHandler>) keyBindingEntry.getValue())
                .map(binding -> injector.getInstance(binding.getKey()))
                .collect(GuavaCollect.immutableList());
    }
}
