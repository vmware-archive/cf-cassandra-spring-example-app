package io.pivotal;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@CassandraDataSet(value = { "simpleCF.cql" })
@EmbeddedCassandra()
@TestExecutionListeners(listeners = { CassandraUnitDependencyInjectionTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@SpringApplicationConfiguration(classes = CassandraExampleAppApplication.class)
@WebIntegrationTest
@TestPropertySource(properties = {"VCAP_SERVICES={\"p-cassandra\":[{\"credentials\":{\"cql_port\":9142,\"keyspace_name\":\"instance_2812caa51ae641c69f6250b259afbb27\",\"node_ips\":[\"localhost\"]},\"label\":\"p-cassandra\",\"name\":\"cassandra\",\"plan\":\"multi-tenant\",\"tags\":[\"pivotal\",\"cassandra\"]}]}"})
public class CassandraExampleAppWithCloudFounrdyConfigTest {
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private RestTemplate restTemplate = new TestRestTemplate();
	
    @Value("${local.server.port}")
    int port;

	@Test
	public void testCRUD() throws JsonProcessingException {
		String tableName = RandomStringUtils.random(6, true, false);
		String key = RandomStringUtils.random(6, true, false);
		String value = RandomStringUtils.random(6, true, false);
		String anotherValue = RandomStringUtils.random(6, true, false);

		HttpEntity<String> httpEntity = new HttpEntity<String>("");

		ResponseEntity<String> response = restTemplate
				.postForEntity(String.format("http://localhost:%d/%s", port, tableName), httpEntity, String.class);
		assertEquals(null, response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		response = restTemplate.postForEntity(String.format("http://localhost:%d/%s/%s/%s", port, tableName, key, value),
				httpEntity, String.class);
		assertEquals(null, response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		response = restTemplate.getForEntity(String.format("http://localhost:%d/%s/%s", port, tableName, key),
				String.class);
		assertEquals(value, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());

		response = restTemplate.postForEntity(
				String.format("http://localhost:%d/%s/%s/%s", port, tableName, key, anotherValue), httpEntity,
				String.class);
		assertEquals(null, response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		response = restTemplate.getForEntity(String.format("http://localhost:%d/%s/%s", port, tableName, key),
				String.class);
		assertEquals(anotherValue, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());

		restTemplate.delete(String.format("http://localhost:%d/%s/%s", port, tableName, key));

		response = restTemplate.getForEntity(String.format("http://localhost:%d/%s/%s", port, tableName, key),
				String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}