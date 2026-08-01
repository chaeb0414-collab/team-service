package com.example.team_service;

import com.example.team_service.common.ParameterStoreLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeamServiceApplication {

	public static void main(String[] args) {
		ParameterStoreLoader.loadIfEnabled();
		SpringApplication.run(TeamServiceApplication.class, args);
	}

}
