package de.gfelbing.microservice.core.http.jetty.handler;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

import javax.servlet.UnavailableException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Module for Jetty ContextHandler.
 * <p>
 * With this module you are able to inject:
 * - A #link{ContextHandler} for static resources.
 * <p>
 * In order to use this module, you have to provide a implementation of
 * - @link{StaticContextHandlerConfiguration}
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public final class StaticContextHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    /**
     * Has to be annotated or guice can't bind annother ContextHandler.
     */
    @BindingAnnotation
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface StaticContextHandler {
    }

    @Provides
    @StaticContextHandler
    @Singleton
    @Inject
    ContextHandler staticResourceHandler(final StaticContextHandlerConfiguration serviceConfiguration)
            throws UnavailableException {
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(serviceConfiguration.getStaticResources());
        final ContextHandler contextHandler = new ContextHandler("/" + serviceConfiguration.getStaticContext());
        contextHandler.setHandler(resourceHandler);
        return contextHandler;
    }

}
