package com.hyl.loanapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.hyl.loanapi.proxy")
public class LoanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanApiApplication.class, args);
	}

}
