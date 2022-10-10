package com.wizeline.maven.learningjavamaven.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;
import com.wizeline.maven.learningjavamaven.model.ResponseDTO;
import com.wizeline.maven.learningjavamaven.service.BankAccountService;
import com.wizeline.maven.learningjavamaven.service.BankAccountServiceImpl;
import com.wizeline.maven.learningjavamaven.service.UserService;
import com.wizeline.maven.learningjavamaven.service.UserServiceImpl;

@Component
public class UtileriasServicios {

	public static ResponseDTO login(String User, String password) {
		UserService userBo = new UserServiceImpl();
		return userBo.login(User, password);
	}

	public static ResponseDTO createUser(String User, String password) {
		UserService userBo = new UserServiceImpl();
		return userBo.createUser(User, password);
	}

	public static Map<String, String> splitQuery(URI uri) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = uri.getQuery();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}

	public static BankAccountDTO getAccountDetails(String user, String lastUsage) {
		BankAccountService bankAccountBO = new BankAccountServiceImpl();
		return bankAccountBO.getAccountDetails(user, lastUsage);
	}

}
