package com.cmxz.snakesmartmemo.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Password implements Serializable {
    private String id;
    private String password;
    private String token;

    public Password(String id, String password, String token) {
        this.id = id;
        this.password = password;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Password{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
