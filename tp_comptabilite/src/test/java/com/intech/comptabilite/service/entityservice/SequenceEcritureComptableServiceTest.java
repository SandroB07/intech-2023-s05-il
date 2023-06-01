package com.intech.comptabilite.service.entityservice;

import com.intech.comptabilite.model.SequenceEcritureComptable;
import com.intech.comptabilite.model.SequenceId;
import com.intech.comptabilite.repositories.SequenceEcritureComptableRepository;
import com.intech.comptabilite.service.exceptions.NotFoundException;
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

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class SequenceEcritureComptableServiceTest
{

    @Autowired
    private SequenceEcritureComptableService service;

    @Autowired
    SequenceEcritureComptableRepository repository;

    @BeforeEach
    void beforeEach()
    {
        ReflectionTestUtils.setField(
            service,
            "repository",
            repository
        );
    }

    @Test
    public void testGetDernierValeurBySequence_Integration_NotFound() throws NotFoundException
    {

        SequenceEcritureComptableRepository rep = Mockito.mock(SequenceEcritureComptableRepository.class);
        ReflectionTestUtils.setField(
            service,
            "repository",
            rep
        );

        //Arrange
        when(rep.findById(Mockito.any(SequenceId.class))).thenReturn(Optional.empty());


        Assertions.assertThrows(
            NotFoundException.class,
            () -> {
                service.getDernierValeurSequenceId(SequenceId.createOrGet("AC", 2020));
                verify(rep).findById(Mockito.any(SequenceId.class));
            }
        );
    }

    @Test
    public void  testGetDernierValeurBySequenceId_Integration_Found() throws NotFoundException
    {

        SequenceEcritureComptableRepository rep =
                Mockito.mock(SequenceEcritureComptableRepository.class);
        ReflectionTestUtils.setField(
                service,
                "repository",
                rep
        );

        String code = "BQ";
        int annee = 2020;
        SequenceId id = SequenceId.createOrGet(code, annee);

        when(rep.findById(id))
            .thenReturn(
                Optional.of(
                    new SequenceEcritureComptable("BQ", 2020, 7)
                )
            );

        //Act
        int result = service.getDernierValeurSequenceId(id);
        verify(rep).findById(id);

        //Assert
        Assertions.assertDoesNotThrow(() -> new NotFoundException());
        Assertions.assertEquals(7, result);
    }

}
