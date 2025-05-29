package com.example.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotEmpty(message = "First name should not be empty")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    @NotEmpty(message = "Address should not be empty")
    private String address;

    @NotEmpty(message = "Phone number should not be empty")
    @Size(min = 10, max = 15, message = "Phone number should be between 10-15 characters")
    private String phone;

    // Optional: Password confirmation field
    @NotEmpty(message = "Password confirmation should not be empty")
    private String confirmPassword;

    // Optional: Getter for full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}