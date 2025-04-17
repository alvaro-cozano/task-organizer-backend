package com.project.management.springboot.backend.project_management.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.management.springboot.backend.project_management.services.board.BoardService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByBoardNameValidation implements ConstraintValidator<ExistsByBoardName, String> {

    @Autowired
    private BoardService service;

    @Override
    public boolean isValid(String boardName, ConstraintValidatorContext context) {
        if (service == null) {
            return true;
        }
        return !service.existsByBoardName(boardName);
    }
}
