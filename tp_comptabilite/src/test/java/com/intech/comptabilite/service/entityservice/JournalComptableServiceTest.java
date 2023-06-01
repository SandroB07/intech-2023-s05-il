package com.intech.comptabilite.service.entityservice;


import com.intech.comptabilite.model.JournalComptable;
import com.intech.comptabilite.repositories.JournalComptableRepository;
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

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class JournalComptableServiceTest
{

    @Autowired
    private JournalComptableService journalComptableService;

    @Autowired
    JournalComptableRepository journalComptableRepository;

    @BeforeEach
    void beforeEach()
    {
        ReflectionTestUtils.setField(
            journalComptableService,
            "repository",
            journalComptableRepository
        );
    }

    @Test
    public void testGetByNumero_TestData_NotFound() {

        //Arrange
        List<JournalComptable> vResult = journalComptableService.getListJournalComptable();

        //Act
        JournalComptable res = journalComptableService.getByCode(vResult, "XX");

        //Assert
        Assertions.assertNull(res);
    }

    @Test
    public void testGetByNumero_TestData_Found() {

        //Arrange
        List<JournalComptable> vResult = journalComptableService.getListJournalComptable();

        //Act
        JournalComptable res = journalComptableService.getByCode(vResult, "BQ");

        //Assert
        Assertions.assertNotNull(res);
        Assertions.assertEquals("BQ", res.getCode());
        Assertions.assertEquals("Banque", res.getLibelle());
    }

    @Test
    public void testGetListJournalComptable_Integration() {

        JournalComptableRepository rep = Mockito.mock(JournalComptableRepository.class);
        ReflectionTestUtils.setField(
            journalComptableService,
            "repository",
            rep
        );

        when(rep.findAll()).thenReturn(List.of(
            new JournalComptable("AC", "AC/DC"),
            new JournalComptable("ITI", "Intech"),
            new JournalComptable("BQ", "Banque")
        ));

        //Act
        List<JournalComptable> res = journalComptableService.getListJournalComptable();

        //Assert
        verify(rep).findAll();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(3, res.size());
        Assertions.assertEquals("ITI", res.get(1).getCode());
        Assertions.assertTrue(
            res.stream().anyMatch(j -> j.getLibelle().equals("AC/DC"))
        );
        Assertions.assertFalse(
            res.stream().anyMatch(j -> j.getLibelle().equals("Esiea"))
        );
    }

    @Test
    public void testGetListJournalComptable_TestData() {

            //Act
            List<JournalComptable> res = journalComptableService.getListJournalComptable();

            //Assert
            Assertions.assertNotNull(res);
            Assertions.assertEquals(2, res.size());
            Assertions.assertEquals("AC", res.get(0).getCode());
            Assertions.assertTrue(
                res.stream().anyMatch(j -> j.getLibelle().equals("Banque"))
            );
            Assertions.assertFalse(
                res.stream().anyMatch(j -> j.getLibelle().equals("Intech"))
            );
    }
}
