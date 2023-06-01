package com.intech.comptabilite.repositories;

import com.intech.comptabilite.model.CompteComptable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteComptableRepository extends CrudRepository<CompteComptable, Integer> {

}
