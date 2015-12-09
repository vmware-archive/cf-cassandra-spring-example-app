package io.pivotal.cassandra.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocalCassandraServiceInfo implements CassandraServiceInfo {

	@Value("${spring.data.cassandra.port}")
	private String port;

	@Value("${spring.data.cassandra.keyspace-name}")
	private String keyspaceName;

	@Value("${spring.data.cassandra.contact-points}")
	private String contactPoints;

	@Value("${spring.data.cassandra.username}")
	private String username;

	@Value("${spring.data.cassandra.password}")
	private String password;

	@Override
	public Integer cqlPort() {
		return Integer.parseInt(port);
	}

	@Override
	public String keyspaceName() {
		return keyspaceName;
	}

	@Override
	public String nodeIps() {
		return contactPoints;
	}

	@Override
	public String username() {
		return username;
	}

	@Override
	public String password() {
		return password;
	}

}
