package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlingController {
    @Value( "${spring.servlet.multipart.max-request-size}" )
    private String maxFileSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleFileUploadException(HttpServletRequest req, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMsg", "Uploaded file size exceeds " +
                "maximum possible file size limit of "+maxFileSize+". Please upload files of size " +
                "less than or equal to "+maxFileSize+".");
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest req, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMsg", ex.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
