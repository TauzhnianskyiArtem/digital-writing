package com.tcsp.digitalwrite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class DigitalWriteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalWriteApplication.class, args);
	}

}
