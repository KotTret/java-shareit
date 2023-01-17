package ru.practicum.shareit.util.validation.myemail;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MyEmailConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyEmail {

    String message() default "Формат электронной почты указан неверно.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
