package de.gfelbing.microservice.core.discovery.server;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.d2.balancer.servers.ZKUriStoreFactory;
import com.linkedin.d2.balancer.servers.ZooKeeperAnnouncer;
import com.linkedin.d2.balancer.servers.ZooKeeperConnectionManager;
import com.linkedin.d2.balancer.servers.ZooKeeperServer;
import com.linkedin.d2.discovery.util.D2Config;
import de.gfelbing.microservice.core.util.GuavaCollect;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * D2Server Module for dynamic service discovery.
 * <p>
 * With this module you are able to inject:
 * - Vanilla linkedin #link{D2Config}
 * - Vanilla linkedin #link{ZooKeeperConnectionManager}
 * - Vanilla linkedin #link{ImmutableList<ZooKeeperAnnouncer>} (used by #link{ZooKeeperConnectionManager})
 * - A #link{D2Server} wrapping the vanilla classes and implementing #link{LifeCycle}
 * <p>
 * In order to use this module you have to provide an implementation of #link{D2ServerConfiguration}
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
            final ImmutableList<ZooKeeperAnnouncer> zooKeeperAnnouncers) {

        return new ZooKeeperConnectionManager(d2ServerConfiguration.getZkConnectString(),
                d2ServerConfiguration.getZkSessionTimeout(),
                d2ServerConfiguration.getZkBasePath(),
                new ZKUriStoreFactory(),
                d2ServerConfiguration.getZkRetryLimit(),
                zooKeeperAnnouncers.toArray(new ZooKeeperAnnouncer[zooKeeperAnnouncers.size()]));
    }

    @Provides
    @Singleton
    @Inject
    ImmutableList<ZooKeeperAnnouncer> zooKeeperAnnouncers(final D2ServerConfiguration d2ServerConfiguration) {
        final Map<String, Object> clusters = d2ServerConfiguration.getD2Clusters();
        return clusters.entrySet().stream()
                .map(entry -> {
                    return d2ServerConfiguration.getD2URIs().stream().map(uri -> {
                        ZooKeeperAnnouncer zooKeeperAnnouncer = new ZooKeeperAnnouncer(new ZooKeeperServer());
                        zooKeeperAnnouncer.setCluster(entry.getKey());
                        zooKeeperAnnouncer.setUri(uri);
                        zooKeeperAnnouncer.setWeightOrPartitionData(1);
                        return zooKeeperAnnouncer;
                    }).collect(GuavaCollect.immutableList());
                })
                .flatMap(Collection::stream)
                .collect(GuavaCollect.immutableList());
    }
}
