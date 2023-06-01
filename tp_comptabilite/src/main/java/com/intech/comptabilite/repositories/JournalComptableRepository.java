package com.intech.comptabilite.repositories;

import com.intech.comptabilite.model.JournalComptable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalComptableRepository extends CrudRepository<JournalComptable, Integer> {

}
