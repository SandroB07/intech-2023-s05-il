package com.intech.comptabilite.service.businessmanager;

import com.intech.comptabilite.model.*;
import com.intech.comptabilite.service.entityservice.CompteComptableService;
import com.intech.comptabilite.service.entityservice.EcritureComptableService;
import com.intech.comptabilite.service.entityservice.JournalComptableService;
import com.intech.comptabilite.service.entityservice.SequenceEcritureComptableService;
import com.intech.comptabilite.service.exceptions.FunctionalException;
import com.intech.comptabilite.service.exceptions.NotFoundException;
import jakarta.validation.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ComptabiliteManagerImpl implements ComptabiliteManager {

	private final EcritureComptableService ecritureComptableService;
	private final JournalComptableService journalComptableService;
	private final CompteComptableService compteComptableService;
	private final SequenceEcritureComptableService sequenceEcritureComptableService;

    public ComptabiliteManagerImpl(EcritureComptableService ecritureComptableService,
                                   JournalComptableService journalComptableService,
                                   CompteComptableService compteComptableService,
                                   SequenceEcritureComptableService sequenceEcritureComptableService)
    {
        this.ecritureComptableService = ecritureComptableService;
        this.journalComptableService = journalComptableService;
        this.compteComptableService = compteComptableService;
        this.sequenceEcritureComptableService = sequenceEcritureComptableService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return compteComptableService.getListCompteComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JournalComptable> getListJournalComptable() {
       return journalComptableService.getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
    	return ecritureComptableService.getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     */
    // TODO à implémenter et à tester
    // Tested in Integration Test
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)
                2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                        1. Utiliser le numéro 1.
                    * Sinon :
                        1. Utiliser la dernière valeur + 1
                3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
                4.  Enregistrer (insert/update) la valeur de la séquence en persitance
                    (table sequence_ecriture_comptable)
         */

        Integer sequence = null;
        try {
            sequence = sequenceEcritureComptableService.getDernierValeurSequenceId(
                SequenceId.createOrGet(
                    pEcritureComptable.getJournal().getCode(),
                    pEcritureComptable.getDate().getYear()
                )
            ) + 1;
        } catch (NotFoundException e)
        {
            sequence = 1;
        }

        StringBuilder sb = new StringBuilder()
                .append(pEcritureComptable.getJournal().getCode())
                .append("-")
                .append(pEcritureComptable.getDate().getYear())
                .append("/")
                .append("0".repeat(Math.max(0, 5 - sequence.toString().length())))
                .append(sequence);

        pEcritureComptable.setReference(sb.toString());

        SequenceEcritureComptable seq =
                new SequenceEcritureComptable(
                        pEcritureComptable.getJournal().getCode(),
                        pEcritureComptable.getDate().getYear(),
                        sequence
                );

        sequenceEcritureComptableService.upsert(seq);

       // return pEcritureComptable;
    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);

    }


    /**
     * {@inheritDoc}
     */
    // TODO tests à compléter
    public void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                                          new ConstraintViolationException(
                                              "L'écriture comptable ne respecte pas les contraintes de validation",
                                              vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!ecritureComptableService.isEquilibree(pEcritureComptable)) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
            || vNbrCredit < 1
            || vNbrDebit < 1) {
            throw new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }

        // TODO ===== RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...

        String reference = pEcritureComptable.getReference();
        Pattern pattern = Pattern.compile("^([A-Za-z0-9]{1,5})-(\\d{4})/(\\d{5})$");
        Matcher matcher = pattern.matcher(reference);

        if (matcher.find() && matcher.matches()) {
            String code = matcher.group(1);
            String year = matcher.group(2);
            String sequence = matcher.group(3);

            if (!code.equals(pEcritureComptable.getJournal().getCode())) {
            	throw new FunctionalException("Le code journal dans la référence ne correspond pas au code journal de l'écriture comptable.");
            }

            if (Integer.parseInt(year) != pEcritureComptable.getDate().getYear()) {
            	throw new FunctionalException("L'année dans la référence ne correspond pas à la date de l'écriture comptable.");
            }

            if (sequence.length() != reference.substring(reference.lastIndexOf("/") + 1).length()) {
                throw new FunctionalException("La longueur de la séquence dans la référence ne correspond pas à la longueur de la séquence dans l'écriture comptable.");
            }
        } else {
            throw new FunctionalException("Le format de la référence n'est pas valide.");
        }

    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = ecritureComptableService.getEcritureComptableByRef(pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            }  catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        //pEcritureComptable =
        this.addReference(pEcritureComptable);
        this.checkEcritureComptable(pEcritureComptable);
        ecritureComptableService.insertEcritureComptable(pEcritureComptable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        ecritureComptableService.updateEcritureComptable(pEcritureComptable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
       ecritureComptableService.deleteEcritureComptable(pId);
    }
    
    protected Validator getConstraintValidator() {
        Configuration<?> vConfiguration = Validation.byDefaultProvider().configure();
        ValidatorFactory vFactory = vConfiguration.buildValidatorFactory();
        return vFactory.getValidator();
    }
}
