package com.charite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CharityActionController {
    private static final Logger logger = LoggerFactory.getLogger(CharityActionController.class);

    @GetMapping("/charity-actions")
    public ModelAndView charityActions() {
        logger.debug("Accessing charity actions page");
        try {
            ModelAndView mav = new ModelAndView("charity-actions");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String username = auth.getName();
                logger.debug("User authenticated: {}", username);
                mav.addObject("username", username);
            } else {
                logger.debug("User not authenticated, setting as Guest");
                mav.addObject("username", "Guest");
            }
            
            logger.debug("Returning charity-actions template");
            return mav;
        } catch (Exception e) {
            logger.error("Error in charity actions controller", e);
            throw e;
        }
    }
} 