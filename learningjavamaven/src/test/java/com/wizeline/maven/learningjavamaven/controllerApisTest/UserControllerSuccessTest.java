package com.wizeline.maven.learningjavamaven.controllerApisTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wizeline.maven.learningjavamaven.controller.UserController;
import com.wizeline.maven.learningjavamaven.model.ResponseDTO;
import com.wizeline.maven.learningjavamaven.model.UserDTO;

@SpringBootTest
public class UserControllerSuccessTest {

	private static final Logger LOG = Logger.getLogger(UserControllerSuccessTest.class.getName());

	private String user = null;
	private String password = null;
	private int codigoServicio = 0;

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

	@BeforeEach
	void antesDeCadaPrueba() {

		if (user == null && password == null) {

			user = "user2@wizeline.com";
			password = "pass2";

		}

	}

	@Test
	@DisplayName("Se prueba happy path")
	public void procesaHappyPathTest() {

		// ?user=user2@wizeline.com&password=pass2

		LOG.info("LearningJava - Iniciado servicio REST para happy path");

		responseService = userController.loginUser(user, password);
		codigoServicio = responseService.getStatusCodeValue();

		LOG.info("RESULTADO SERVICIO: " + responseService.getStatusCodeValue());
		assertEquals(codigoServicio, 200);

	}

	@Test
	@DisplayName("PruebaServicioLogin")
	public void pruebaServicioLoginTest() {

		LOG.info("LearningJava - Iniciado servicio REST para servicio /login tomando en cuenta un proceso exitoso");

		responseService = userController.loginUser(user, password);
		codigoServicio = responseService.getStatusCodeValue();

		LOG.info("RESULTADO SERVICIO: " + responseService.getStatusCodeValue());
		assertNotNull(codigoServicio);

	}

	@Test
	@DisplayName("PruebaServicioCreateUser")
	public void pruebaServicioCreateUserTest() {
		// {"user":"user2@wizeline.com","password":"pass2"}
		LOG.info("LearningJava - Iniciado servicio REST para servicio /createUser");

		userDTO.setUser(user);
		userDTO.setPassword(password);

		responseService = userController.createUserAccount(userDTO);

		assertEquals(HttpStatus.OK, responseService.getStatusCode());

	}

	@Test
	@DisplayName("PruebaServicioCreateUsers")
	public void pruebaServicioCreateUsersTest() {
		// {"user":"user2@wizeline.com","password":"pass2"}
		LOG.info("LearningJava - Iniciado servicio REST para servicio /createUsers");
		int repeticiones = 2;
		for (int i = 0; i <= repeticiones; i++) {

			if (i == 1) {
				userDTO.setUser("user2@wizeline.com");
				userDTO.setPassword("pass2");
				userDTOList.add(userDTO);
				responseServices = userController.createUsersAccount(userDTOList);

				assertEquals(HttpStatus.OK, responseServices.getStatusCode());

			} else {
				userDTO.setUser(user);
				userDTO.setPassword(password);
			}

			userDTOList.add(userDTO);

		}

	}

}
