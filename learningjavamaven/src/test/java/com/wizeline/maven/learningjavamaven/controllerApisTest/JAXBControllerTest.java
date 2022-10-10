package com.wizeline.maven.learningjavamaven.controllerApisTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wizeline.maven.learningjavamaven.controller.JAXBController;
import com.wizeline.maven.learningjavamaven.model.XmlBean;

@SpringBootTest
public class JAXBControllerTest {

	private static final Logger LOG = Logger.getLogger(JAXBControllerTest.class.getName());

	@Autowired
	JAXBController jaxb;

	@Mock
	ResponseEntity<XmlBean> resultadoTest;

	@Test
	@DisplayName("Se prueba funcionalidad de insercion")
	public void jaxbTest() {
		resultadoTest = jaxb.loginUser();
		LOG.info("Resultado de la prueba: " + resultadoTest.getStatusCode());
		assertEquals(HttpStatus.OK, resultadoTest.getStatusCode());

	}

}
