package com.intech.comptabilite.model;

import com.intech.comptabilite.model.validation.MontantComptable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "ligne_ecriture_comptable")
public class LigneEcritureComptable {


    private static final String MESSAGE_DECIMAL = "Le montant doit posseder 2 chiffres maximum après la virgule";

	@EmbeddedId
	private LigneId ligneId; // Pour gérer une clé composée

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "compteComptableNumero")
    @NotNull
    private CompteComptable compteComptable;

    /** The Libelle. */
    @Size(max = 200)
    private String libelle;

    /** The Credit. */
    @MontantComptable
    private BigDecimal credit;
    /** The Debit. */
    @MontantComptable
    private BigDecimal debit;


    public LigneEcritureComptable() {
    }

    public LigneEcritureComptable(CompteComptable pCompteComptable, String pLibelle,
                                  BigDecimal pDebit, BigDecimal pCredit) {
        compteComptable = pCompteComptable;
        libelle = pLibelle;
        debit = pDebit;
        credit = pCredit;
    }

    public LigneId getLigneId() {
		return ligneId;
	}
    public void setLigneId(LigneId ligneId) {
		this.ligneId = ligneId;
	}    
    public CompteComptable getCompteComptable() {
        return compteComptable;
    }
    public void setCompteComptable(CompteComptable pCompteComptable) {
        compteComptable = pCompteComptable;
    }
    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String pLibelle) {
        libelle = pLibelle;
    }
    public BigDecimal getDebit() {
        return debit;
    }
    public void setDebit(BigDecimal pDebit) {
        debit = pDebit;
    }
    public BigDecimal getCredit() {
        return credit;
    }
    public void setCredit(BigDecimal pCredit) {
        credit = pCredit;
    }
}
