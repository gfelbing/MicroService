package de.gfelbing.microservice.service.config;

import java.util.Arrays;
import java.util.Collection;

/**
 * Some configuration parameters for the service.
 *
 * @author gfelbing@github.com on 02.05.15.
 */
public interface Configuration {
    /**
     * The IP/Host, the webserver will bind the socket to.
     */
    String HOST = "ermunaz";
    /**
     * The Port, the webserver will bind the socket to.
     */
    Integer PORT = 8080;

    /**
     * The size of the thread pool used by restli and jetty.
     */
    Integer THREAD_POOL_SIZE = 4;

    /**
     * The name of the package, where the rest.li resources are located.
     */
    Collection<String> REST_PACKAGES = Arrays.asList("de.gfelbing.microservice.service.restli");
    /**
     * The context path for rest resources.
     */
    String REST_CONTEXT = "/microservice.v1";

    /**
     * The context path for static resources.
     */
    String STATIC_CONTEXT = "/static";
    /**
     * The path to the static resource-folder.
     */
    String STATIC_RESOURCES = "/tmp/static/";
}
