package de.gfelbing.microservice.service.d2.client;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.inject.Singleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Reads the d2client.json from resources and parses it.
 *
 * @author gfelbing@github.com on 03.05.15.
 */
@Singleton
public final class D2ClientConfiguration {
    private final String zkConnectString;
    private final Long zkSessionTimeout;
    private final String zkBasePath;
    private final Long zkStartupTimeout;
    private final Long zkLoadBalancerNotificationTimeout;
    private final String zkFlagFile;
    private final String fsBasePath;
    private final Map<String, Long> trafficProportion;
    private final Long clientShutdownTimeout;
    private final Long clientStartTimeout;
    private final Long rate;

    D2ClientConfiguration() throws JSONException, IOException {
        String jsonString = Resources.toString(Resources.getResource("d2client.json"), Charsets.UTF_8);
        final JSONObject configJson = new JSONObject(jsonString);
        zkConnectString = configJson.getString("zkConnectString");
        zkSessionTimeout = configJson.getLong("zkSessionTimeout");
        zkBasePath = configJson.getString("zkBasePath");
        zkStartupTimeout = configJson.getLong("zkStartupTimeout");
        zkLoadBalancerNotificationTimeout = configJson.getLong("zkLoadBalancerNotificationTimeout");
        zkFlagFile = configJson.getString("zkFlagFile");
        fsBasePath = configJson.getString("fsBasePath");
        trafficProportion = asMap(configJson.getJSONObject("trafficProportion"));
        clientShutdownTimeout = configJson.getLong("clientShutdownTimeout");
        clientStartTimeout = configJson.getLong("clientStartTimeout");
        rate = configJson.getLong("rateMillisecond");
    }

    private static Map<String, Long> asMap(final JSONObject jsonObject) throws JSONException {
        final Map<String, Long> map = new HashMap<>();
        final Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            final String key = iterator.next();
            map.put(key, jsonObject.getLong(key));
        }
        return map;
    }

    /**
     * @return the Zookeeper-Connection-String, i.e. localhost:2181
     */
    public String getZkConnectString() {
        return zkConnectString;
    }

    /**
     * @return the Session-Timeout used by the Zookeeper-Client.
     */
    public Long getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    /**
     * @return the base path in zookeeper, in this path the are the nodes uris, services and clusters located.
     */
    public String getZkBasePath() {
        return zkBasePath;
    }

    /**
     * @return the Startup-Timeout used by the Zookeeper-Client.
     */
    public Long getZkStartupTimeout() {
        return zkStartupTimeout;
    }

    /**
     * @return the timeout for notifactions of ZooKeeper-Loadbalancer.
     */
    public Long getZkLoadBalancerNotificationTimeout() {
        return zkLoadBalancerNotificationTimeout;
    }

    /**
     * @return Local storage of ZooKeeper-Flags, should be in /tmp
     */
    public String getZkFlagFile() {
        return zkFlagFile;
    }

    /**
     * @return Base-Path of Local Storage for ZooKeeper-Nodes, should be in /tmp
     */
    public String getFsBasePath() {
        return fsBasePath;
    }

    /**
     * @return targets for traffic generator.
     */
    public Map<String, Long> getTrafficProportion() {
        return trafficProportion;
    }

    /**
     * @return The rate in millis, in which the targets for traffic generator should be called.
     */
    public Long getRate() {
        return rate;
    }

    /**
     * @return timeout for shutdown-locks.
     */
    public Long getClientShutdownTimeout() {
        return clientShutdownTimeout;
    }

    /**
     * @return timeout for StartUp of the zookeeper Client.
     */
    public Long getClientStartTimeout() {
        return clientStartTimeout;
    }
}
