package com.capitan.encuestas.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.capitan.encuestas.annotations.UniqueEmail;
import com.capitan.encuestas.entities.UserEntity;
import com.capitan.encuestas.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        UserEntity user = userRepository.findByEmail(value);
        if (user == null) {
            return true;
        }
        
        return false;
    }
    
}
