package com.cmxz.snakesmartmemo.bean.exceptions;

public class SystemContactNotAllowedException extends PermissionException {
    SystemContactNotAllowedException() {
        super("System Contact not Allowed Exception");
    }

    SystemContactNotAllowedException(String msg) {
        super(msg);
    }
}
