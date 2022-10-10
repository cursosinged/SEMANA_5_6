package com.wizeline.maven.learningjavamaven.controller.EdgeCases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.wizeline.maven.learningjavamaven.controller.BankingAccountController;
import com.wizeline.maven.learningjavamaven.controller.UserController;
import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;
import com.wizeline.maven.learningjavamaven.model.ResponseDTO;
import com.wizeline.maven.learningjavamaven.model.UserDTO;

@SpringBootTest
public class PruebaServiciosConError {

	private static final Logger LOG = Logger.getLogger(PruebaServiciosConError.class.getName());

	private String user = null;
	private String password = null;
	private String fecha = null;

	@Mock
	private ResponseEntity<ResponseDTO> responseService;

	@Mock
	private ResponseEntity<List<ResponseDTO>> responseServices;

	@Spy
	private UserDTO userDTO;

	@Mock
	private List<UserDTO> userDTOList;

	@Mock
	private ResponseEntity<List<BankAccountDTO>> respuestaGetAccount;

	@Autowired
	private UserController userController;

	@Autowired
	private BankingAccountController bankingAccountController;

	@BeforeEach
	void antesDeCadaPrueba() {
		if (user == null && password == null) {

			user = "edwin.moreno@elektra.com";
			password = "pass2";
			fecha = "07/10/2022";
		}

	}

	@Test
	@DisplayName("PruebaServicioLoginEdgeCase")
	public void pruebaServicioLoginError() {

		LOG.info("LearningJava - Iniciado servicio REST para servicio /login");
		responseService = userController.loginUser(user, password);
		LOG.info("RESULTADO SERVICIO: " + responseService.getBody().getStatus());
		assertEquals("fail", responseService.getBody().getStatus(), "Error, Usuario no registrado");

	}

	@Test
	@DisplayName("PruebaServicioCreateUserEdgeCase")
	public void pruebaServicioCreateUserError() {
		LOG.info("LearningJava - Iniciado servicio REST para servicio /createUser");

		userDTO.setUser(null);
		userDTO.setPassword(null);

		responseService = userController.createUserAccount(userDTO);

		LOG.info("Resultado: " + responseService.getBody().getErrors().getErrorCode());

		assertEquals("ER001", responseService.getBody().getErrors().getErrorCode(),
				"Error al crear usuario, parametros no validos");

	}

	@Test
	@DisplayName("PruebaServicioCreateUsersEdgeCase")
	public void pruebaServicioCreateUsersError() {
		LOG.info("LearningJava - Iniciado servicio REST para servicio /createUsers");
		int repeticiones = 2;
		List<ResponseDTO> expectedResponse = new ArrayList<ResponseDTO>();

		for (int i = 0; i <= repeticiones; i++) {

			if (i == 1) {
				userDTO.setUser(null);
				userDTO.setPassword(null);

				userDTOList.add(userDTO);

				responseServices = userController.createUsersAccount(userDTOList);

				LOG.info("Resultado: " + responseServices.getBody());

				assertEquals(expectedResponse, responseServices.getBody(), "Datos incorrectos");

			} else {
				userDTO.setUser(null);
				userDTO.setPassword(null);
			}

			userDTOList.add(userDTO);

		}

	}

	@Test
	@DisplayName("Se prueba servicio getUserAccountEdgeCase")
	public void getUserAccountTestError() {
		String result = null;
		boolean concuerda;
		result = (String) bankingAccountController.getUserAccount(user, password, fecha).getBody();
		concuerda = result.contains("Incorrecto");
		LOG.info("Resultado: " + result);
		assertEquals(true, concuerda, "Problema al obtener datos de usuario");

	}

	@Test
	@DisplayName("Se prueba servicio getAccountsEdgeCase")
	public void getAccountsTestError() {
		respuestaGetAccount = bankingAccountController.getAccounts();
		LOG.info("Resultado: " + respuestaGetAccount.getBody());

		Assertions.assertAll(

				() -> Assertions.assertNotEquals(null, respuestaGetAccount.getBody()));
	}

	@Test
	@DisplayName("Se prueba servicio getAccountByUserEdgeCase")
	public void getAccountByUserTestError() {
		List<BankAccountDTO> accounts = new ArrayList<BankAccountDTO>();
		respuestaGetAccount = bankingAccountController.getAccountByUser(user);
		LOG.info("Resultado: " + respuestaGetAccount.getBody());

		Assertions.assertAll(

				() -> Assertions.assertEquals(accounts, respuestaGetAccount.getBody()));
	}

}
