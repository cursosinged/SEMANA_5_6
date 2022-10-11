/*
 * Copyright (c) 2022 Nextiva, Inc. to Present.
 * All rights reserved.
 */

package com.wizeline.maven.learningjavamaven.controller;

import static com.wizeline.maven.learningjavamaven.utils.Utils.isPasswordValid;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomAcountNumber;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.result.UpdateResult;
import com.wizeline.maven.learningjavamaven.client.AccountsJSONClient;
import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;
import com.wizeline.maven.learningjavamaven.model.Post;
import com.wizeline.maven.learningjavamaven.model.RequestPeticionPutDTO;
import com.wizeline.maven.learningjavamaven.model.ResponseDTO;
import com.wizeline.maven.learningjavamaven.service.BankAccountService;
import com.wizeline.maven.learningjavamaven.utils.CommonServices;
import com.wizeline.maven.learningjavamaven.utils.singleton.ProcesarInfo;

/**
 * Class Description goes here. Created by enrique.gutierrez on 26/09/22
 */

@RequestMapping("/api")
@RestController
public class BankingAccountController {

	private static final String SUCCESS_CODE = "OK000";

	@Value("${server.port}")
	private String port;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private CommonServices commonServices;

	@Autowired
	private AccountsJSONClient accountsJSONClient;

	private static final Logger LOGGER = Logger.getLogger(BankingAccountController.class.getName());
	String msgProcPeticion = "LearningJava - Inicia procesamiento de peticion ...";

	@GetMapping("/getUserAccount")
	public ResponseEntity<?> getUserAccount(@RequestParam String user, @RequestParam String password,
			@RequestParam String date) {
		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();
		ResponseDTO response = new ResponseDTO();
		HttpHeaders responseHeaders = new HttpHeaders();
		String responseText = "Uno de los datos ingresados es Incorrecto";
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");

		ProcesarInfo validaFecha = ProcesarInfo.getInstance(date);

		// Valida fecha con patron de diseño singleton

		if (validaFecha.isDateFormatValid(date)) // isDateFormatValid(date)) {
		{
			LOGGER.info("FORMATO FECHA CORRECTO");
			// Valida el password del usuario (password)
			if (isPasswordValid(password)) {
				response = commonServices.login(user, password);
				if (response.getCode().equals(SUCCESS_CODE)) {
					BankAccountDTO bankAccountDTO = getAccountDetails(user, date);
					Instant finalDeEjecucion = Instant.now();
					LOGGER.info("LearningJava - Cerrando recursos ...");
					String total = new String(
							String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis())
									.concat(" segundos."));
					LOGGER.info("Tiempo de respuesta: ".concat(total));
					return new ResponseEntity<>(bankAccountDTO, responseHeaders, HttpStatus.OK);
				}

				else {
					responseText = "Logueo Incorrecto";
					return new ResponseEntity<>(responseText, responseHeaders, HttpStatus.BAD_REQUEST);

				}
			}
		} else {
			responseText = "Formato de Fecha Incorrecto";
		}
		Instant finalDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Cerrando recursos ...");
		String total = new String(
				String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
		LOGGER.info("Tiempo de respuesta: ".concat(total));
		return new ResponseEntity<>(responseText, responseHeaders, HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getAccounts")
	public ResponseEntity<List<BankAccountDTO>> getAccounts() {
		LOGGER.info("The port used is " + port);
		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
		LOGGER.info("Retrieving external endpoint ");

		List<BankAccountDTO> accounts = bankAccountService.getAccounts();
		Instant finalDeEjecucion = Instant.now();

		LOGGER.info("LearningJava - Cerrando recursos ...");
		String total = new String(
				String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
		LOGGER.info("Tiempo de respuesta: ".concat(total));

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<>(accounts, responseHeaders, HttpStatus.OK);

	}

	@GetMapping("/getAccountByUser")
	public ResponseEntity<List<BankAccountDTO>> getAccountByUser(@RequestParam String user) {
		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
		List<BankAccountDTO> accounts = bankAccountService.getAccountByUser(user);

		Instant finalDeEjecucion = Instant.now();

		LOGGER.info("LearningJava - Cerrando recursos ...");
		String total = new String(
				String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
		LOGGER.info("Tiempo de respuesta: ".concat(total));

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<>(accounts, responseHeaders, HttpStatus.OK);
	}

	@GetMapping("/getAccountsGroupByType")
	public ResponseEntity<Map<String, List<BankAccountDTO>>> getAccountsGroupByType() {

		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();

		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
		List<BankAccountDTO> accounts = bankAccountService.getAccounts();

		// Aqui implementaremos la programación funcional
		Map<String, List<BankAccountDTO>> groupedAccounts;
		Function<BankAccountDTO, String> groupFunction = (account) -> account.getAccountType().toString();
		groupedAccounts = accounts.stream().collect(Collectors.groupingBy(groupFunction));
		Instant finalDeEjecucion = Instant.now();

		LOGGER.info("LearningJava - Cerrando recursos ...");
		String total = new String(
				String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
		LOGGER.info("Tiempo de respuesta: ".concat(total));

		return new ResponseEntity<>(groupedAccounts, HttpStatus.OK);
	}

	@DeleteMapping("/deleteAccounts")
	public ResponseEntity<String> deleteAccounts() {
		bankAccountService.deleteAccounts();
		return new ResponseEntity<>("All accounts deleted", HttpStatus.OK);
	}

	private BankAccountDTO getAccountDetails(String user, String lastUsage) {
		return bankAccountService.getAccountDetails(user, lastUsage);
	}

	// The usage of FeignClient for demo purposes
	@GetMapping("/getExternalUser/{userId}")
	public ResponseEntity<Post> getExternalUser(@PathVariable Long userId) {

		Post postTest = accountsJSONClient.getPostById(userId);
		LOGGER.info("Getting post userId..." + postTest.getUserId());
		LOGGER.info("Getting post body..." + postTest.getBody());
		LOGGER.info("Getting post title..." + postTest.getTitle());
		postTest.setUserId("External user " + randomAcountNumber());
		postTest.setBody("No info in accountBalance since it is an external user");
		postTest.setTitle("No info in title since it is an external user");
		LOGGER.info("Setting post userId..." + postTest.getUserId());
		LOGGER.info("Setting post body..." + postTest.getBody());
		LOGGER.info("Setting post title...." + postTest.getTitle());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<>(postTest, responseHeaders, HttpStatus.OK);
	}
	
	@PutMapping("/updateAccounts")
	public ResponseEntity<Object> updateAccounts(@Valid @RequestBody RequestPeticionPutDTO user) {

		UpdateResult updResult;
		LOGGER.info("The port used is " + port);
		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo PUT");
		LOGGER.info("Retrieving external endpoint ");
		HttpHeaders responseHeaders;
		try {
			responseHeaders = new HttpHeaders();
			updResult=bankAccountService.actualizarDato(user.getUser());
		}

		catch (Exception e) {
			responseHeaders = new HttpHeaders();
			return new ResponseEntity<Object>("Error en la actualización de datos", responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		LOGGER.info("Numero de registros actualizados: " + updResult.getModifiedCount());
		Instant finalDeEjecucion = Instant.now();

		LOGGER.info("LearningJava - Cerrando recursos ...");
		String total = new String(
				String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
		LOGGER.info("Tiempo de respuesta: ".concat(total));

		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<Object>("Datos actualizados correctamente", responseHeaders, HttpStatus.OK);

	}

}
