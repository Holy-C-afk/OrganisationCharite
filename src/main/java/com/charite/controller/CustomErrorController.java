package com.charite.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        
        logger.error("Error occurred: Status Code = {}, Exception = {}", statusCode, exception != null ? exception.getMessage() : "No exception");
        
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", exception != null ? exception.getMessage() : "Une erreur inattendue s'est produite");
        
        return "error";
    }
} 