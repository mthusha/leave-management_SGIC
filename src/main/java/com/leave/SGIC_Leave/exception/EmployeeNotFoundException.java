package com.leave.SGIC_Leave.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Order")
public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException (String error){
        super(error);
    }
}
