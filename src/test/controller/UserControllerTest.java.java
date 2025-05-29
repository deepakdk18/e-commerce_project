package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UserRegistrationDto;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private UserController userController;

    @Test
    public void showRegistrationForm_ShouldReturnRegistrationView() {
        String viewName = userController.showRegistrationForm(model);
        
        assertEquals("registration", viewName);
        verify(model).addAttribute(eq("user"), any(UserRegistrationDto.class));
        verify(model).addAttribute("pageTitle", "Register - ECommerce");
    }

    @Test
    public void registerUserAccount_ValidInput_ShouldRedirectToLogin() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        
        when(bindingResult.hasErrors()).thenReturn(false);
        
        String viewName = userController.registerUserAccount(userDto, bindingResult, redirectAttributes);
        
        assertEquals("redirect:/registration?success", viewName);
        verify(userService).save(userDto);
        verify(redirectAttributes, never()).addFlashAttribute(anyString(), any());
    }

    @Test
    public void registerUserAccount_InvalidInput_ShouldReturnToRegistration() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        
        when(bindingResult.hasErrors()).thenReturn(true);
        
        String viewName = userController.registerUserAccount(userDto, bindingResult, redirectAttributes);
        
        assertEquals("registration", viewName);
        verify(userService, never()).save(any());
    }

    @Test
    public void registerUserAccount_PasswordMismatch_ShouldAddError() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setPassword("password");
        userDto.setConfirmPassword("different");
        
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("Passwords don't match"))
            .when(userService).save(userDto);
        
        String viewName = userController.registerUserAccount(userDto, bindingResult, redirectAttributes);
        
        assertEquals("registration", viewName);
        verify(bindingResult).rejectValue("confirmPassword", "error.user", "Passwords don't match");
    }

    @Test
    public void showLoginForm_ShouldReturnLoginView() {
        String viewName = userController.showLoginForm();
        
        assertEquals("login", viewName);
    }

    @Test
    public void showUserProfile_AuthenticatedUser_ShouldReturnProfileView() {
        User user = new User();
        user.setEmail("test@example.com");
        
        when(userService.getCurrentUser()).thenReturn(user);
        
        String viewName = userController.showUserProfile(model);
        
        assertEquals("user/profile", viewName);
        verify(model).addAttribute("user", user);
    }
}