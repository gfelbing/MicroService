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

package de.gfelbing.microservice.core.http.jetty.server;

import com.google.common.collect.ImmutableList;
import de.gfelbing.microservice.core.common.LifeCycle;
import de.gfelbing.microservice.core.util.GuavaCollect;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple HttpServer using Jetty, implementing #{LifeCycle}.
 *
 * @author Georg Felbinger (gfelbing@github.com)
 */
public final class JettyServer implements LifeCycle {

    private static final Integer DEFAULT_THREAD_POOL_SIZE = 512;

    private final Server server;
    private final ContextHandlerCollection contextHandler;
    private final AtomicReference<State> healthState;

    /**
     * Constructor creating a jetty server and initializes a ContextHandlerCollection,
     * which can be filled by addHandler().
     *
     * @param hosts    IPs / Hostnames jetty will bind itself to.
     * @param port     Port to which the jetty server will bind itself.
     */
    public JettyServer(final ImmutableList<String> hosts, final Integer port) {
        this.healthState = new AtomicReference<>(State.CREATED);
        this.server = new Server();

        final ImmutableList<Connector> connectors = hosts.stream().map(host -> {
            ServerConnector connector = new ServerConnector(server);
            connector.setHost(host);
            connector.setPort(port);
            return connector;
        }).collect(GuavaCollect.immutableList());

        this.server.setConnectors(connectors.toArray(new Connector[connectors.size()]));
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
    public JettyServer addHandler(final ContextHandler handler) {
        contextHandler.addHandler(handler);
        return this;
    }

    /**
     * Invokes @link{addHandler()} for each ContextHandler.
     * @param contextHandlers Handler to add.
     * @return itself for chaining.
     */
    public JettyServer addAll(final ImmutableList<ContextHandler> contextHandlers) {
        contextHandlers.forEach(contextHandler::addHandler);
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
            this.healthState.set(State.STARTING);
            server.start();
            this.healthState.set(State.UP);
        } catch (Exception e) {
            this.healthState.set(State.ERROR);
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
                this.healthState.set(State.STOPPING);
                server.stop();
                this.healthState.set(State.STOPPED);
            } catch (Exception e) {
                this.healthState.set(State.ERROR);
                throw new IOException("Failed to stop Jetty", e);
            }
        }

    }

    /**
     * @return the current state.
     */
    @Override
    public State getState() {
        return this.healthState.get();
    }
}
