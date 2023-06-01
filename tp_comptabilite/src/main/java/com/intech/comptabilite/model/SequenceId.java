package com.intech.comptabilite.model;

import jakarta.persistence.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SequenceId implements Serializable {

	@Transient
	private static final long serialVersionUID = -3816603702192890820L;

	private static final List<SequenceId> ID_LIST = new ArrayList<>();

	public static SequenceId createOrGet(String code, Integer annee) {

		SequenceId id = new SequenceId(code, annee);

		for (SequenceId sequenceId : ID_LIST) {
			if (sequenceId.equals(id)) {
				return sequenceId;
			}
		}

		ID_LIST.add(id);

		return id;

	}

	private String journalCode;
	private Integer annee;
	
	public SequenceId() {
	}
	
	public SequenceId(String code, Integer annee) {
		super();
		this.journalCode = code;
		this.annee = annee;
	}
	
	public String getJournalCode() {
		return journalCode;
	}
	public void setJournalCode(String code) {
		this.journalCode = code;
	}
	public Integer getAnnee() {
		return annee;
	}
	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof SequenceId sequenceId) {
			return sequenceId.getJournalCode().equals(this.getJournalCode())
				&& sequenceId.getAnnee().equals(this.getAnnee());
		}

		return  false;
	}
}
