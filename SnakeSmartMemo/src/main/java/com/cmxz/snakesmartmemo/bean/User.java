package com.cmxz.snakesmartmemo.bean;

import java.io.Serial;
import java.io.Serializable;
import org.springframework.util.DigestUtils;

import com.cmxz.snakesmartmemo.bean.exceptions.IncorrectPasswdException;
import com.cmxz.snakesmartmemo.bean.exceptions.NewPasswdNotMatchException;
import com.cmxz.snakesmartmemo.bean.exceptions.PasswdException;
import com.cmxz.snakesmartmemo.bean.exceptions.PasswdTooWeakException;

public class User implements Serializable {
	public static final String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$";
	// 强密码校验正则表达式：必须同时具有字母、数字、特殊符号
	@Serial
	private static final long serialVersionUID = -5025988945599797167L;
	private long userId = 0;
	private String userName = "";
	private String passwdmd5 = "";
	private User[] friends;

	public User(long userId, String userName, String passwd, String passwdagain) throws PasswdException {
		this.userId = userId;
		this.userName = userName;
		this.setPasswd(passwd,passwdagain);
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPasswdmd5(String passwdmd5) {//只可用于序列化

		this.passwdmd5 = passwdmd5;
	}

	public User[] getFriends() {
		return friends;
	}

	public void setFriends(User[] friends) {
		this.friends = friends;
	}
	public void setPasswd(String passwd,String passwdagain) throws PasswdException {
		if(passwd != passwdagain) {
			throw new NewPasswdNotMatchException();
		}
		if (!passwd.matches(regex)) {
			throw new PasswdTooWeakException();
		}
		this.passwdmd5 = DigestUtils.md5DigestAsHex(passwd.getBytes());
	}
	public void changePasswd(String oldPasswd, String newPasswd, String newPasswdagain)
			throws PasswdException {
		if (this.passwdmd5 != DigestUtils.md5DigestAsHex(oldPasswd.getBytes())) {
			throw new IncorrectPasswdException();
		}
		if(newPasswd != newPasswdagain) {
			throw new NewPasswdNotMatchException();
		}
		if (!newPasswd.matches(regex)) {
			throw new PasswdTooWeakException();
		}
		this.passwdmd5 = DigestUtils.md5DigestAsHex(newPasswd.getBytes());
	}
	public boolean confirmPasswd(String passwd) {
		if (this.passwdmd5 != DigestUtils.md5DigestAsHex(passwd.getBytes())) {
			return false;
		}
		return true;
	}
}
