package de.gfelbing.microservice.core.discovery.server;

import java.util.List;
import java.util.Map;

/**
 * The specific service should provide an implementation of this interface in order to get a D2Server.
 *
 * @author gfelbing@github.com on 05.05.15.
 */
public interface D2ServerConfiguration {
    /**
     * @return the Zookeeper-Connection-String, i.e. localhost:2181
     */
    String getZkConnectString();

    /**
     * @return the Session-Timeout used by the Zookeeper-Client.
     */
    Integer getZkSessionTimeout();

    /**
     * @return the base path in zookeeper, in this path the are the nodes uris, services and clusters located.
     */
    String getZkBasePath();

    /**
     * @return Number of retries before the client throws an exception an dies when the zookeeper-cluster is down.
     */
    Integer getZkRetryLimit();

    /**
     * @return mapping of default service properties to their json configurations.
     * NOTE: Object has to be the parsed object from json-simple, do not use another parser,
     * the D2Config.configure() will die!
     */
    Map<String, Object> getDefaultServiceProperties();

    /**
     * @return mapping of Cluster-Names to their json configurations.
     * NOTE: Object has to be the parsed object from json-simple, do not use another parser,
     * the D2Config.configure() will die!
     */
    Map<String, Object> getD2Clusters();

    /**
     * @return mapping of Cluster-Names to their json configurations.
     * NOTE: Object has to be the parsed object from json-simple, do not use another parser,
     * the D2Config.configure() will die!
     */
    List<String> getD2URIs();

}
