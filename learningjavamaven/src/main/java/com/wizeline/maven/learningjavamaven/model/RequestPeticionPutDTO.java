package com.wizeline.maven.learningjavamaven.model;

import javax.validation.constraints.NotBlank;

public class RequestPeticionPutDTO {

	@NotBlank(message = "Campo user no debe ser vacio o nulo")
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "RequestPeticionPutDTO [user=" + user + "]";
	}

}
