package com.cmxz.snakesmartmemo.bean.exceptions;

import java.io.IOException;

public class PermissionException extends IOException {
    PermissionException() {
        super("Permission Exception");
    }

    PermissionException(String msg) {
        super(msg);
    }
}
