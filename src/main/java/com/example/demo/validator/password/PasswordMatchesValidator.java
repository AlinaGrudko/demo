package com.example.demo.validator.password;

import com.example.demo.entity.user.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, User> {

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
