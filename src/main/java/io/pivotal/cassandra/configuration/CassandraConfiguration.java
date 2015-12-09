package io.pivotal.cassandra.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PlainTextAuthProvider;

import io.pivotal.cassandra.CassandraClientFactoryBean;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
@EnableCassandraRepositories(basePackages = { "io.pivotal" })
public class CassandraConfiguration {
	@Autowired
	LocalCassandraServiceInfo localInfo;
	@Autowired
	Environment env;

	CloudFoundryCassandraServiceInfo cfInfo;

	@Bean
	public CassandraClusterFactoryBean cluster() {
		CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
		cluster.setContactPoints(cassandraServiceInfo().nodeIps());
		cluster.setPort(cassandraServiceInfo().cqlPort());
		if (cassandraServiceInfo().username() != null && !cassandraServiceInfo().username().isEmpty()) {
			AuthProvider auth = new PlainTextAuthProvider(cassandraServiceInfo().username(),
					cassandraServiceInfo().password());
			cluster.setAuthProvider(auth);
		}
		return cluster;
	}

	@Bean
	public CassandraMappingContext mappingContext() {
		return new BasicCassandraMappingContext();
	}

	@Bean
	public CassandraConverter converter() {
		return new MappingCassandraConverter(mappingContext());
	}

	@Bean
	public CassandraSessionFactoryBean session() throws Exception {
		CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
		session.setCluster(cluster().getObject());
		session.setKeyspaceName(cassandraServiceInfo().keyspaceName());
		session.setConverter(converter());
		session.setSchemaAction(SchemaAction.NONE);

		return session;
	}

	@Bean
	public CassandraOperations cassandraTemplate() throws Exception {
		return new CassandraTemplate(session().getObject());
	}

	@Bean
	public CassandraClientFactoryBean cassandraClient() throws Exception {
		CassandraClientFactoryBean cassandraClientFactoryBean = new CassandraClientFactoryBean(session().getObject());
		return cassandraClientFactoryBean;
	}

	private CassandraServiceInfo cassandraServiceInfo() {
		// if (info == null) {
		// CassandraServiceInfoFactory factory = new
		// CassandraServiceInfoFactory();
		// info = factory.getServiceInfo();
		// }
		String property = env.getProperty("VCAP_SERVICES");
		if (cfInfo == null) {
			cfInfo = new CloudFoundryCassandraServiceInfo(env.getProperty("VCAP_SERVICES"));
		}
		if (cfInfo.keyspaceName() == null) {
			return localInfo;
		}
		return cfInfo;
	}
}
