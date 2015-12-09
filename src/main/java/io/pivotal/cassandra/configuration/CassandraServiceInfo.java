package io.pivotal.cassandra.configuration;

public interface CassandraServiceInfo {

	Integer cqlPort();

	String keyspaceName();

	String nodeIps();

	String username();

	String password();

}