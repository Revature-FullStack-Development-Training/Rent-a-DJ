package com.revature.P2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.revature.models") //This tells Spring Boot to look in the models package for DB entities
@ComponentScan("com.revature") //This tells Spring Boot to look in com.revature for Beans (stereotype annotations)
@EnableJpaRepositories("com.revature.daos") //This tells Spring Boot to look in the daos package for JPARepositories

public class RentADJApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentADJApplication.class, args);
	}

}
