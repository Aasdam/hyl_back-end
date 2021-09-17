package com.hyl.mailserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MailServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailServerApplication.class, args);
	}

}
