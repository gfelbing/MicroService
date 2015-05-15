package de.gfelbing.microservice.core.rest.swagger.jetty;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import de.gfelbing.microservice.core.rest.swagger.SwaggerServlet;
import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewritePatternRule;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides ContextHandler containing a HttpServlet.
 * <p>
 * With this module you are able to inject:
 * - A @link{ContextHandler} containing a @link{SwaggerServlet}.
 * <p>
 * In order to use this module, you have to provide implementations of:
 * - @link{SwaggerContextHandlerConfiguration}
 * - @link{SwaggerServlet} from the @link{SwaggerModule}.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public final class SwaggerContextHandlerModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    /**
     * Has to be annotated or guice can't bind annother ContextHandler.
     */
    @BindingAnnotation
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface SwaggerContextHandler {
    }

    @Provides
    @SwaggerContextHandler
    @Singleton
    @Inject
    ContextHandler restContextHandler(
            final SwaggerServlet jerseyServlet,
            final SwaggerContextHandlerConfiguration serverConfiguration) {
        final ServletContextHandler handler = new ServletContextHandler();
        final ServletHolder servletHolder = new ServletHolder(jerseyServlet);

        final RewriteHandler rewriteHandler = new RewriteHandler();
        RedirectPatternRule rewritePatternRule = new RedirectPatternRule();
        rewritePatternRule.setPattern("/health");
        rewritePatternRule.setLocation("/" + serverConfiguration.getRestContext() + "/health");
        rewriteHandler.addRule(rewritePatternRule);

        handler.setContextPath("/");
        handler.setHandler(rewriteHandler);
        handler.addServlet(servletHolder, "/" + serverConfiguration.getRestContext() + "/*");

        return handler;
    }
}
