package io.pivotal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.cassandra.CassandraClient;

@RestController
public class AppController {

	@Autowired
	CassandraClient client;

	@RequestMapping(value = "/{tableName}", method = RequestMethod.POST)
	ResponseEntity<String> createTable(@PathVariable("tableName") String tableName) throws Exception {
		client.createTable(tableName);
		return new ResponseEntity<String>("", HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{tableName}/{key}/{value}", method = RequestMethod.POST)
	ResponseEntity<String> createValue(@PathVariable("tableName") String tableName, @PathVariable("key") String key,
			@PathVariable("value") String value) {
		if (!client.tableExists(tableName)) {
			return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		}
		client.setValue(tableName, key, value);
		return new ResponseEntity<String>("", HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{tableName}/{key}", method = RequestMethod.GET)
	ResponseEntity<String> returnValue(@PathVariable("tableName") String tableName, @PathVariable("key") String key) {
		String value = client.getValue(tableName, key);
		if (value == null) {
			return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>(value, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/{tableName}/{key}", method = RequestMethod.DELETE)
	ResponseEntity<String> deleteKey(@PathVariable("tableName") String tableName, @PathVariable("key") String key) {
		client.deleteKey(tableName, key);	
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}
