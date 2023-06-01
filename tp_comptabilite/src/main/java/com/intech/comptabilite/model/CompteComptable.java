package com.intech.comptabilite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "compte_comptable")
public class CompteComptable {
	
	@Id
    @NotNull
    private Integer numero;

    @NotNull
    @Size(min = 1, max = 150)
    private String libelle;


    public CompteComptable() {
    }

    public CompteComptable(Integer pNumero) {
        numero = pNumero;
    }

    public CompteComptable(Integer pNumero, String pLibelle) {
        this(pNumero);
        libelle = pLibelle;
    }

    public Integer getNumero() {
        return numero;
    }
    public void setNumero(Integer pNumero) {
        numero = pNumero;
    }
    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String pLibelle) {
        libelle = pLibelle;
    }
}
