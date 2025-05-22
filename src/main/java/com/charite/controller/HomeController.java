package com.charite.controller;

import com.charite.model.User;
import com.charite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        logger.debug("Accessing home page");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            logger.debug("User is authenticated, redirecting to dashboard");
            return "redirect:/dashboard";
        }
        logger.debug("User is not authenticated, showing home page");
        return "home";
    }

    @GetMapping("/home")
    public String homePage() {
        logger.debug("Accessing home page via /home");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        logger.debug("Accessing login page");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            logger.debug("User is already authenticated, redirecting to dashboard");
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.debug("Accessing registration form");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            logger.debug("User is already authenticated, redirecting to dashboard");
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        logger.info("Received registration request for email: {}", user.getEmail());
        try {
            User registeredUser = userService.registerUser(user);
            logger.info("User registered successfully with ID: {}", registeredUser.getId());
            redirectAttributes.addFlashAttribute("success", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Registration failed for email: {}", user.getEmail(), e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'inscription : " + e.getMessage());
            return "redirect:/register";
        }
    }
} 