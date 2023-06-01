package com.intech.comptabilite.service.entityservice;

import com.intech.comptabilite.model.SequenceEcritureComptable;
import com.intech.comptabilite.model.SequenceId;
import com.intech.comptabilite.repositories.SequenceEcritureComptableRepository;
import com.intech.comptabilite.service.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SequenceEcritureComptableService {


	private final SequenceEcritureComptableRepository repository;

	public SequenceEcritureComptableService(SequenceEcritureComptableRepository repository)
	{
		this.repository = repository;
	}

	public int getDernierValeurSequenceId(SequenceId sequenceId) throws NotFoundException {
		Optional<SequenceEcritureComptable> seq =
				repository.findById(sequenceId);
		if(seq.isEmpty()) {
			throw new NotFoundException();
		} else {
			return seq.get().getDerniereValeur();
		}
	}
	
	public void upsert(SequenceEcritureComptable sequenceEcritureComptable) {

		SequenceEcritureComptable seq =
				repository.findById(SequenceId.createOrGet(
						sequenceEcritureComptable.getJournalCode(),
						sequenceEcritureComptable.getAnnee())
				).orElse(sequenceEcritureComptable);

		seq.setDerniereValeur(sequenceEcritureComptable.getDerniereValeur());
		repository.save(seq);
	}
	
}
