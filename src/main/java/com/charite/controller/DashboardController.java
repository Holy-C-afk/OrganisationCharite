package com.charite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        logger.debug("Accessing dashboard");
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                logger.warn("Unauthorized access attempt to dashboard");
                return "redirect:/login";
            }

            String username = userDetails != null ? userDetails.getUsername() : auth.getName();
            logger.debug("User authenticated: {}", username);
            model.addAttribute("username", username);
            return "dashboard";
        } catch (Exception e) {
            logger.error("Error accessing dashboard", e);
            return "redirect:/error";
        }
    }
} 