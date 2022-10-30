package com.cmxz.snakesmartmemo.bean.exceptions;

public abstract class PasswdException extends Exception {

	public PasswdException() {
		super("Password Exception");
	}

	public PasswdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message);
	}

	public PasswdException(String message, Throwable cause) {
		super(message);
	}

	public PasswdException(String message) {
		super(message);
	}

	public PasswdException(Throwable cause) {
		super("Password Exception");
	}
	//禁用其他方法，防止抛出用户密码
}
