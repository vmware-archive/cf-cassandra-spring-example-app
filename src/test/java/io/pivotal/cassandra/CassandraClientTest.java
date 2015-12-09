package io.pivotal.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import io.pivotal.cassandra.configuration.CassandraConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ CassandraUnitTestExecutionListener.class })
@CassandraDataSet(value = { "simple.cql" })
@EmbeddedCassandra()
@ContextConfiguration(classes = { CassandraConfiguration.class })
public class CassandraClientTest {

	CassandraConverter converter = new MappingCassandraConverter(new BasicCassandraMappingContext());

	private CassandraClient client;

	private final String tableName = RandomStringUtils.random(6, true, false);
	private final String key = RandomStringUtils.random(6, true, false);
	private final String value = RandomStringUtils.random(6, true, false);

	@Before
	public void setup() {
		Cluster cluster = Cluster.builder().addContactPoint("localhost").withPort(9142).build();
		Session session = cluster.connect("mykeyspace");

		client = new CassandraClient(session);
	}

	@Test
	public void testCreateTable() throws Exception {
		assertFalse(client.tableExists(tableName));
		client.createTable(tableName);
		assertTrue(client.tableExists(tableName));
	}

	@Test
	public void testGetValueReturnsValueWhenKeyExists() throws Exception {
		client.createTable(tableName);

		assertNull(client.getValue(tableName, key));
		client.setValue(tableName, key, value);
		assertEquals(value, client.getValue(tableName, key));
	}

	@Test
	public void testGetValueReturnsNullWhenTableDoesNotExist() {
		assertNull(client.getValue(tableName, key));
	}

	@Test
	public void testSetValueReturnsFalseWhenTableDoesNotExist() {
		assertFalse(client.setValue(tableName, key, value));
	}

	@Test
	public void testSetValueReturnsTrueWhenValueInsertedInCassandra() throws Exception {
		client.createTable(tableName);
		assertTrue(client.setValue(tableName, key, value));
	}

	@Test
	public void testDeleteKeyDeletesFromCassandra() throws Exception {
		client.createTable(tableName);
		client.setValue(tableName, key, value);
		assertEquals(value, client.getValue(tableName, key));

		client.deleteKey(tableName, key);
		assertNull(client.getValue(tableName, key));
	}

	@Test
	public void testDeleteDoesNotRaiseExceptionWhenKeyDoesNotExist() throws Exception {
		client.createTable(tableName);
		client.deleteKey(tableName, key);
	}

	@Test
	public void testDeleteDoesNotRaiseExceptionWhenTableDoesNotExist() {
		client.deleteKey(tableName, key);
	}
}
