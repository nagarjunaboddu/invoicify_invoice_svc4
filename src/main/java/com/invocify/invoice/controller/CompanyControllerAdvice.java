package com.invocify.invoice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CompanyControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> handleNotFound(MethodArgumentNotValidException methodArgumentNotValidException) throws Exception {
        List<String> result = methodArgumentNotValidException.getAllErrors().stream().map(ObjectError::getDefaultMessage
        ).collect(Collectors.toList());
        Collections.sort(result);
        return result;
    }

}
