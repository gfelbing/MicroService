package de.gfelbing.microservice.service.http;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.r2.transport.http.server.HttpServer;
import com.linkedin.restli.server.guice.GuiceRestliServlet;
import de.gfelbing.microservice.service.config.Configuration;
import de.gfelbing.microservice.service.util.GuavaCollect;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
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
    @Inject
    ContextHandler staticResourceHandler(final Configuration configuration) throws UnavailableException {
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(configuration.getStaticResources());
        final ContextHandler contextHandler = new ContextHandler(configuration.getStaticContext());
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
    ContextHandler restContextHandler(final GuiceRestliServlet restliServlet, final Configuration configuration) {
        final ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(configuration.getRestContext());
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
                                       final Configuration configuration) {
        final ImmutableList<Connector> httpConnectors = configuration.getHttpConnectors();
        return new HttpJettyServer(httpConnectors.toArray(new Connector[httpConnectors.size()]))
                .addHandler(restLiHandler)
                .addHandler(staticResourceHandler);
    }


    /**
     * Provides the Rest-Base-URL as String.
     */
    @BindingAnnotation
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface RestLiURI {
    }

    @RestLiURI
    @Provides
    @Singleton
    @Inject
    ImmutableList<String> restURI(@RestLiHandler final ContextHandler contextHandler,
                                  final Configuration configuration) {
        try {
            return configuration.getHttpConnectors().stream().map(con -> {
                return "http://" + con.getHost() + ":" + con.getPort() + contextHandler.getContextPath();
            }).collect(GuavaCollect.immutableList());

        } catch (ArrayIndexOutOfBoundsException e) {
            LOG.error("Couldn't parse Server URI: ", e);
            return null;
        }
    }
}
