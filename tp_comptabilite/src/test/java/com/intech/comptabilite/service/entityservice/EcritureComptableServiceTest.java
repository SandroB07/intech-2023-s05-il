package com.intech.comptabilite.service.entityservice;

import com.intech.comptabilite.model.CompteComptable;
import com.intech.comptabilite.model.EcritureComptable;
import com.intech.comptabilite.model.JournalComptable;
import com.intech.comptabilite.model.LigneEcritureComptable;
import com.intech.comptabilite.repositories.EcritureComptableRepository;
import com.intech.comptabilite.service.exceptions.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EcritureComptableServiceTest
{
	
	@Autowired
	private EcritureComptableService service;

    @Autowired
    EcritureComptableRepository repository;


    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(
            service,
            "repository",
            repository
        );
    }


    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test
    public void testIsEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable(new JournalComptable("AX", "libelle"));

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assertions.assertTrue(service.isEquilibree(vEcriture));

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assertions.assertFalse(service.isEquilibree(vEcriture));
    }


    @Test
    public void testGetEcritureComptableByRef_Mock_NotFound() throws NotFoundException
    {
        EcritureComptableRepository rep = Mockito.mock(EcritureComptableRepository.class);
        ReflectionTestUtils.setField(
            service,
            "repository",
            rep
        );

        when(rep.getByReference(Mockito.anyString())).thenReturn(Optional.empty());

        //Act
        Assertions.assertThrows(
            NotFoundException.class,
            () -> {
                service.getEcritureComptableByRef("BQ-2023/00001");
                verify(rep).getByReference(Mockito.anyString());
            }
        );
    }

    @Test
    public void testGetEcritureComptableByRef_Mock_Found() throws NotFoundException
    {
        EcritureComptableRepository rep = Mockito.mock(EcritureComptableRepository.class);
        ReflectionTestUtils.setField(
            service,
            "repository",
            rep
        );

        String reference = "BQ-2023/00001";

        EcritureComptable vECR = new EcritureComptable(new JournalComptable("BQ", "Banque"));
        vECR.setLibelle("Paiement Facture F110001");
        vECR.setReference(reference);
        vECR.getListLigneEcriture().add(this.createLigne(401, null, "2500.00"));
        vECR.getListLigneEcriture().add(this.createLigne(512, "2500.00", null));

        when(rep.getByReference(Mockito.anyString())).thenReturn(Optional.of(vECR));

        //Act
        EcritureComptable result = service.getEcritureComptableByRef(reference);

        Assertions.assertDoesNotThrow(
                () -> new NotFoundException()
        );
        Assertions.assertEquals(reference, result.getReference());
    }

    @Test
    public void testGetListEcritureComptable() {
        var result = service.getListEcritureComptable();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() > 0);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("AC", result.get(0).getJournal().getCode());
        Assertions.assertEquals("BQ-2023/00001", result.get(2).getReference());
    }

    @Test
    public void testGetTotalDebit() {
        var vECS = repository.findById(1)
                .orElse(null);

        if (vECS == null) {
            Assertions.fail();
        }

        //Act
        BigDecimal result = service.getTotalDebit(vECS);

        //Assert
        BigDecimal expected = new BigDecimal("100.55");
        Assertions.assertEquals(0, expected.compareTo(result));
    }

    @Test
    public void testGetTotalCredit() {
        var vECS = repository.findById(3)
                .orElse(null);

        if (vECS == null) {
            Assertions.fail();
        }

        //Act
        BigDecimal result = service.getTotalCredit(vECS);

        //Assert
        BigDecimal expected = new BigDecimal("386.26");
        Assertions.assertEquals(0, expected.compareTo(result));
    }
}
