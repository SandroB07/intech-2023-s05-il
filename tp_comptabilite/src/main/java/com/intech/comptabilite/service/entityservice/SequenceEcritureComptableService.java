package com.intech.comptabilite.service.entityservice;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.intech.comptabilite.model.SequenceEcritureComptable;
import com.intech.comptabilite.model.SequenceId;
import com.intech.comptabilite.repositories.SequenceEcritureComptableRepository;
import com.intech.comptabilite.service.exceptions.NotFoundException;

@Service
public class SequenceEcritureComptableService {


	private final SequenceEcritureComptableRepository repository;

	public SequenceEcritureComptableService(SequenceEcritureComptableRepository repository)
	{
		this.repository = repository;
	}

	public int getDernierValeurByCodeAndAnnee(String journalCode, Integer annee) throws NotFoundException {
		Optional<SequenceEcritureComptable> seq =
				repository.findById(new SequenceId(journalCode, annee));
		if(seq.isEmpty()) {
			throw new NotFoundException();
		} else {
			return seq.get().getDerniereValeur();
		}
	}
	
	public SequenceEcritureComptable upsert(SequenceEcritureComptable sequenceEcritureComptable) {

		SequenceEcritureComptable seq =
				repository.findById(new SequenceId(
						sequenceEcritureComptable.getJournalCode(),
						sequenceEcritureComptable.getAnnee())
				).orElse(sequenceEcritureComptable);

		seq.setDerniereValeur(sequenceEcritureComptable.getDerniereValeur());
		return repository.save(seq);
	}
	
}
