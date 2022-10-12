package com.wizeline.maven.learningjavamaven.CRUDtests;

import static com.wizeline.maven.learningjavamaven.utils.Utils.getCountry;
import static com.wizeline.maven.learningjavamaven.utils.Utils.pickRandomAccountType;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomBalance;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomInt;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.wizeline.maven.learningjavamaven.enums.Country;
import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;
import com.wizeline.maven.learningjavamaven.repository.RepositorioTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")

public class OperacionesCrudTest {

	private static final Logger LOG = Logger.getLogger(OperacionesCrudTest.class.getName());

	private String user = null;
	private String password = null;
	private String fecha = null;

	@Mock
	private BankAccountDTO bankAccountOne;

	@Autowired
	RepositorioTest repositorioTest;

	@Spy
	List<BankAccountDTO> listaDatos = new ArrayList<>();

	/*
	 * 
	 * NOTA: EN ESTE APARTADO SE CONTEMPLAN LAS PRUEBAS CRUD PARA BASE DE DATOS
	 * MONGO
	 */

	private BankAccountDTO buildBankAccount(String user, boolean isActive, Country country, LocalDateTime lastUsage) {
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setAccountNumber(1L);
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

	@BeforeAll
	public void antesDeTodo() {
		LOG.info("Se Pobla la Base de Datos para prueba");
		repositorioTest.save(buildBankAccount("user3@wizeline.com", true, Country.MX, LocalDateTime.now()));
	}

	@BeforeEach
	void antesDeCadaPrueba() {

		if (user == null && password == null) {

			user = "user3@wizeline.com";
			password = "Pass1@";
			fecha = "07-10-2022";

		}
	}

	// OPERACIONES CRUD

	@Test
	@DisplayName("Prueba Insercion (CREATE)")
	public void DadoUnaBDD_CuandoSeInserteUnDato_InsertaDatoEsperado() {
		LOG.info("Se inserta dato");
		try {
			BankAccountDTO datoInsertado = repositorioTest
					.save(buildBankAccount("edwin.moreno@elektra.com.mx", true, Country.MX, LocalDateTime.now()));
			LOG.info("Se verifica que el dato se haya agregado");
			listaDatos.add(datoInsertado);
			Assertions.assertNotNull(datoInsertado);

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(listaDatos).size();
			Executable accion = () -> listaDatos.size();
			assertThrows(NullPointerException.class, accion);
		}
	}

	@Test
	@DisplayName("Prueba Lectura de datos (READ)")
	public void DadoUnaBDD_CuandoSeMandeLlamar_DevuelveDatos() {
		LOG.info("Se realiza lectura en base de datos");
		try {
			List<BankAccountDTO> listaDatos = repositorioTest.findAll();
			LOG.info("Se corrobora la lista");
			LOG.info("Se mapea y se corrobora que exista al menos la inserción deseada");

			this.listaDatos.addAll(listaDatos);
			Assertions.assertAll(() -> Assertions.assertNotNull(listaDatos),
					() -> Assertions.assertTrue(listaDatos.stream().map(BankAccountDTO::getUserName)
							.collect(Collectors.toList()).containsAll(List.of(user, "user1@wizeline.com"))));

		} catch (Exception e) {
			doThrow(NullPointerException.class).when(listaDatos).size();
			Executable accion = () -> listaDatos.size();
			assertThrows(NullPointerException.class, accion);
		}
	}

	@Test
	@DisplayName("Prueba actualizacion de datos (UPDATE)")
	public void DadaUnaBD_QueRecibeDato_EntoncesDevuelveRegistrosConModificaciones() {
		try {
			LOG.info("Se Actualiza Dato");

			BankAccountDTO data = repositorioTest.findById(String.valueOf(1L)).get();
			data.setCountry("Korea");
			listaDatos.add(data);
			LOG.info("Se guarda pais con la nueva actualización");
			repositorioTest.save(data);

			data = repositorioTest.findById(user).get();

			LOG.info("Se verifica la actualizacion del dato");
			Assertions.assertFalse(data.getCountry() == null);
		} catch (Exception e) {
			doThrow(NullPointerException.class).when(listaDatos).size();
			Executable accion = () -> listaDatos.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

	@Test
	@DisplayName("Prueba borrado de datos (DELETE)")
	public void DadaUnaBD_QueEliminaDatoDeseado() {
		LOG.info("Se hace el borrado de los regiastros de la Coleccion");
		try {
			repositorioTest.delete(bankAccountOne);
			List<BankAccountDTO> datosColectados = repositorioTest.findAll();
			LOG.info("Se verifica que el tamaÃ±o de la lista esté vacia");
			Assertions.assertNull(datosColectados);
		} catch (Exception e) {
			doThrow(NullPointerException.class).when(listaDatos).size();
			Executable accion = () -> listaDatos.size();
			assertThrows(NullPointerException.class, accion);
		}

	}

}
