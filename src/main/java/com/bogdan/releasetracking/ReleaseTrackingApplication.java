package com.bogdan.releasetracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableCaching
@EnableKafka
public class ReleaseTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReleaseTrackingApplication.class, args);
	}

}
