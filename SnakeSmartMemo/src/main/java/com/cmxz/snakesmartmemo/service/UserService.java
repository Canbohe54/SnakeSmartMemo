package com.cmxz.snakesmartmemo.service;

public interface  UserService {
    public String echo(String id);
}
class UserServerImpl implements UserService {
    public String echo(String id){
        return id;
    }
}
