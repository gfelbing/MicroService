package de.gfelbing.microservice.core.discovery.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.balancer.D2Client;
import com.linkedin.d2.balancer.clients.DynamicClient;
import de.gfelbing.microservice.core.common.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * TODO: Add statement here.
 *
 * @author gfelbing@github.com on 06.05.15.
 */
@Singleton
public class D2DynamicLifeCycleClient implements LifeCycle {

    private static final Logger LOG = LoggerFactory.getLogger(D2DynamicLifeCycleClient.class);

    private final DynamicClient dynamicClient;
    private final AtomicReference<State> lifeCycleState;

    @Inject
    public D2DynamicLifeCycleClient(DynamicClient dynamicClient) {
        this.dynamicClient = dynamicClient;
        this.lifeCycleState = new AtomicReference<>(State.CREATED);
    }

    /**
     * @return the d2 dynamic client
     */
    public D2Client get() {
        return dynamicClient;
    }

    /**
     * Starts the component.
     *
     * @throws Exception if the component fails to start
     */
    @Override
    public void start() throws Exception {
        lifeCycleState.set(State.STARTING);
        dynamicClient.start(new Callback<None>() {
            @Override
            public void onError(Throwable e) {
                LOG.error("There was an error starting the D2Client", e);
                lifeCycleState.set(State.ERROR);
            }

            @Override
            public void onSuccess(None result) {
                LOG.info("D2 Client successfully started.");
                lifeCycleState.set(State.UP);
            }
        });
    }

    /**
     * Stops the component.
     *
     * @throws Exception if the component fails to stop
     */
    @Override
    public void stop() throws Exception {
        lifeCycleState.set(State.STOPPING);
        dynamicClient.shutdown(new Callback<None>() {
            @Override
            public void onError(Throwable e) {
                LOG.error("There was an error stopping the D2Client", e);
                lifeCycleState.set(State.ERROR);
            }

            @Override
            public void onSuccess(None result) {
                LOG.error("D2 Client successfully stopped.");
                lifeCycleState.set(State.STOPPED);
            }
        });
    }

    /**
     * @return the current state.
     */
    @Override
    public State getState() {
        return lifeCycleState.get();
    }
}
