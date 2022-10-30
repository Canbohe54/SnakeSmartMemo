package com.cmxz.snakesmartmemo.bean.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){super("User not found Exception");}
}
