package de.gfelbing.microservice.service.d2.server;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.d2.balancer.servers.ZKUriStoreFactory;
import com.linkedin.d2.balancer.servers.ZooKeeperAnnouncer;
import com.linkedin.d2.balancer.servers.ZooKeeperConnectionManager;
import com.linkedin.d2.balancer.servers.ZooKeeperServer;
import com.linkedin.d2.discovery.util.D2Config;
import de.gfelbing.microservice.service.http.HttpJettyServerModule;
import de.gfelbing.microservice.service.util.ImmutableListCollector;

import java.util.Collections;
import java.util.Map;

/**
 * Provides a D2Config-Server to Handle the D2 LifeCycle.
 *
 * @author gfelbing@github.com on 03.05.15.
 */
public final class D2ServerModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    @Inject
    D2Config d2Config(final D2ServerConfiguration d2ServerConfiguration) {
        return new D2Config(
                d2ServerConfiguration.getZkConnectString(),
                d2ServerConfiguration.getZkSessionTimeout(),
                d2ServerConfiguration.getZkBasePath(),
                d2ServerConfiguration.getZkSessionTimeout(),
                d2ServerConfiguration.getZkRetryLimit(),
                Collections.emptyMap(),
                d2ServerConfiguration.getDefaultServiceProperties(),
                d2ServerConfiguration.getD2Clusters(),
                Collections.emptyMap(),
                Collections.emptyMap());
    }

    @Provides
    @Singleton
    @Inject
    ZooKeeperConnectionManager zooKeeperConnectionManager(
            final D2ServerConfiguration d2ServerConfiguration,
            final ZooKeeperAnnouncer[] zooKeeperAnnouncers) {

        return new ZooKeeperConnectionManager(d2ServerConfiguration.getZkConnectString(),
                d2ServerConfiguration.getZkSessionTimeout(),
                d2ServerConfiguration.getZkBasePath(),
                new ZKUriStoreFactory(),
                d2ServerConfiguration.getZkRetryLimit(),
                zooKeeperAnnouncers);
    }

    @Provides
    @Singleton
    @Inject
    ZooKeeperAnnouncer[] zooKeeperAnnouncers(@HttpJettyServerModule.RestLiURI final String uri,
                                             final D2ServerConfiguration d2ServerConfiguration) {
        final Map<String, Object> clusters = d2ServerConfiguration.getD2Clusters();
        return clusters.entrySet().stream()
                .map(entry -> {
                    ZooKeeperAnnouncer zooKeeperAnnouncer = new ZooKeeperAnnouncer(new ZooKeeperServer());
                    zooKeeperAnnouncer.setCluster(entry.getKey());
                    zooKeeperAnnouncer.setUri(uri);
                    zooKeeperAnnouncer.setWeightOrPartitionData(1);
                    return zooKeeperAnnouncer;
                })
                .collect(new ImmutableListCollector<>()).toArray(new ZooKeeperAnnouncer[clusters.size()]);
    }
}
