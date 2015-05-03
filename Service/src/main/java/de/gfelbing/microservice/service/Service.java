package de.gfelbing.microservice.service;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.gfelbing.microservice.api.HealthState;
import de.gfelbing.microservice.service.d2.server.D2Server;
import de.gfelbing.microservice.service.http.HttpJettyServer;
import de.gfelbing.microservice.service.util.ImmutableListCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the LifeCycle of all classes having a lifecycle.
 *
 * @author gfelbing@github.com on 03.05.15.
 */
@Singleton
public final class Service {

    private static final Logger LOG = LoggerFactory.getLogger(Service.class);

    private final ImmutableList<LifeCycle> servers;

    @Inject
    Service(final HttpJettyServer httpServer, final D2Server d2Server) {
        this.servers = ImmutableList.of(httpServer, d2Server);
    }

    /**
     * Starts all servers and creates a shutdown-hook to gracefully shutdown on signals.
     * @throws java.lang.Exception uncaught exceptions from the servers' start.
     */
    public void start() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                stop();
            } catch (Exception e) {
                LOG.error("Error during shutdown.", e);
            }
        }));
        for (LifeCycle server : servers) {
            LOG.info("Bootstrapping " + server.getClass().getName());
            server.start();
        }
    }

    /**
     * Gracefully shutting down of all servers.
     * @throws java.lang.Exception uncaught exceptions from the servers' stop.
     */
    public void stop() throws Exception {
        for (LifeCycle server : servers) {
            LOG.info("Stopping " + server.getClass().getName());
            server.stop();
        }
    }

    /**
     * @return all HealthStates of all Servers.
     */
    public ImmutableList<HealthState> getHealthState() {
        return servers.stream().map(s -> s.getHealthState()).collect(new ImmutableListCollector<>());
    }
}
