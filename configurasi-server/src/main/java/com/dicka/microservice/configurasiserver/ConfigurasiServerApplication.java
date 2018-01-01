package com.dicka.microservice.configurasiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;


@EnableConfigServer
@SpringBootApplication
public class ConfigurasiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigurasiServerApplication.class, args);
	}
}
