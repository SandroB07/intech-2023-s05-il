package com.intech.comptabilite.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;

import java.lang.annotation.*;


/**
 * Contrainte à apposer sur les attibuts de type "montant comptable"
 *
 *  Cette contrainte est composée de :
 *  <ul>
 *      <li>@{@link Digits}</li>
 *  </ul>
 *
 *  Types supportés :
 *  <ul>
 *      <li>{@link java.math.BigDecimal}</li>
 *  </ul>
 */
@Digits(integer = 13, fraction = 2)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface MontantComptable {

    /** Message de la violation */
    String message() default "Taux de TVA invalide";

    /** Groupe de validation */
    Class<?>[] groups() default {};

    /** Payload */
    Class<? extends Payload>[] payload() default {};

    /**
     * Interface permettant la déclaration de plusieurs {@link MontantComptable}
     */
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        /** List des {@link MontantComptable} */
        MontantComptable[] value();
    }
}
