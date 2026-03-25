package com.sara.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sara.entity.User;
import com.sara.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           HttpSession session,
                           Model model) {
        AuthenticationException authException =
                (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        if (error != null) {
            if (authException != null && authException.getMessage() != null
                    && !authException.getMessage().isBlank()) {
                model.addAttribute("error", authException.getMessage());
            } else {
                model.addAttribute("error", "Invalid username or password!");
            }
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        // Include all roles except ADMIN
        model.addAttribute("userRoles", java.util.Arrays.stream(User.UserRole.values())
            .filter(role -> role != User.UserRole.ADMIN)
            .toList());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        System.out.println("[DEBUG] Register attempt: " + user);
        if (result.hasErrors()) {
            System.out.println("[DEBUG] Validation errors: " + result.getAllErrors());
            model.addAttribute("userRoles", java.util.Arrays.stream(User.UserRole.values())
                .filter(role -> role != User.UserRole.ADMIN)
                .toList());
            model.addAttribute("error", "Please correct the highlighted errors and try again.");
            return "auth/register";
        }
        // Additional email validation only
        if (user.getEmail() == null || !user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("[DEBUG] Invalid email: " + user.getEmail());
            model.addAttribute("error", "Please enter a valid and complete email address.");
            model.addAttribute("userRoles", java.util.Arrays.stream(User.UserRole.values())
                .filter(role -> role != User.UserRole.ADMIN)
                .toList());
            return "auth/register";
        }
        // Check if username already exists
        if (userService.existsByUsername(user.getUsername())) {
            System.out.println("[DEBUG] Username already exists: " + user.getUsername());
            model.addAttribute("error", "Username already exists!");
            model.addAttribute("userRoles", java.util.Arrays.stream(User.UserRole.values())
                .filter(role -> role != User.UserRole.ADMIN)
                .toList());
            return "auth/register";
        }
        // Check if email already exists
        if (userService.existsByEmail(user.getEmail())) {
            System.out.println("[DEBUG] Email already exists: " + user.getEmail());
            model.addAttribute("error", "Email already exists!");
            model.addAttribute("userRoles", java.util.Arrays.stream(User.UserRole.values())
                .filter(role -> role != User.UserRole.ADMIN)
                .toList());
            return "auth/register";
        }
        // Save the user
        try {
            userService.saveUser(user);
            System.out.println("[DEBUG] User saved successfully: " + user.getUsername());
            // Different message for volunteers who need verification
            if (user.getRole() == User.UserRole.VOLUNTEER) {
                redirectAttributes.addFlashAttribute("success", 
                    "Registration successful! Your volunteer account is pending admin approval. You will be able to login once verified.");
            } else {
                redirectAttributes.addFlashAttribute("success", 
                    "Registration successful! Please login with your credentials.");
            }
            return "redirect:/login";
        } catch (Exception e) {
            System.out.println("[ERROR] Registration exception: " + e.getMessage());
            model.addAttribute("error", "Registration failed. Please try again or contact support.");
            model.addAttribute("userRoles", java.util.Arrays.stream(User.UserRole.values())
                .filter(role -> role != User.UserRole.ADMIN)
                .toList());
            return "auth/register";
        }
    }
}