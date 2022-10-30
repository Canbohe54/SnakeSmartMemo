package com.cmxz.snakesmartmemo.bean.exceptions;

public class SystemCalendarNotAllowedException extends PermissionException {
    SystemCalendarNotAllowedException() {
        super("System Calendar not Allowed Exception");
    }

    SystemCalendarNotAllowedException(String msg) {
        super(msg);
    }
}
