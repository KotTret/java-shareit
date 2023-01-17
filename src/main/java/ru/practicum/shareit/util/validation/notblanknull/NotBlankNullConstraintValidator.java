package ru.practicum.shareit.util.validation.notblanknull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NotBlankNullConstraintValidator implements ConstraintValidator<NotBlankNull, String> {

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        if (text == null) {
            return true;
        } else {
            return !text.isBlank();
        }
    }
}
