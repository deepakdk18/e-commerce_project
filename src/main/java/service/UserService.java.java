package com.example.ecommerce.service;

import com.example.ecommerce.dto.UserRegistrationDto;
import com.example.ecommerce.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
    User getCurrentUser();
}