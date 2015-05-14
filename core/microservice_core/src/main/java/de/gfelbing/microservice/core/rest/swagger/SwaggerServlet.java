package de.gfelbing.microservice.core.rest.swagger;

import com.google.inject.Injector;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.models.Swagger;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.WebConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;

/**
 * Extending jersey @link{ServletContainer} to configure the guice bridge and swagger.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
@Singleton
public final class SwaggerServlet extends ServletContainer {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerServlet.class);

    private final Injector injector;
    private final SwaggerConfig swaggerConfig;

    @Inject
    SwaggerServlet(
            final ResourceConfig resourceConfig,
            final Injector injector,
            final ReflectiveJaxrsScannerCollection swaggerScanner,
            final SwaggerConfig swaggerConfig) {
        super(resourceConfig);
        this.injector = injector;
        this.swaggerConfig = swaggerConfig;
        ScannerFactory.setScanner(swaggerScanner);
    }

    @Override
    protected void init(final WebConfig webConfig) throws ServletException {
        super.init(webConfig);

        final ServiceLocator locator = getApplicationHandler().getServiceLocator();
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
        GuiceIntoHK2Bridge guiceBridge = locator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(injector);

        getServletContext().setAttribute("swagger", swaggerConfig.configure(new Swagger()));

        LOG.info("Servlet initialized.");
    }
}
