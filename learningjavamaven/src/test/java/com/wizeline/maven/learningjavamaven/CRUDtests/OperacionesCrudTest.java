package com.wizeline.maven.learningjavamaven.CRUDtests;

import static com.wizeline.maven.learningjavamaven.utils.Utils.getCountry;
import static com.wizeline.maven.learningjavamaven.utils.Utils.pickRandomAccountType;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomAcountNumber;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomBalance;
import static com.wizeline.maven.learningjavamaven.utils.Utils.randomInt;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.wizeline.maven.learningjavamaven.enums.Country;
import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;

@SpringBootTest
public class OperacionesCrudTest {

	private static final Logger LOG = Logger.getLogger(OperacionesCrudTest.class.getName());

	private String user = null;
	private String password = null;
	private String fecha = null;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Spy
	private BankAccountDTO bankAccountOne;

	@Spy
	private List<BankAccountDTO> respuestaCollection;

	@Mock
	UpdateResult updateResult;

	@Mock
	Query query;

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

	@BeforeEach
	void antesDeCadaPrueba() {

		if (user == null && password == null) {

			user = "user3@wizeline.com";// para error: "user2@wizeline.com";
			password = "Pass1@"; // para error: "pass2";
			fecha = "07-10-2022";

		}
	}

	// OPERACIONES CRUD

	@Test
	@DisplayName("Se prueba funcionalidad de insercion")
	public void insercionDatoTest() {

		bankAccountOne = buildBankAccount("user3@wizeline.com", true, Country.MX, LocalDateTime.now());
		LOG.info("Resultado:" + bankAccountOne);
		mongoTemplate.save(bankAccountOne);
		assertNotNull(bankAccountOne);

	}

	@Test
	@DisplayName("Se prueba funcionalidad de lectura")
	public void lecturaDatoTest() {

		query = new Query();
		query.addCriteria(Criteria.where("userName").is(user));

		respuestaCollection = mongoTemplate.find(query, BankAccountDTO.class);
		LOG.info("Resultado:" + respuestaCollection.toString());

		assertNotNull(respuestaCollection);

	}

	@Test
	@DisplayName("Se prueba funcionalidad de actualizacion de datos")
	public void actualizarDatoTest() {

		query = new Query();
		query.addCriteria(Criteria.where("userName").is(user));

		Update updateData = Update.update("accountName", "Cambio dato por prueba unitaria");

		updateResult = mongoTemplate.updateMulti(query, updateData, "bankAccountCollection");

		LOG.info("Resultado: Numero de datos actualizados: " + updateResult.getModifiedCount());

		assertNotNull(updateResult);

	}

	@Test
	@DisplayName("Se prueba funcionalidad de eliminacion de datos")
	public void borrarDatoTest() {

		long datosEliminados = 0;
		query = new Query();
		query.addCriteria(Criteria.where("_id").exists(true));

		datosEliminados = mongoTemplate.remove(query, "bankAccountCollection").getDeletedCount();

		LOG.info("Resultado: Numero de datos eliminados: " + datosEliminados);
		assertNotEquals(0, datosEliminados);

	}

}
