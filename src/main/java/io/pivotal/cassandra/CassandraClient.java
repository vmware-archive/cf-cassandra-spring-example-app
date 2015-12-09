package io.pivotal.cassandra;

import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;

import com.datastax.driver.core.Session;

public class CassandraClient {

	private final Session session;

	public CassandraClient(Session session) {
		this.session = session;
	}

	public void createTable(String tableName) throws Exception {
		CassandraOperations cassandraOps = new CassandraTemplate(session);
		cassandraOps.execute("create table " + tableName + " (key text primary key, value text)");
	}

	public boolean tableExists(String tableName) {
		CassandraConverter converter = new MappingCassandraConverter(new BasicCassandraMappingContext());
		CassandraAdminOperations adm = new CassandraAdminTemplate(session, converter);

		return adm.getTableMetadata(session.getLoggedKeyspace(), new CqlIdentifier(tableName)) != null;
	}

	public String getValue(String tableName, String key) {
		if (!tableExists(tableName)) {
			return null;
		}

		CassandraOperations cassandraOps = new CassandraTemplate(session);
		return cassandraOps.queryForObject("select value from " + tableName + " where key = '" + key + "';",
				String.class);
	}

	public boolean setValue(String tableName, String key, String value) {
		if (!tableExists(tableName)) {
			return false;
		}

		CassandraOperations cassandraOps = new CassandraTemplate(session);
		cassandraOps.execute("insert into " + tableName + " (key, value) VALUES('" + key + "', '" + value + "');");
		return true;
	}

	public void deleteKey(String tableName, String key) {
		if (!tableExists(tableName)) {
			return;
		}
		
		CassandraOperations cassandraOps = new CassandraTemplate(session);
		cassandraOps.execute("delete from " + tableName + " where key = '" + key + "';");
	}
}
