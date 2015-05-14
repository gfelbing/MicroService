package de.gfelbing.microservice.vertical.foo;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Singleton;
import de.gfelbing.microservice.core.common.LifeCycle;
import de.gfelbing.microservice.core.discovery.server.D2Server;
import de.gfelbing.microservice.core.http.jetty.server.JettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Example Service handling used LifeCycles.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
@Singleton
public final class Service implements LifeCycle {

    /**
     * Starting the Service by creating an injector and initializing a Service-Instance.
     *
     * @param args aren't used yet.
     * @throws java.lang.Exception uncaught from Service in order let the process die if something goes wrong.
     */
    public static void main(final String[] args) throws Exception {
        Guice.createInjector(new ServiceModule())
            .getInstance(Service.class)
            .start();
    }

    private static final Logger LOG = LoggerFactory.getLogger(Service.class);

    private final ImmutableList<LifeCycle> servers;

    @Inject
    Service(final JettyServer httpServer, final D2Server d2Server) {
        this.servers = ImmutableList.of(httpServer, d2Server);
    }

    /**
     * Starts all servers and creates a shutdown-hook to gracefully shutdown on signals.
     *
     * @throws java.lang.Exception uncaught exceptions from the servers' start.
     */
    @Override
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
     *
     * @throws java.lang.Exception uncaught exceptions from the servers' stop.
     */
    @Override
    public void stop() throws Exception {
        for (LifeCycle server : servers) {
            LOG.info("Stopping " + server.getClass().getName());
            server.stop();
        }
    }

    /**
     * @return the current state.
     */
    @Override
    public State getState() {
        final Map<State, Integer> stateCounts = new HashMap<>();
        for (LifeCycle lifeCycle : servers) {
            stateCounts.put(lifeCycle.getState(), stateCounts.getOrDefault(lifeCycle.getState(), 0));
        }
        if (stateCounts.containsKey(State.ERROR)) {
            return State.ERROR;
        } else if (stateCounts.containsKey(State.STOPPED)) {
            return State.STOPPED;
        } else if (stateCounts.containsKey(State.STOPPING)) {
            return State.STOPPING;
        } else if (stateCounts.containsKey(State.STARTING)) {
            return State.STARTING;
        } else if (stateCounts.containsKey(State.CREATED)) {
            return State.CREATED;
        } else {
            return State.UP;
        }
    }
}
