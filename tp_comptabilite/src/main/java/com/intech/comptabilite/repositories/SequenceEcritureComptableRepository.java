package com.intech.comptabilite.repositories;

import com.intech.comptabilite.model.SequenceEcritureComptable;
import com.intech.comptabilite.model.SequenceId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceEcritureComptableRepository extends CrudRepository<SequenceEcritureComptable, SequenceId> {
	
}
