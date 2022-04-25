package com.sparta.hh99finalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Hh99finalprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Hh99finalprojectApplication.class, args);
	}

}
