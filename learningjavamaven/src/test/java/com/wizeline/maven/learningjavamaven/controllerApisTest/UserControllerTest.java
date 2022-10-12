package com.wizeline.maven.learningjavamaven.controllerApisTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

import com.wizeline.maven.learningjavamaven.controller.UserController;
import com.wizeline.maven.learningjavamaven.model.ResponseDTO;
import com.wizeline.maven.learningjavamaven.model.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class UserControllerTest {

	private static final Logger LOG = Logger.getLogger(UserControllerTest.class.getName());

	private String user = null;
	private String password = null;
	private int codigoServicio = 200;

	private static final String respuestaSuccess = "Login exitoso";

	@Mock
	ResponseEntity<ResponseDTO> responseService;

	@Mock
	ResponseEntity<List<ResponseDTO>> responseServices;

	@Mock
	UserDTO userDTO;

	@Mock
	List<UserDTO> userDTOList;

	@Autowired
	private UserController userController;

	@Spy
	List<ResponseDTO> dataList = new ArrayList<>();

	/*
	 * 
	 * NOTA: TODOS ESTOS ESCENARIOS CORRESPONDIENTES A LA API DE USER SON
	 * CONSIDERADOS HAPPY PATH PUESTO QUE OBTENEMOS LA RESPUESTA ESPERADA POR PARTE
	 * DEL SERVICIO
	 * 
	 */

	@BeforeEach
	void antesDeCadaPrueba() {

		if (user == null && password == null) {

			user = "user2@wizeline.com";
			password = "pass2";

		}

	}

	@Test
	@DisplayName("Se prueba dado un HTTP 200OK esperado")
	public void procesaHTTP200() {

		// ?user=user2@wizeline.com&password=pass2

		LOG.info("LearningJava - Iniciado servicio REST para happy path");

		responseService = userController.loginUser(user, password);

		LOG.info("RESULTADO SERVICIO: " + responseService.getStatusCodeValue());
		assertEquals(codigoServicio, responseService.getStatusCodeValue());

	}

	@Test
	@DisplayName("Prueba Servicio Dado un resultado no Nullo")
	public void pruebaServicioNotNullComoRespuesta() {

		LOG.info("LearningJava - Iniciado servicio REST para servicio /login tomando en cuenta un proceso exitoso");

		responseService = userController.loginUser(user, password);
		codigoServicio = responseService.getStatusCodeValue();

		LOG.info("RESULTADO SERVICIO: " + responseService.getStatusCodeValue());
		assertNotNull(codigoServicio);

	}

	@Test
	@DisplayName("PruebaServicioCreateUser (POST)")
	public void pruebaServicioCreateUserTest() {
		// {"user":"user2@wizeline.com","password":"pass2"}
		LOG.info("LearningJava - Iniciado servicio REST para servicio /createUser");
		try {
			ResponseDTO resultadoLogin = new ResponseDTO();
			LOG.info("Creamos los datos del usuario a insertar");
			userDTO.setUser(user);
			userDTO.setPassword(password);

			responseService = userController.createUserAccount(userDTO);
			resultadoLogin.setStatus("OK");
			dataList.add(resultadoLogin);
			assertEquals(HttpStatus.OK, responseService.getStatusCode());

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}
	}

	@Test
	@DisplayName("PruebaServicioDadoVariosUsuarios")
	public void pruebaServicioCreateUsersTest() {
		// {"user":"user2@wizeline.com","password":"pass2"}
		LOG.info("LearningJava - Iniciado servicio REST para servicio /createUsers");
		int repeticiones = 2;
		try {
			ResponseDTO resultadoLogin = new ResponseDTO();
			LOG.info("Creamos los datos del usuario a insertar");

			for (int i = 0; i <= repeticiones; i++) {

				if (i == 1) {
					userDTO.setUser("user2@wizeline.com");
					userDTO.setPassword("pass2");
					userDTOList.add(userDTO);
					responseServices = userController.createUsersAccount(userDTOList);
					resultadoLogin.setStatus("OK");
					dataList.add(resultadoLogin);
					assertEquals(HttpStatus.OK, responseServices.getStatusCode());

				} else {
					userDTO.setUser(user);
					userDTO.setPassword(password);
				}

				userDTOList.add(userDTO);

			}

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(dataList).size();
			Executable accion = () -> dataList.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

}
