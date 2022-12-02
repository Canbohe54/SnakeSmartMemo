package com.cmxz.snakesmartmemo.bean.exceptions;

public class TokenExpirationTimeException extends ShareException{
    public TokenExpirationTimeException(){
        super("TokenExpirationTimeException");
    }
    public TokenExpirationTimeException(String msg){
        super(msg);
    }
}
