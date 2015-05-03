package de.gfelbing.microservice.service.d2.client;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.d2.balancer.D2Client;
import com.linkedin.d2.balancer.D2ClientBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Provides a D2 Client.
 *
 * @author gfelbing@github.com on 03.05.15.
 */
public final class D2ClientModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    @Inject
    D2Client createClient(final D2ClientConfiguration clientConfiguration) {
        return new D2ClientBuilder()
                .setZkHosts(clientConfiguration.getZkConnectString())
                .setZkSessionTimeout(clientConfiguration.getZkSessionTimeout(), TimeUnit.MILLISECONDS)
                .setZkStartupTimeout(clientConfiguration.getZkStartupTimeout(), TimeUnit.MILLISECONDS)
                .setLbWaitTimeout(clientConfiguration.getZkLoadBalancerNotificationTimeout(), TimeUnit.MILLISECONDS)
                .setFlagFile(clientConfiguration.getZkFlagFile())
                .setBasePath(clientConfiguration.getZkBasePath())
                .setFsBasePath(clientConfiguration.getFsBasePath())
                .build();
    }
}
