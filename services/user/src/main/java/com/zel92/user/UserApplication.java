package com.zel92.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(UserApplication.class, args);
	}

}
