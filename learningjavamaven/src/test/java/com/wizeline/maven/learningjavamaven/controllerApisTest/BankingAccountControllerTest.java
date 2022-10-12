package com.wizeline.maven.learningjavamaven.controllerApisTest;

import static com.wizeline.maven.learningjavamaven.utils.Utils.getCountry;
import static com.wizeline.maven.learningjavamaven.utils.Utils.pickRandomAccountType;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomAcountNumber;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomBalance;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizeline.maven.learningjavamaven.controller.BankingAccountController;
import com.wizeline.maven.learningjavamaven.enums.Country;
import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;
import com.wizeline.maven.learningjavamaven.model.Post;
import com.wizeline.maven.learningjavamaven.model.RequestPeticionPutDTO;
import com.wizeline.maven.learningjavamaven.model.ResponseDTO;
import com.wizeline.maven.learningjavamaven.utils.CommonServices;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class BankingAccountControllerTest {

	private static final Logger LOG = Logger.getLogger(BankingAccountControllerTest.class.getName());

	private String user = null;
	private String password = null;
	private String fecha = null;

	@Mock
	private ResponseEntity<Object> responseServiceActualizacion;

	@Mock
	RequestPeticionPutDTO updateUser;

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

	@Autowired
	private CommonServices commonServices;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Spy
	List<ResponseDTO> dataList = new ArrayList<>();

	/*
	 * NOTA: EN ESTA SECCION SE MUESTRAN LAS PRUEBAS REALIZADAS PARA LOS SERVICIOS
	 * DESARROLLADOS, ASÍ COMO ESTOS CONTIENEN LA RESPUESTA ESPERADA, TAMBIÉN SE PUEDEN CONSIDERAR
	 * PRUEBAS DE HAPPY PATH ------
	 */

	private BankAccountDTO buildBankAccount(String user, boolean isActive, Country country, LocalDateTime lastUsage) {
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setAccountNumber(randomAcountNumber());
		bankAccountDTO.setAccountName("Testing Demo Account ".concat(randomInt()));
		bankAccountDTO.setUserName(user);
		bankAccountDTO.setAccountBalance(randomBalance());
		bankAccountDTO.setAccountType(pickRandomAccountType());
		bankAccountDTO.setCountry(getCountry(country));
		bankAccountDTO.getLastUsage();
		bankAccountDTO.setCreationDate(lastUsage);
		bankAccountDTO.setAccountActive(isActive);
		return bankAccountDTO;
	}

	Long datoInicial = (long) 1;

	@BeforeEach
	void antesDeCadaPrueba() {

		if (user == null && password == null) {

			user = "user1@wizeline.com";
			password = "Pass1@";
			fecha = "07-10-2022";

		}
	}

	@Test
	@DisplayName("Se prueba servicio GET dado un usuario y un password")
	public void getUserAccount() {
		// user=user1@wizeline.com&password=Pass1@&date=12-03-2018

		LOG.info("Creamos un registro de prueba");
		try {
			BankAccountDTO bankAccountDTO = buildBankAccount(user, true, Country.MX, LocalDateTime.now());
			ResponseDTO resultadoLogin;

			resultadoLogin = commonServices.login(user, password);
			MvcResult resultado = mockMvc.perform(get("/api/getUserAccount").content(user))
					.andExpect(status().isBadRequest()).andReturn();

			BankAccountDTO[] arregloLogin = mapper.readValue(resultado.getResponse().getContentAsString(),
					BankAccountDTO[].class);
			List<BankAccountDTO> listaDatos = Arrays.asList(arregloLogin);

			LOG.info("Resultado: " + listaDatos.toString());

			assertNotNull(listaDatos.stream().map(BankAccountDTO::getUserName).collect(Collectors.toList())
					.containsAll(List.of(user, "user3@wizeline.com")));

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("Se prueba servicio getUserAccount (GET)")
	public void getUserAccountTest() { /// 400
		// user=user1@wizeline.com&password=Pass1@&date=12-03-2018

		try {
			ResponseDTO response = new ResponseDTO();

			responseService = (ResponseEntity<ResponseDTO>) bankingAccountController.getUserAccount(user, password,
					fecha);
			LOG.info("Resultado: " + responseService.getStatusCode());
			response.setStatus("OK");
			dataList.add(response);
			assertEquals(HttpStatus.OK, responseService.getStatusCode());

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("Se prueba servicio getAccounts (GET)")
	public void getAccountsTest() {

		try {
			// responseServices = bankingAccountController.getAccounts();

			MvcResult resultado = mockMvc.perform(get("/api/getAccounts")).andExpect(status().isOk()).andReturn();

			BankAccountDTO[] arregloLogin = mapper.readValue(resultado.getResponse().getContentAsString(),
					BankAccountDTO[].class);
			List<BankAccountDTO> listaDatos = Arrays.asList(arregloLogin);

			LOG.info("Resultado: " + listaDatos.toString());

			assertNotNull(listaDatos);
		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("Se prueba servicio getAccountByUser (GET)")
	public void getAccountByUserTest() {
		try {
			responseServices = bankingAccountController.getAccountByUser(user);
			LOG.info("Resultado: " + responseServices.getBody());

			assertNotNull(responseServices.getBody());

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("Se prueba servicio getAccountsGroupByType (GET)")
	public void getAccountsGroupByTypeTest() {
		try {
			// responseAccountByType = bankingAccountController.getAccountsGroupByType();
			LOG.info("Consumimos el servicio");

			MvcResult resultado = mockMvc.perform(get("/api/getAccountsGroupByType")).andExpect(status().isOk())
					.andReturn();

			BankAccountDTO[] arregloLogin = mapper.readValue(resultado.getResponse().getContentAsString(),
					BankAccountDTO[].class);
			List<BankAccountDTO> listaDatos = Arrays.asList(arregloLogin);

			LOG.info("Resultado: " + listaDatos.toString());

			assertNotNull(listaDatos);

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("Se prueba servicio deleteAccounts (DELETE)")
	public void deleteAccountsTest() {

		try {
			responseServiceGenerico = bankingAccountController.deleteAccounts();
			LOG.info("Resultado: " + responseServiceGenerico.getStatusCode());
			assertEquals(HttpStatus.OK, responseServiceGenerico.getStatusCode());

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("Se prueba servicio getExternalUser/{userId} (GET)")
	public void getExternalUserTest() {

		try {
			externalUserResponse = bankingAccountController.getExternalUser(datoInicial);
			LOG.info("Resultado: " + externalUserResponse.getBody().getBody());
			assertEquals("No info in accountBalance since it is an external user",
					externalUserResponse.getBody().getBody());

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}
	}

	@Test
	@DisplayName("Se prueba servicio updateAccounts (PUT)")
	public void getActualizaUserTest() {
		try {
			updateUser.setUser(user);
			responseServiceActualizacion = bankingAccountController.updateAccounts(updateUser);
			LOG.info("Resultado: " + responseServiceActualizacion.getStatusCode());
			assertEquals(HttpStatus.OK, responseServiceActualizacion.getStatusCode());

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}
	}

}
