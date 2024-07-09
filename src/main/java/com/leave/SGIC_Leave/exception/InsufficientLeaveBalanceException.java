package com.leave.SGIC_Leave.exception;

public class InsufficientLeaveBalanceException extends RuntimeException{
    public InsufficientLeaveBalanceException(String error){
        super(error);
    }
}
