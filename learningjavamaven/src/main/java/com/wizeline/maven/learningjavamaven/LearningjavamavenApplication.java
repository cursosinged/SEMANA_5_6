package com.wizeline.maven.learningjavamaven;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Descripcion: Metodo principal del proyecto LearningJava con Spring Boot
 *
 * @param args
 * @throws IOException
 * Edwin Moreno
 */

@SpringBootApplication
@ComponentScan("com.wizeline.maven")
@EnableFeignClients
@EnableKafka
public class LearningjavamavenApplication{
	public static void main(String[] args) throws IOException {
		SpringApplication.run(LearningjavamavenApplication.class, args);
	}
}
