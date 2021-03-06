package de.gfelbing.microservice.core.discovery.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.balancer.servers.ZooKeeperConnectionManager;
import com.linkedin.d2.discovery.util.D2Config;
import de.gfelbing.microservice.core.common.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Handles the ZooKeeper-Connection for RestLi D2 Layer.
 *
 * @author gfelbing@github.com on 03.05.15.
 */
@Singleton
public final class D2Server implements LifeCycle {

    private static final Logger LOG = LoggerFactory.getLogger(D2Server.class);

    private final D2Config d2Config;
    private final ZooKeeperConnectionManager zooKeeperConnectionManager;
    private final AtomicReference<State> healthState;

    @Inject
    D2Server(final D2Config d2Config,
             final ZooKeeperConnectionManager zooKeeperConnectionManager) {
        this.healthState = new AtomicReference<>(State.CREATED);
        this.d2Config = d2Config;
        this.zooKeeperConnectionManager = zooKeeperConnectionManager;
    }

    /**
     * Connecting to ZooKeeper to announce cluster, service and instance configuration for D2.
     */
    @Override
    public void start() throws Exception {
        this.healthState.set(State.STARTING);
        LOG.info("Configuring D2 Service.");
        d2Config.configure();
        LOG.info("Initiating ZooKeeper-Connection");
        zooKeeperConnectionManager.start(new Callback<None>() {
            @Override
            public void onError(final Throwable e) {
                LOG.error("Couldn't start ZooKeeperConnection, exiting...");
                healthState.set(State.ERROR);
            }

            @Override
            public void onSuccess(final None result) {
                LOG.info("ZooKeeper-Connection sucessfully started.");
                healthState.set(State.UP);
            }
        });
    }

    /**
     * Stops the Connection to ZooKeeper.
     */
    public void stop() {
        zooKeeperConnectionManager.shutdown(new Callback<None>() {
            @Override
            public void onError(final Throwable e) {
                LOG.error("Couldn't stop ZooKeeperConnection", e);
                healthState.set(State.ERROR);
            }

            @Override
            public void onSuccess(final None result) {
                LOG.info("ZooKeeper-Connection sucessfully stopped.");
                healthState.set(State.STOPPED);
            }
        });
    }

    /**
     * @return the current lifecycle-state
     */
    @Override
    public State getState() {
        return this.healthState.get();
    }

}
