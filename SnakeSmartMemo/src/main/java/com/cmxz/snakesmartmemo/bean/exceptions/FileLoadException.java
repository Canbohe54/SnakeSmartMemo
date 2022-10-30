package com.cmxz.snakesmartmemo.bean.exceptions;

import java.io.IOException;

public class FileLoadException extends IOException {
    public FileLoadException() {
        super("File Load Exception");
    }

    public FileLoadException(String message) {
        super(message);
    }
}
