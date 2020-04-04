package com.molam0la.dev.newsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.molam0la.dev.newsapi.ReactorContextLogWrapper.logOnError;

@SpringBootApplication
@EnableWebFlux
public class NewsApiApplication {


	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	public RestTemplate getRestTemplate() { return new RestTemplate(); }

	public static void main(String[] args) throws IOException {
		SpringApplication.run(NewsApiApplication.class, args);
		Logger log = LoggerFactory.getLogger(NewsApiApplication.class);

		log.error("Fake error");
		System.out.println(logOnError(err -> log.error("Exception processing record")));
	}


}
