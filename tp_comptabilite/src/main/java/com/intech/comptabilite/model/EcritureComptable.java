package com.intech.comptabilite.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "ecriture_comptable")
public class EcritureComptable {

	private static Integer sequence = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "journalCode")
	@NotNull
	private JournalComptable journal;

	@Pattern(regexp = "[a-zA-Z0-9]{1,5}-\\d{4}/\\d{5}")
	private String reference;

	@NotNull
	@CreatedDate
	private LocalDate date;

	@NotNull
	@Size(min = 1, max = 200)
	private String libelle;

	@OneToMany(
			cascade = CascadeType.ALL, 
			orphanRemoval = true, 
			fetch = FetchType.EAGER)
	@JoinColumn(name = "ecritureId")
	@Valid
	@Size(min = 2)
	private final List<LigneEcritureComptable> listLigneEcriture;


	public EcritureComptable() {
		this.listLigneEcriture = new ArrayList<>();
		this.date = LocalDate.now();
	}

	public EcritureComptable(JournalComptable journal) {
		this();
		this.journal = journal;

		StringBuilder sb = new StringBuilder();
		sb.append(getJournal().getCode())
				.append("-")
				.append(getDate().getYear())
				.append("/")
				.append("0".repeat(Math.max(0, 5 - sequence.toString().length())))
				.append(sequence);

		setReference(sb.toString());
		sequence++;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer pId) {
		id = pId;
	}

	public JournalComptable getJournal() {
		return journal;
	}

	public void setJournal(JournalComptable pJournal) {
		journal = pJournal;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String pReference) {
		reference = pReference;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate pDate) {
		date = pDate;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String pLibelle) {
		libelle = pLibelle;
	}

	public List<LigneEcritureComptable> getListLigneEcriture() {
		return listLigneEcriture;
	}
}
