package com.pdy.fac.adminserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAutoConfiguration
@EnableAdminServer
@SpringBootApplication
public class AdminserverApplication {

	public static void main(final String[] args) {
		SpringApplication.run(AdminserverApplication.class, args);
	}
}
