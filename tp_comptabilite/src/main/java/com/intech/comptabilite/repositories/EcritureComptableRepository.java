package com.intech.comptabilite.repositories;

import com.intech.comptabilite.model.EcritureComptable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EcritureComptableRepository extends CrudRepository<EcritureComptable, Integer> {

	public Optional<EcritureComptable> getByReference(String reference);
	
}
