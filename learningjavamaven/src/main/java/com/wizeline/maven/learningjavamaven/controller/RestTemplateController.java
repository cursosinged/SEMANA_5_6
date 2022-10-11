package com.wizeline.maven.learningjavamaven.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/api")
@RestController
public class RestTemplateController {

	private String url = "https://jsonplaceholder.typicode.com/posts";

	@GetMapping("/consumoRestTemplate")
	public List<Object> consumoRest() {
		RestTemplate restTemplate = new RestTemplate();

		Object[] datoObtenido = restTemplate.getForObject(url, Object[].class);

		return Arrays.asList(datoObtenido);

	}

}
