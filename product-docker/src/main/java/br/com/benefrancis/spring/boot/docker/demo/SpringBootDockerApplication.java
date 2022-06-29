package br.com.benefrancis.spring.boot.docker.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringBootDockerApplication {

	private static final Logger log = LoggerFactory.getLogger(SpringBootDockerApplication.class);

	@GetMapping("/")
	public String getMEssage() {
		log.info("Bem vindo ao Mundo!!!");
		return "Bem vindo ao Mundo!!!";
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDockerApplication.class, args);
	}

}
