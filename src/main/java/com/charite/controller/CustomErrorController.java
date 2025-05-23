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
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");
        String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
        
        logger.error("Error occurred: Status Code = {}, Exception = {}, Message = {}", 
            statusCode, 
            exception != null ? exception.getMessage() : "No exception",
            errorMessage != null ? errorMessage : "No message");
        
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage != null ? errorMessage : 
            (exception != null ? exception.getMessage() : "Une erreur inattendue s'est produite"));
        
        return "error";
    }
} 