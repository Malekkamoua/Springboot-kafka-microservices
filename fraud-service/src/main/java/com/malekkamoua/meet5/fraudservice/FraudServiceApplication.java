package com.malekkamoua.meet5.fraudservice;

import com.malekkamoua.meet5.fraudservice.config.DatabaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class FraudServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudServiceApplication.class, args);
	}

}
