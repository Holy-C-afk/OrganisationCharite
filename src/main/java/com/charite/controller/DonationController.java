package com.charite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DonationController {
    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    @GetMapping("/donations")
    public ModelAndView donations() {
        logger.debug("Accessing donations page");
        try {
            ModelAndView mav = new ModelAndView("donations");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String username = auth.getName();
                logger.debug("User authenticated: {}", username);
                mav.addObject("username", username);
            } else {
                logger.debug("User not authenticated, setting as Guest");
                mav.addObject("username", "Guest");
            }
            
            logger.debug("Returning donations template");
            return mav;
        } catch (Exception e) {
            logger.error("Error in donations controller", e);
            throw e;
        }
    }
} 