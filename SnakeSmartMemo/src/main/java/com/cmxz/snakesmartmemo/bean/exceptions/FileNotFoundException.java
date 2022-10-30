package com.cmxz.snakesmartmemo.bean.exceptions;

import java.io.IOException;

public class FileNotFoundException extends IOException {
    public FileNotFoundException() {
        super("File not Found Exception");
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
