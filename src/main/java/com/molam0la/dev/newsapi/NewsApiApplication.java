package com.molam0la.dev.newsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@SpringBootApplication
@EnableWebFlux
public class NewsApiApplication {

	@Bean
	public String getBaseUrl() { return "baseUrl"; }

	@Bean
	public WebClient.Builder getWebClientBuilder() { return WebClient.builder(); }

	@Bean
	public RestTemplate getRestTemplate() { return new RestTemplate(); }

	public static void main(String[] args) throws IOException {
		SpringApplication.run(NewsApiApplication.class, args);
	}

}
