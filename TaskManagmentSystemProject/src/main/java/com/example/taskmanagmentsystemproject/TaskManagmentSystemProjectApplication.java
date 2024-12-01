package com.example.taskmanagmentsystemproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Включает функциональность планировщика в Spring Boot
public class TaskManagmentSystemProjectApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(TaskManagmentSystemProjectApplication.class, args);
	}
}
