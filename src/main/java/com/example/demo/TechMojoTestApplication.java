package com.example.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TechMojoTestApplication {
	
	@Autowired
	private TweetService tweetService; 

	public static void main(String[] args) {
		SpringApplication.run(TechMojoTestApplication.class, args);
	}
	
	@PostConstruct
	public void startUp() {
		tweetService.acceptInputAndDisplayTopHashTags();
	}

	

}
