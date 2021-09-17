package com.hyl.memoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MemoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoApiApplication.class, args);
	}

}
