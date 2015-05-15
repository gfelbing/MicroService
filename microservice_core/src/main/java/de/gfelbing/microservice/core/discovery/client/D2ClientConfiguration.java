package de.gfelbing.microservice.core.discovery.client;

import java.util.Map;

/**
 * The specific service should provide an implementation of this interface in order to get a D2Client.
 *
 * @author gfelbing@github.com on 05.05.15.
 */
public interface D2ClientConfiguration {
    /**
     * @return the Zookeeper-Connection-String, i.e. localhost:2181
     */
    String getZkConnectString();

    /**
     * @return the Session-Timeout used by the Zookeeper-Client.
     */
    Long getZkSessionTimeout();

    /**
     * @return the base path in zookeeper, in this path the are the nodes uris, services and clusters located.
     */
    String getZkBasePath();

    /**
     * @return the Startup-Timeout used by the Zookeeper-Client.
     */
    Long getZkStartupTimeout();

    /**
     * @return the timeout for notifactions of ZooKeeper-Loadbalancer.
     */
    Long getZkLoadBalancerNotificationTimeout();

    /**
     * @return Local storage of ZooKeeper-Flags, should be in /tmp
     */
    String getZkFlagFile();

    /**
     * @return Base-Path of Local Storage for ZooKeeper-Nodes, should be in /tmp
     */
    String getFsBasePath();

    /**
     * @return targets for traffic generator.
     */
    Map<String, Long> getTrafficProportion();

    /**
     * @return The rate in millis, in which the targets for traffic generator should be called.
     */
    Long getRate();

    /**
     * @return timeout for shutdown-locks.
     */
    Long getClientShutdownTimeout();

    /**
     * @return timeout for StartUp of the zookeeper Client.
     */
    Long getClientStartTimeout();
}
