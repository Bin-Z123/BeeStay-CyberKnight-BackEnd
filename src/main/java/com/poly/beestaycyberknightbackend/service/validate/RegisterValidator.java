package com.poly.beestaycyberknightbackend.service.validate;

import org.springframework.stereotype.Service;

import com.poly.beestaycyberknightbackend.dto.request.RegisterRequest;
import com.poly.beestaycyberknightbackend.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


@Service
public class RegisterValidator implements ConstraintValidator<RegisterChecked, RegisterRequest> {

    private final UserService userService;

    public RegisterValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(RegisterRequest user, ConstraintValidatorContext context) {
        boolean valid = true;

        // Check if password fields match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Passwords nhập không chính xác")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        // Additional validations can be added here
        // check email
        if (this.userService.checkEmailExist(user.getEmail())) {
            context.buildConstraintViolationWithTemplate("Email đã tồn tại")
                    .addPropertyNode("email")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        return valid;
    }
}