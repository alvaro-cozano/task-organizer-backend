package com.project.management.springboot.backend.project_management.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.management.springboot.backend.project_management.services.card.CardService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByCardTitleValidation implements ConstraintValidator<ExistsByCardTitle, String> {

    @Autowired
    private CardService service;

    @Override
    public boolean isValid(String cardTitle, ConstraintValidatorContext context) {
        if (service == null) {
            return true;
        }
        return !service.existsByCardTitle(cardTitle);
    }
}
