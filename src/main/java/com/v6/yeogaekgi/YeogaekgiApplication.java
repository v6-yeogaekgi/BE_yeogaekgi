package com.v6.yeogaekgi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class YeogaekgiApplication {

	public static void main(String[] args) {
		SpringApplication.run(YeogaekgiApplication.class, args);
	}

}
