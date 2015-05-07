package de.gfelbing.microservice.core.http.jetty.handler;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.restli.server.guice.GuiceRestliServlet;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Module for Jetty-Restli-ContextHandler
 * <p>
 * With this module you are able to inject:
 * - A Jetty #link{ContextHandler} containing a RestliServlet using the #link{JettyRestliContextHandler} annotation
 * <p>
 * In order to use this module, you have to provide a implementation of #link{RestliContextHandlerConfiguration}
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public class RestliContextHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    /**
     * Provides a ContextHandler containing a RestLi-Servlet.
     */
    @BindingAnnotation
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface JettyRestliContextHandler {
    }

    @JettyRestliContextHandler
    @Provides
    @Singleton
    @Inject
    ContextHandler restContextHandler(
            final GuiceRestliServlet restliServlet,
            final RestliContextHandlerConfiguration serviceConfiguration) {
        final ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(serviceConfiguration.getRestContext());
        handler.addServlet(new ServletHolder(restliServlet), "/*");
        return handler;
    }
}
