package io.pivotal.cassandra;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import com.datastax.driver.core.Session;

public class CassandraClientFactoryBean implements FactoryBean<CassandraClient> {

	private final Session session;
	
	public CassandraClientFactoryBean(Session session) {
		this.session = session;
	}

	@Override
	public CassandraClient getObject() throws Exception {
		Assert.notNull(session);

		CassandraClient cassandraClient = new CassandraClient(session);
		return cassandraClient;
	}

	@Override
	public Class<? extends CassandraClient> getObjectType() {
		return CassandraClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
