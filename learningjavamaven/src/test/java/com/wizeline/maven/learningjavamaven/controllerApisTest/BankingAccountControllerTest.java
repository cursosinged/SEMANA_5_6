package com.wizeline.maven.learningjavamaven.controllerApisTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wizeline.maven.learningjavamaven.controller.BankingAccountController;
import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;
import com.wizeline.maven.learningjavamaven.model.Post;
import com.wizeline.maven.learningjavamaven.model.ResponseDTO;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class BankingAccountControllerTest {

	private static final Logger LOG = Logger.getLogger(BankingAccountControllerTest.class.getName());

	private String user = null;
	private String password = null;
	private String fecha = null;

	@Autowired
	private BankingAccountController bankingAccountController;

	@Mock
	private ResponseEntity<ResponseDTO> responseService;

	@Mock
	private ResponseEntity<String> responseServiceGenerico;

	@Mock
	private ResponseEntity<List<BankAccountDTO>> responseServices;

	@Mock
	private ResponseEntity<Map<String, List<BankAccountDTO>>> responseAccountByType;

	@Mock
	private ResponseEntity<Post> externalUserResponse;

	Long datoInicial = (long) 1;

	@BeforeEach
	void antesDeCadaPrueba() {

		if (user == null && password == null) {

			user = "user1@wizeline.com";// para error: "user2@wizeline.com";
			password = "Pass1@"; // para error: "pass2";
			fecha = "07-10-2022";

		}
	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Se prueba servicio getUserAccount")
	public void getUserAccountTest() { /// 400
		// user=user1@wizeline.com&password=Pass1@&date=12-03-2018
		responseService = (ResponseEntity<ResponseDTO>) bankingAccountController.getUserAccount(user, password, fecha);
		LOG.info("Resultado: " + responseService.getStatusCode());
		assertEquals(HttpStatus.OK, responseService.getStatusCode());
	}

	@Test
	@DisplayName("Se prueba servicio getAccounts")
	public void getAccountsTest() { // null
		responseServices = bankingAccountController.getAccounts();
		LOG.info("Resultado: " + responseServices.getBody());

		assertNotNull(responseServices.getBody());

	}

	@Test
	@DisplayName("Se prueba servicio getAccountByUser")
	public void getAccountByUserTest() {
		responseServices = bankingAccountController.getAccountByUser(user);
		LOG.info("Resultado: " + responseServices.getBody());

		assertNotNull(responseServices.getBody());

	}

	@Test
	@DisplayName("Se prueba servicio getAccountsGroupByType")
	public void getAccountsGroupByTypeTest() {

		responseAccountByType = bankingAccountController.getAccountsGroupByType();
		LOG.info("Resultado: " + responseAccountByType.getStatusCode());

		assertEquals(HttpStatus.OK, responseAccountByType.getStatusCode());

	}

	@Test
	@DisplayName("Se prueba servicio deleteAccounts")
	public void deleteAccountsTest() {

		responseServiceGenerico = bankingAccountController.deleteAccounts();
		LOG.info("Resultado: " + responseServiceGenerico.getStatusCode());
		assertEquals(HttpStatus.OK, responseServiceGenerico.getStatusCode());

	}

	@Test
	@DisplayName("Se prueba servicio getExternalUser/{userId}")
	public void getExternalUserTest() {

		externalUserResponse = bankingAccountController.getExternalUser(datoInicial);
		LOG.info("Resultado: " + externalUserResponse.getBody().getBody());
		assertEquals("No info in accountBalance since it is an external user",
				externalUserResponse.getBody().getBody());

	}

}
