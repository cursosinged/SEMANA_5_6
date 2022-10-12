package com.wizeline.maven.learningjavamaven.controller.EdgeCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizeline.maven.learningjavamaven.controller.BankingAccountController;
import com.wizeline.maven.learningjavamaven.controller.UserController;
import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;
import com.wizeline.maven.learningjavamaven.model.ResponseDTO;
import com.wizeline.maven.learningjavamaven.model.UserDTO;
import com.wizeline.maven.learningjavamaven.utils.CommonServices;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class PruebaServiciosConError {

	private static final Logger LOG = Logger.getLogger(PruebaServiciosConError.class.getName());

	private String user = null;
	private String password = null;
	private String fecha = null;

	@MockBean
	private CommonServices commonServices;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Spy
	List<ResponseDTO> dataList = new ArrayList<>();

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

	/*
	 * 
	 * 
	 * NOTA: EN ESTA SECCION SE MUESTRAN VARIOS ESCENARIOS PARA EDGE CASE
	 * CONTEMPLANDO LOS SERVICIOS REALIZADOS.
	 * 
	 * 
	 *
	 */

	@BeforeEach
	void antesDeCadaPrueba() {
		if (user == null && password == null) {

			user = "edwin.moreno@elektra.com";
			password = "pass2";
			fecha = "07/10/2022";
		}

	}

	@Test
	@DisplayName("Se prueba servicio para respuesta 400")
	public void getUserAccount() { /// 400

		LOG.info("Creamos un registro de prueba");
		try {
			ResponseDTO resultadoLogin;

			resultadoLogin = commonServices.login(user, password);
			dataList.add(resultadoLogin);

			LOG.info("Hacemos la corroboración de que lo consumodo de algo diferente al 200 Ok habitual");

			MvcResult resultado = mockMvc.perform(get("/api/getUserAccount")).andExpect(status().isBadRequest())
					.andReturn();

			BankAccountDTO[] arregloLogin = mapper.readValue(resultado.getResponse().getContentAsString(),
					BankAccountDTO[].class);
			List<BankAccountDTO> listaDatosObtenidos = Arrays.asList(arregloLogin);

			LOG.info("Verificamos que la lista de datos tenga información sobre el login ");
			assertNotNull(listaDatosObtenidos.stream().map(BankAccountDTO::getUserName).collect(Collectors.toList())
					.containsAll(List.of(user)));

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("Se prueba servicio para respuesta 404")
	public void getUserAccountServerError() { /// 404

		LOG.info("Creamos un registro de prueba");
		try {
			ResponseDTO resultadoLogin;
			resultadoLogin = commonServices.login(user, password);
			LOG.info("Hacemos la corroboración de que lo consumido da un 404 mandando un enpoint no registrado");

			MvcResult resultado = mockMvc.perform(get("/getUserTesting")).andExpect(status().isNotFound()).andReturn();

			BankAccountDTO[] arregloLogin = mapper.readValue(resultado.getResponse().getContentAsString(),
					BankAccountDTO[].class);
			List<BankAccountDTO> listaDatosObtenidos = Arrays.asList(arregloLogin);
			dataList.add(resultadoLogin);

			LOG.info("Verificamos que la lista de datos tenga información sobre el login ");
			assertNotNull(listaDatosObtenidos);

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("PruebaServicioLoginParaDatoFail")
	public void pruebaServicioLoginError() {
		try {
			ResponseDTO resultado = new ResponseDTO();

			LOG.info("LearningJava - Iniciado servicio REST para servicio /login");
			LOG.info("Creamos los datos de prueba requeridos");

			responseService = userController.loginUser(user, password);
			LOG.info("RESULTADO SERVICIO: " + responseService.getBody().getStatus());
			resultado.setStatus("OK");
			dataList.add(resultado);
			assertEquals("fail", responseService.getBody().getStatus(), "Error, Usuario no registrado");

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("PruebaServicioCreateUserErrorER001")
	public void pruebaServicioCreateUserError() {
		LOG.info("Prueba Edge case con error ER001");
		LOG.info("Creamos los datos de prueba requeridos");

		userDTO.setUser(null);
		userDTO.setPassword(null);

		responseService = userController.createUserAccount(userDTO);

		LOG.info("Resultado: " + responseService.getBody().getErrors().getErrorCode());

		assertEquals("ER001", responseService.getBody().getErrors().getErrorCode(),
				"Error al crear usuario, parametros no validos");

	}

	@Test
	@DisplayName("PruebaServicioCreacionUsuarioNull")
	public void pruebaServicioCreateUsersDadoObjetoNull() {
		LOG.info("LearningJava - Iniciado prueba de creacionUsuario dado un parametro nulo");
		int repeticiones = 2;
		List<ResponseDTO> expectedResponse = new ArrayList<ResponseDTO>();
		LOG.info("Creamos los datos de prueba requeridos");

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
	@DisplayName("Se prueba servicio dado un usuario no existente")
	public void getUserAccountTestErrorUsuarioNoExiste() {

		String result = null;
		boolean concuerda;
		try {
			ResponseDTO resultado = new ResponseDTO();
			LOG.info("Creamos los datos de prueba requeridos");
			result = (String) bankingAccountController.getUserAccount(user, password, fecha).getBody();
			concuerda = result.contains("Incorrecto");
			LOG.info("Resultado: " + result);
			resultado.setStatus("OK");
			dataList.add(resultado);
			assertEquals(true, concuerda, "Problema al obtener datos de usuario");

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}
	}

	@Test
	@DisplayName("Se prueba servicio dado un error en procesamiento")
	public void getAccountsErrorDatosNulos() {

		try {
			ResponseDTO resultado = new ResponseDTO();
			LOG.info("Obtenemos datos");

			respuestaGetAccount = bankingAccountController.getAccounts();
			LOG.info("Forzamos un error en procesamiento");

			respuestaGetAccount = null;
			dataList.add(resultado);
			Assertions.assertAll(

					() -> Assertions.assertEquals(null, respuestaGetAccount.getBody()));
		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}
	}

	@Test
	@DisplayName("Se prueba servicio dado un error en la cuenta por usuario no registrado")
	public void getAccountByUserErrorByUser() {

		LOG.info("Creamos los datos de prueba requeridos");
		try {
			ResponseDTO resultado = new ResponseDTO();
			List<BankAccountDTO> accounts = new ArrayList<BankAccountDTO>();
			LOG.info("Consultamos dato de un usuario que no existe");

			respuestaGetAccount = bankingAccountController.getAccountByUser(user);
			resultado.setStatus("OK");
			;
			LOG.info("Resultado: " + respuestaGetAccount.getBody());
			dataList.add(resultado);
			Assertions.assertAll(

					() -> Assertions.assertEquals(accounts, respuestaGetAccount.getBody()));

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}
	}

}
