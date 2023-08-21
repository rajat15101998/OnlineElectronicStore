package com.lcwd.electronic.store.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

    @Override
    public boolean isValid(String imageName, ConstraintValidatorContext constraintValidatorContext) {
           return (imageName.isEmpty() ? false : true);
    }
}
