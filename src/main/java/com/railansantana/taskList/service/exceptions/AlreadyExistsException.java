package com.railansantana.taskList.service.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String msg){
        super(msg);
    }
}
