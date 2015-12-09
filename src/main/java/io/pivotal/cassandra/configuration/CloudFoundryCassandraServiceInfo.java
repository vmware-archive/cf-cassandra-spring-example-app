package io.pivotal.cassandra.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

public class CloudFoundryCassandraServiceInfo implements CassandraServiceInfo {
	Map<String, Object> jsonMap;

	@SuppressWarnings("unchecked")
	public CloudFoundryCassandraServiceInfo(String json) {
		JsonParser jsonParser = JsonParserFactory.getJsonParser();
		try {
			jsonMap = jsonParser.parseMap(json);

			Optional<Map<String, Object>> cassandraCredentials = jsonMap.values().stream()
					.flatMap(list -> ((List<Map<String, Object>>) list).stream())
					.filter(section -> "cassandra".equals(section.get("name"))).findFirst();
			if (cassandraCredentials.isPresent()) {
				jsonMap = (Map<String, Object>) cassandraCredentials.get().get("credentials");
			} else {
				jsonMap = null;
			}
		} catch (java.lang.IllegalArgumentException ex) {
			jsonMap = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.pivotal.cassandra.CassandraServiceInfo#cqlPort()
	 */
	@Override
	public Integer cqlPort() {
		if (jsonMap == null)
			return null;

		return (Integer) jsonMap.get("cql_port");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.pivotal.cassandra.CassandraServiceInfo#keyspaceName()
	 */
	@Override
	public String keyspaceName() {
		if (jsonMap == null)
			return null;

		return (String) jsonMap.get("keyspace_name");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.pivotal.cassandra.CassandraServiceInfo#nodeIps()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String nodeIps() {
		if (jsonMap == null)
			return null;

		ArrayList<String> list = (ArrayList<String>) jsonMap.get("node_ips");

		return String.join(",", list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.pivotal.cassandra.CassandraServiceInfo#username()
	 */
	@Override
	public String username() {
		if (jsonMap == null)
			return null;

		return (String) jsonMap.get("username");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.pivotal.cassandra.CassandraServiceInfo#password()
	 */
	@Override
	public String password() {
		if (jsonMap == null)
			return null;

		return (String) jsonMap.get("password");
	}

}
