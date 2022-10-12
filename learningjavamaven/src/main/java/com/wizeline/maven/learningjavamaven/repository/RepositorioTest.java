package com.wizeline.maven.learningjavamaven.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wizeline.maven.learningjavamaven.model.BankAccountDTO;

public interface RepositorioTest extends MongoRepository<BankAccountDTO,String> {
}
