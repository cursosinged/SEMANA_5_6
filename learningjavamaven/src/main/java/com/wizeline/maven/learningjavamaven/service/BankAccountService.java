/*
 * Copyright (c) 2022 Nextiva, Inc. to Present.
 * All rights reserved.
 */
 package com.wizeline.maven.learningjavamaven.service;

import java.util.List;

import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;


public interface BankAccountService {

    /**
     * Gets accounts.
     *
     * @return todas las cuentas existentes en formato de lista.
     */
    List<BankAccountDTO> getAccounts();

    /**
     * Gets account details.
     *
     * @param user      nombre de usuario.
     * @param lastUsage último uso de la cuenta.
     * @return detalles de una sola cuenta.
     */
    BankAccountDTO getAccountDetails(String user, String lastUsage);

    void deleteAccounts();

    List<BankAccountDTO> getAccountByUser(String user);

}
