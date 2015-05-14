package de.gfelbing.microservice.vertical.foo.config;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.inject.Singleton;
import de.gfelbing.microservice.core.discovery.server.D2ServerConfiguration;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads the d2server.json from resources and parses it.
 *
 * @author gfelbing@github.com on 03.05.15.
 */
@Singleton
public final class D2ServerConfig implements D2ServerConfiguration {

    private final String zkConnectString;
    private final Integer zkSessionTimeout;
    private final String zkBasePath;
    private final Integer zkRetryLimit;
    private final Map<String, Object> d2Clusters;
    private final Map<String, Object> defaultServiceProperties;

    D2ServerConfig() throws IOException, ParseException, JSONException {
        final String jsonString = Resources.toString(Resources.getResource("d2server.json"), Charsets.UTF_8);
        final JSONObject configJson = (JSONObject) new JSONParser().parse(jsonString);
        zkConnectString = (String) configJson.get("zkConnectString");
        zkSessionTimeout = ((Long) configJson.get("zkSessionTimeout")).intValue();
        zkBasePath = (String) configJson.get("zkBasePath");
        zkRetryLimit = ((Long) configJson.get("zkRetryLimit")).intValue();
        defaultServiceProperties = asMap((JSONObject) configJson.get("defaultServiceProperties"));
        d2Clusters = asMap((JSONObject) configJson.get("d2Clusters"));
    }

    private static Map<String, Object> asMap(final JSONObject jsonObject) throws JSONException {
        final Map<String, Object> map = new HashMap<>();
        for (Object entryObj : jsonObject.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            map.put((String) entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * @return the Zookeeper-Connection-String, i.e. localhost:2181
     */
    @Override
    public String getZkConnectString() {
        return zkConnectString;
    }

    /**
     * @return the Session-Timeout used by the Zookeeper-Client.
     */
    @Override
    public Integer getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    /**
     * @return the base path in zookeeper, in this path the are the nodes uris, services and clusters located.
     */
    @Override
    public String getZkBasePath() {
        return zkBasePath;
    }

    /**
     * @return Number of retries before the client throws an exception an dies when the zookeeper-cluster is down.
     */
    @Override
    public Integer getZkRetryLimit() {
        return zkRetryLimit;
    }

    /**
     * @return mapping of default service properties to their json configurations.
     * NOTE: Object has to be the parsed object from json-simple, do not use another parser,
     * the D2Config.configure() will die!
     */
    @Override
    public Map<String, Object> getDefaultServiceProperties() {
        return defaultServiceProperties;
    }

    /**
     * @return mapping of Cluster-Names to their json configurations.
     * NOTE: Object has to be the parsed object from json-simple, do not use another parser,
     * the D2Config.configure() will die!
     */
    @Override
    public Map<String, Object> getD2Clusters() {
        return d2Clusters;
    }

    /**
     * @return mapping of Cluster-Names to their json configurations.
     * NOTE: Object has to be the parsed object from json-simple, do not use another parser,
     * the D2Config.configure() will die!
     */
    @Override
    public List<String> getD2URIs() {
        return ImmutableList.of("/foo.v1");
    }

}
