package com.charite.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        logger.error("Unhandled exception occurred", e);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage() != null ? e.getMessage() : "Une erreur inattendue s'est produite");
        return mav;
    }
} 