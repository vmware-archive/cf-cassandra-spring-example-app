package io.pivotal.samples;

import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CloudCassandraPropertiesManager extends CassandraProperties {
    private static final String VCAP_SERVICES = "VCAP_SERVICES";

    public CloudCassandraPropertiesManager() {
        super();
        if (hasCloudProperties()) {
            setProperties();
        }
    }

    public boolean hasCloudProperties() {
        return getVCapVariable() != null;
    }

    public Map<String, Object> getProperties() {
        Map<String, Object> jsonMap = getJsonMapFromEnvironment();
        return jsonMap;
    }

    private Map<String, Object> getJsonMapFromEnvironment() {
        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        Map<String, Object> jsonMap;
        jsonMap = jsonParser.parseMap(getVCapVariable());
        return (Map<String, Object>) ((List<Map<String, Object>>) jsonMap.get( "p-cassandra")).get(0).get("credentials");
    }

    public void setProperties() {
        Map<String, Object> jsonMap = this.getProperties();
        ArrayList<String> nodeIps= (ArrayList<String>) jsonMap.get("node_ips");

        this.setPort( (Integer) jsonMap.get("cql_port"));
        this.setContactPoints(String.join(",", nodeIps));
        this.setKeyspaceName((String) jsonMap.get("keyspace_name"));
        this.setPassword((String) jsonMap.get("password"));
        this.setUsername((String) jsonMap.get("username"));
    }

    public String getVCapVariable() {
        return System.getenv(VCAP_SERVICES);
    }
}
