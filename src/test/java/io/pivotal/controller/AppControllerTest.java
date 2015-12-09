package io.pivotal.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.pivotal.CassandraExampleAppApplication;
import io.pivotal.cassandra.CassandraClient;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = { CassandraExampleAppApplication.class, TestContext.class })
@WebAppConfiguration
public class AppControllerTest {

	private MockMvc mockMvc;

	@Mock
	CassandraClient mockedClient;

	@InjectMocks
	private AppController appController;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(appController).build();
	}

	@Test
	public void testTableCreationEndpoint() throws Exception {
		mockMvc.perform(post("/newTable")).andExpect(status().is2xxSuccessful());
		verify(mockedClient, times(1)).createTable("newTable");
		verifyNoMoreInteractions(mockedClient);
	}

	@Test
	public void testSetKeyEndpointReturnsBadRequestIfTableDoesNotExist() throws Exception {
		Mockito.when(mockedClient.tableExists(Matchers.anyString())).thenReturn(false);
		mockMvc.perform(post("/newTable/myKey/myValue")).andExpect(status().isBadRequest());
		verify(mockedClient).tableExists(Matchers.anyString());
		verifyNoMoreInteractions(mockedClient);
	}

	@Test
	public void testSetKeyEndpointReturnsCreatedIfTableExists() throws Exception {
		Mockito.when(mockedClient.tableExists(Matchers.anyString())).thenReturn(true);
		mockMvc.perform(post("/newTable/myKey/myValue")).andExpect(status().isCreated());

		verify(mockedClient).tableExists(Matchers.anyString());
		verify(mockedClient, times(1)).setValue("newTable", "myKey", "myValue");
		verifyNoMoreInteractions(mockedClient);
	}

	@Test
	public void testGetValueEndpointReturnsNotFoundIfKeyDoesNotExist() throws Exception {
		mockMvc.perform(get("/newTable/myKey")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetValueEndpointReturnsValueForKey() throws Exception {
		Mockito.when(mockedClient.getValue(Matchers.anyString(), Matchers.anyString())).thenReturn("value");
		mockMvc.perform(get("/newTable/myKey")).andExpect(status().isOk())
				.andExpect(content().string("value"));
	}
	
	@Test
	public void testDeleteEndpointDeletesKeyFromCassandra() throws Exception {
		mockMvc.perform(delete("/newTable/myKey")).andExpect(status().isOk());
		verify(mockedClient, times(1)).deleteKey("newTable", "myKey");
	}
}
