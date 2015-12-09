package io.pivotal.cassandra.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CloudFoundryCassandraServiceInfoTest {
	private final String json = "{\"p-cassandra\":[{\"credentials\":{\"cql_port\":9042,\"keyspace_name\":\"instance_2812caa51ae641c69f6250b259afbb27\",\"node_ips\":[\"10.0.21.6\",\"10.0.21.7\",\"10.0.21.8\",\"10.0.21.9\"],\"password\":\"19e39ce6-7012-43d2-b328-ab30075532ad\",\"thrift_port\":9160,\"username\":\"8e5e2329-7854-461d-8b3f-facbcee874c7\"},\"label\":\"p-cassandra\",\"name\":\"cassandra\",\"plan\":\"multi-tenant\",\"tags\":[\"pivotal\",\"cassandra\"]}]}";

	@Test
	public void testParsesPortFromJSON() {
		CassandraServiceInfo parser = new CloudFoundryCassandraServiceInfo(json);
		assertEquals(9042, (int) parser.cqlPort());
	}

	@Test
	public void testParsesKeyspaceNameFromJSON() {
		CassandraServiceInfo parser = new CloudFoundryCassandraServiceInfo(json);
		assertEquals("instance_2812caa51ae641c69f6250b259afbb27", parser.keyspaceName());
	}

	@Test
	public void testParsesNodeIpsFromJSON() {
		CassandraServiceInfo parser = new CloudFoundryCassandraServiceInfo(json);
		assertEquals("10.0.21.6,10.0.21.7,10.0.21.8,10.0.21.9", parser.nodeIps());
	}

	@Test
	public void testParsesUsernameFromJSON() {
		CassandraServiceInfo parser = new CloudFoundryCassandraServiceInfo(json);
		assertEquals("8e5e2329-7854-461d-8b3f-facbcee874c7", parser.username());
	}

	@Test
	public void testParsesPasswordFromJSON() {
		CassandraServiceInfo parser = new CloudFoundryCassandraServiceInfo(json);
		assertEquals("19e39ce6-7012-43d2-b328-ab30075532ad", parser.password());
	}
	
	@Test
	public void testParsesJSONUsingSectionWithCassandraName()
	{
		String json = "{\"p-cassandra\": [{\"credentials\": {\"cql_port\": 7890123}}],\"not-p-cassandra\":[{\"credentials\":{\"cql_port\":9042,\"keyspace_name\":\"instance_2812caa51ae641c69f6250b259afbb27\",\"node_ips\":[\"10.0.21.6\",\"10.0.21.7\",\"10.0.21.8\",\"10.0.21.9\"],\"password\":\"19e39ce6-7012-43d2-b328-ab30075532ad\",\"thrift_port\":9160,\"username\":\"8e5e2329-7854-461d-8b3f-facbcee874c7\"},\"label\":\"p-cassandra\",\"name\":\"cassandra\",\"plan\":\"multi-tenant\",\"tags\":[\"pivotal\",\"cassandra\"]}]}";
		CassandraServiceInfo parser = new CloudFoundryCassandraServiceInfo(json);
		assertEquals(9042, (int) parser.cqlPort());
	}
	
	@Test
	public void testReturnNullAsKeyspaceNameWhenThereIsNoJSONPassed()
	{
		CassandraServiceInfo parser = new CloudFoundryCassandraServiceInfo(null);
		assertNull(parser.keyspaceName());
	}
	
	@Test
	public void testReturnNullAsKeyspaceNameWhenThereIsNoCassandraSection() {
		CassandraServiceInfo parser = new CloudFoundryCassandraServiceInfo(
				"{\"p-cassandra\": [{\"credentials\": {\"cql_port\": 7890123}}]}");
		assertNull(parser.keyspaceName());
	}
}
