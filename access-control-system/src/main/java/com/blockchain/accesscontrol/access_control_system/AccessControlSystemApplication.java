package com.blockchain.accesscontrol.access_control_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AccessControlSystemApplication {

	public static void main(String[] args) 
	{
		SpringApplication.run(AccessControlSystemApplication.class, args);
	}

}