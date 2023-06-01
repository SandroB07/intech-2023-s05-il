package com.intech.comptabilite.service.entityservice;

import com.intech.comptabilite.model.CompteComptable;
import com.intech.comptabilite.repositories.CompteComptableRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CompteComptableServiceTest
{

    @Autowired
    private CompteComptableService compteComptableService;

    @Autowired
    CompteComptableRepository compteComptableRepository;

    @BeforeEach
    void beforeEach()
    {
        ReflectionTestUtils.setField(
            compteComptableService,
            "repository",
            compteComptableRepository
        );
    }

    @Test
    public void testGetByNumero_TestData_NotFound() {

        //Arrange
        List<CompteComptable> vResult = compteComptableService.getListCompteComptable();

        //Act
        CompteComptable res = compteComptableService.getByNumero(vResult, 19);

        //Assert
        Assertions.assertNull(res);
    }

    @Test
    public void testGetByNumero_TestData_Found() {

        //Arrange
        List<CompteComptable> vResult = compteComptableService.getListCompteComptable();

        //Act
        CompteComptable res = compteComptableService.getByNumero(vResult, 16);

        //Assert
        Assertions.assertNotNull(res);
        Assertions.assertEquals(16, res.getNumero());
        Assertions.assertEquals("Matteo", res.getLibelle());
    }

    @Test
    public void testGetListCompteComptable_Integration() {

        //Arrange
        CompteComptableRepository rep = Mockito.mock(CompteComptableRepository.class);
        ReflectionTestUtils.setField(
            compteComptableService,
            "repository",
            rep
        );

        List<CompteComptable> toReturn =
            new ArrayList<>() {

                {
                    add(new CompteComptable(1, "test"));
                    add(new CompteComptable(2, "test2"));
                    add(new CompteComptable(3, "test3"));
                    add(new CompteComptable(198, "test4"));
                }

            };


        when(rep.findAll()).thenReturn(toReturn);

        //Act
        List<CompteComptable> result = compteComptableService.getListCompteComptable();

        //Assert
        verify(rep).findAll();
        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals(198, result.get(3).getNumero());

    }

    @Test
    public void testGetListComptable_TestData() {

        //Act
        List<CompteComptable> result = compteComptableService.getListCompteComptable();

        //Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(
            result.stream()
                    .anyMatch(c -> c.getLibelle().equals("Matteo"))
        );

        Assertions.assertFalse(
            result.stream()
                    .anyMatch(c -> c.getLibelle().equals("Godzilla"))
        );
    }
}
