package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        log.error("Error occurred - Status: {}, Message: {}, URI: {}", status, message, uri);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return new ModelAndView("error-404");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return new ModelAndView("error-500");
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                log.error("Bad Request - Parameters: {}", request.getParameterMap());
                return new ModelAndView("error-400");
            }
        }
        return new ModelAndView("error");
    }
} 