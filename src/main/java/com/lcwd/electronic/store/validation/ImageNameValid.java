package com.lcwd.electronic.store.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {

    //default error message
    String message() default "Invalid Image Name";

    //represent group of annotations
    Class<?>[] groups() default {};

    //represent additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
