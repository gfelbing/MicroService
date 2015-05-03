package de.gfelbing.microservice.service.http;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.r2.transport.http.server.HttpServer;
import com.linkedin.restli.server.guice.GuiceRestliServlet;
import de.gfelbing.microservice.service.config.Configuration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.UnavailableException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a singleton instance of a HttpJettyServer according to the Configuration-Interface.
 *
 * @author gfelbing@github.com on 02.05.15.
 */
public final class HttpJettyServerModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(HttpJettyServerModule.class);

    @Override
    protected void configure() {
        bind(HttpServer.class).to(HttpJettyServer.class);
    }

    /**
     * @return Connectors used by the HTTP-Server.
     */
    @Provides
    @Singleton
    Connector[] connectors() {
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(Configuration.PORT);
        return new Connector[]{connector};
    }

    /**
     * Provides a ContextHandler containing a ResourceHandler.
     */
    @BindingAnnotation
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface StaticHandler {
    }
    @StaticHandler
    @Provides
    @Singleton
    ContextHandler staticResourceHandler() throws UnavailableException {
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(Configuration.STATIC_RESOURCES);
        final ContextHandler contextHandler = new ContextHandler(Configuration.STATIC_CONTEXT);
        contextHandler.setHandler(resourceHandler);
        return contextHandler;
    }

    /**
     * Provides a ContextHandler containing a RestLi-Servlet.
     */
    @BindingAnnotation
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface RestLiHandler {
    }
    @RestLiHandler
    @Provides
    @Singleton
    @Inject
    ContextHandler restContextHandler(final GuiceRestliServlet restliServlet) {
        final ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(Configuration.REST_CONTEXT);
        handler.addServlet(new ServletHolder(restliServlet), "/*");
        return handler;
    }

    /**
     * @return a Jetty-Http-Server containing a static and a rest context handler.
     */
    @Provides
    @Singleton
    @Inject
    HttpJettyServer getHttpJettyServer(@RestLiHandler final ContextHandler restLiHandler,
                                       @StaticHandler final ContextHandler staticResourceHandler,
                                       final Connector[] connectors) {
        return new HttpJettyServer(connectors)
                .addHandler(restLiHandler)
                .addHandler(staticResourceHandler);
    }


    /**
     * Provides the Rest-Base-URL as String.
     */
    @BindingAnnotation
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface RestLiURI { }
    @RestLiURI
    @Provides
    @Singleton
    @Inject
    String restURI(@RestLiHandler final ContextHandler contextHandler) {
        try {
            return "http://" + Configuration.HOST + ":" + Configuration.PORT + "/" + contextHandler.getContextPath();
        } catch (ArrayIndexOutOfBoundsException e) {
            LOG.error("Couldn't parse Server URI: ", e);
            return null;
        }
    }
}
