package com.cmxz.snakesmartmemo.bean.exceptions;

public class InvalidTimeException extends RuntimeException {
    InvalidTimeException() {
        super("Invalid Time Exception");
    }

    InvalidTimeException(String msg) {
        super(msg);
    }
}
