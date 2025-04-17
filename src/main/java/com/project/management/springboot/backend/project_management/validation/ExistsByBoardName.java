package com.project.management.springboot.backend.project_management.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistsByBoardNameValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByBoardName {
    String message() default "ya existe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}