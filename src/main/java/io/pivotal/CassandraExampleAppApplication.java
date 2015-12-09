package io.pivotal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CassandraExampleAppApplication {
	
	public static void main(String[] args) {
		
		try {
			SpringApplication.run(CassandraExampleAppApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
