package io.pivotal.samples;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PlainTextAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@EnableCassandraRepositories
@Import(RepositoryRestMvcConfiguration.class)
@EnableAutoConfiguration
@EnableConfigurationProperties(CassandraProperties.class)
public class ApplicationConfiguration extends AbstractCassandraConfiguration {

    @Autowired
    private CassandraConnectionFactory cassandraConnectionFactory;

    @Override
    protected String getKeyspaceName() {
        return cassandraConnectionFactory.getProperties().getKeyspaceName();
    }

    @Override
    protected String getContactPoints() {
        return cassandraConnectionFactory.getProperties().getContactPoints();
    }

    @Override
    protected int getPort() {
        return cassandraConnectionFactory.getProperties().getPort();
    }

    @Override
    protected AuthProvider getAuthProvider() {
        return new PlainTextAuthProvider(cassandraConnectionFactory.getProperties().getUsername(), cassandraConnectionFactory.getProperties().getPassword());
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.RECREATE_DROP_UNUSED; // Please note that, in production, you would want a different schema
    }

    @Bean
    public CassandraOperations operations() throws Exception {
        return new CassandraTemplate(session().getObject(), new MappingCassandraConverter(new BasicCassandraMappingContext()));
    }

    @Bean
    public CassandraConnectionFactory cassandraConnectionFactory() {
        return new CassandraConnectionFactory();
    }

}
