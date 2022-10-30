package com.cmxz.snakesmartmemo.bean.exceptions;

public class ShareException extends RuntimeException{
    ShareException(){super("Share Exception");}
    ShareException(String msg){super(msg);}
}
