package com.intech.comptabilite.service.businessmanager;

import com.intech.comptabilite.model.*;
import com.intech.comptabilite.service.entityservice.SequenceEcritureComptableService;
import com.intech.comptabilite.service.exceptions.FunctionalException;
import com.intech.comptabilite.service.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ComptabiliteManagerImplUnitTest
{

    @Autowired
    private MockMvc mockMvc;

	@Autowired
    private ComptabiliteManagerImpl manager;

    @Autowired
    private SequenceEcritureComptableService sequenceEcritureComptableService;

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(
                manager,
                "sequenceEcritureComptableService",
                sequenceEcritureComptableService
        );
    }

    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable(new JournalComptable("AX", "libelle"));
        /*vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(LocalDate.now());*/
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable(new JournalComptable("AX", "libelle"));
        Assertions.assertThrows(FunctionalException.class,
        		() -> {
        		manager.checkEcritureComptableUnit(vEcritureComptable);}
        );        
    }

    @Test
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable(new JournalComptable("AX", "libelle"));
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(LocalDate.now());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
       
        Assertions.assertThrows(
                FunctionalException.class,
        		() -> {
                    manager.checkEcritureComptableUnit(vEcritureComptable);
                }
        );
    }

    @Test
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable(new JournalComptable("AX", "libelle"));
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(LocalDate.now());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        
        Assertions.assertThrows(
                FunctionalException.class,
        		() -> manager.checkEcritureComptableUnit(vEcritureComptable)
        );
    }

    @Test
    public void checkEcitureComptableUnitRG5_Conforme() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable(new JournalComptable("AX", "libelle"));

        /*vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(LocalDate.now());*/
        vEcritureComptable.setLibelle("Libelle");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
        																		 null, new BigDecimal(123),
        																		 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
        																		 null, null,
        																		 new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);

        String ref = vEcritureComptable.getReference();
        String codeYear = ref.substring(0, ref.indexOf('/'));
        assertEquals("AX-2023", codeYear);

    }

    @Test
    public void checkEcitureComptableUnitRG5_Inconforme() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable(new JournalComptable("AX", "libelle"));

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(LocalDate.now());
        vEcritureComptable.setReference("AC-2016/00001");
        vEcritureComptable.setLibelle("Libelle");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        Assertions.assertThrows(
                FunctionalException.class,
                () -> manager.checkEcritureComptableUnit(vEcritureComptable)
        );
    }

    @Test
    public void checkEcritureComptableUnitRG7() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable(new JournalComptable("AC", "Achat"));

        /*vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(LocalDate.now());*/
        vEcritureComptable.setLibelle("Libelle");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
        																		 null, new BigDecimal("123.123"),
        																		 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
        																		 null, null,
        																		 new BigDecimal("123.123")));

        Assertions.assertThrows(
                FunctionalException.class,
                () -> manager.checkEcritureComptableUnit(vEcritureComptable)
        );

    }

    @Test
    public void testAddReference_ExistingJournal() throws Exception {

        //Arrange
        var result = manager.getListJournalComptable();
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(result.get(0));
        vEcritureComptable.setDate(LocalDate.now());
        vEcritureComptable.setLibelle("SANDREX");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        //Act
        //EcritureComptable compt =
        manager.addReference(vEcritureComptable);

       // System.out.println(compt.getReference());

        //Assert
        String expected = "AC-2023/00003";
        assertEquals(expected, vEcritureComptable.getReference());

        System.out.println(result);
    }

    @Test
    public void testAddReference_NewJournal() throws Exception {

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setDate(LocalDate.parse("2015-01-01"));
        vEcritureComptable.setJournal(new JournalComptable("BY", "Paris"));
        vEcritureComptable.setLibelle("Cartouches");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

//        List<EcritureComptable> compts = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            manager.addReference(vEcritureComptable);
        }

        //var compt = compts.get(compts.size() - 1);

        String expected = "BY-2015/00015";
        Assertions.assertEquals(expected, vEcritureComptable.getReference());

    }

    @Test
    public void testAddReference_Mock() throws NotFoundException
    {

        SequenceEcritureComptableService seq =
                Mockito.mock(SequenceEcritureComptableService.class);
        ReflectionTestUtils.setField(
            manager,
            "sequenceEcritureComptableService",
            seq
        );

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setDate(LocalDate.parse("2015-01-01"));
        vEcritureComptable.setJournal(new JournalComptable("BY", "Paris"));
        vEcritureComptable.setLibelle("Cartouches");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));



        //J'ai du creee methode statique createOrGet pour eviter l'erreur de stubbing


        SequenceId sequenceId = SequenceId.createOrGet(
            vEcritureComptable.getJournal().getCode(),
            vEcritureComptable.getDate().getYear()
        );

        Mockito
            .when(
                seq.getDernierValeurSequenceId(
                    sequenceId
                )
            )
            .thenReturn(12);


        //var compt =
        manager.addReference(vEcritureComptable);
        Mockito.verify(seq).getDernierValeurSequenceId(
            sequenceId
        );

        String expected = "BY-2015/00013";
        Assertions.assertEquals(expected, vEcritureComptable.getReference());
    }
}
