/*
   Copright (c) 2015 Georg Felbinger (gfelbing@github.com)

   Based on com.linkedin.r2.transport.http.HttpJettyServer
    from 'com.linkedin.pegasus:r2-jetty:2.2.5' (Gradle-Maven-Link)

   Copyright (c) 2012 LinkedIn Corp.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/**
 * $Id: $
 */

package de.gfelbing.microservice.service.http;

import com.linkedin.r2.transport.http.server.HttpServer;
import com.linkedin.r2.transport.http.server.HttpServerFactory;
import de.gfelbing.microservice.api.HealthState;
import de.gfelbing.microservice.service.LifeCycle;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.io.IOException;

/**
 * Simple HttpServer implementation using Jetty.
 *
 * @author Georg Felbinger (gfelbing@github.com)
 */
public final class HttpJettyServer implements HttpServer, LifeCycle {
    private final Server server;
    private final ContextHandlerCollection contextHandler;
    private HealthState healthState;

    /**
     * Constructor used by the guice module, takes HttpServerFactory.DEFAULT_THREAD_POOL_SIZE as thread-pool size.
     *
     * @param connectors Array of Connectors to which the jetty server will bind itself.
     */
    public HttpJettyServer(final Connector[] connectors) {
        this(connectors, HttpServerFactory.DEFAULT_THREAD_POOL_SIZE);
    }

    /**
     * Constructor creating a jetty server and initializes a ContextHandlerCollection,
     * which can be filled by addHandler().
     *
     * @param connectors     Array of Connectors to which the jetty server will bind itself.
     * @param threadPoolSize The size of the QueuedThreadPool used by jetty.
     */
    public HttpJettyServer(final Connector[] connectors, final Integer threadPoolSize) {
        this.healthState = HealthState.SHUTDOWN;
        this.server = new Server();
        this.server.setConnectors(connectors);
        this.server.setThreadPool(new QueuedThreadPool(threadPoolSize));
        this.contextHandler = new ContextHandlerCollection();
        this.server.setHandler(contextHandler);
    }

    /**
     * Adding a handler to the ContextHandlerCollection.
     * I.e., you can add an additional handler for static resources.
     *
     * @param handler The handler to add.
     * @return this to be able to chain.
     */
    public HttpJettyServer addHandler(final ContextHandler handler) {
        contextHandler.addHandler(handler);
        return this;
    }

    /**
     * Starts the wrapped jetty server.
     *
     * @throws IOException if it fails at startup.
     */
    @Override
    public void start() throws IOException {
        try {
            this.healthState = HealthState.STARTING;
            server.start();
            this.healthState = HealthState.OK;
        } catch (Exception e) {
            this.healthState = HealthState.ERROR;
            throw new IOException("Failed to start Jetty", e);
        }
    }

    /**
     * Stops the jetty.
     *
     * @throws IOException if the stop fails.
     */
    @Override
    public void stop() throws IOException {
        if (server != null) {
            try {
                this.healthState = HealthState.SHUTDOWN;
                server.stop();
            } catch (Exception e) {
                this.healthState = HealthState.ERROR;
                throw new IOException("Failed to stop Jetty", e);
            }
        }

    }

    @Override
    public HealthState getHealthState() {
        return this.healthState;
    }

    /**
     * Joins jetty's thread with the current thread, can be used to wait for shutdown after using stop().
     *
     * @throws InterruptedException if the join gets interrupted.
     */
    @Override
    public void waitForStop() throws InterruptedException {
        server.join();
    }
}
