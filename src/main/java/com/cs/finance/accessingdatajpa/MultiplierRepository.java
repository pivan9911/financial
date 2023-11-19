package com.cs.finance.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

public interface MultiplierRepository extends CrudRepository<MultiplierEntity, Long> {

	MultiplierEntity findByName(String lastName);

	MultiplierEntity findById(long id);
}
