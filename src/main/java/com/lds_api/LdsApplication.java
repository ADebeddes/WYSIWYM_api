package com.lds_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/***
 * 
 * @author Alexandre DEBEDDES
 *
 */
@SpringBootApplication
/*@EnableConfigurationProperties
@EnableDiscoveryClient*/
public class LdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdsApplication.class, args);
	}

}
