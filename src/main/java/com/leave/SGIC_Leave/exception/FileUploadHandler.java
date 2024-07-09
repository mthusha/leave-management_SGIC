package com.leave.SGIC_Leave.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Check File")
public class FileUploadHandler extends RuntimeException{
    public FileUploadHandler(String error){
        super(error);
    }
}
