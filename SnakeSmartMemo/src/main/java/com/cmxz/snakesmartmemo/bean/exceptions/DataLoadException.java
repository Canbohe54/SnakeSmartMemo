package com.cmxz.snakesmartmemo.bean.exceptions;

public class DataLoadException extends FileLoadException{
    public DataLoadException() {
        super("Data Load Exception");
    }

    public DataLoadException(String message) {
        super(message);
    }
}
