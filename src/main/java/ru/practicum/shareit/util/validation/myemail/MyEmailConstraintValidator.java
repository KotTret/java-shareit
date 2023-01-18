package ru.practicum.shareit.util.validation.myemail;

import org.apache.commons.validator.routines.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class MyEmailConstraintValidator implements ConstraintValidator<MyEmail, String> {

    private final static EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null) {
            return true;
        }
        return emailValidator.isValid(email);
    }
}
