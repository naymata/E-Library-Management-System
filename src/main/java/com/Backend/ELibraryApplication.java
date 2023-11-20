package com.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ELibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ELibraryApplication.class, args);
	}

}
