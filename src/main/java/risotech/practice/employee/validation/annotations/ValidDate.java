package risotech.practice.employee.validation.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import javax.validation.Constraint;
import javax.validation.Payload;

import risotech.practice.employee.validation.validators.DateValidator;

@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
    String message() default "birthday項目の日付が無効です";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}