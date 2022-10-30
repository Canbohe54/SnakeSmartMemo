package com.cmxz.snakesmartmemo.bean.exceptions;

import java.io.IOException;

public class DataSavingException extends IOException {
    public DataSavingException() {
        super("Data Saving Exception");
    }

    public DataSavingException(String message) {
        super(message);
    }
}
