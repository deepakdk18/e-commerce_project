package com.example.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "An error occurred: " + ex.getMessage());
        return "redirect:/error";
    }
}