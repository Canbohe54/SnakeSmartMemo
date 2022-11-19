package com.cmxz.snakesmartmemo.bean.exceptions;

public abstract class PasswdException extends Exception {

	public PasswdException() {
		super("IdAndPassword Exception");
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
		super("IdAndPassword Exception");
	}
	//禁用其他方法，防止抛出用户密码
}
