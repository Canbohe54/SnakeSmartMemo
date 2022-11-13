package com.cmxz.snakesmartmemo.service;

import org.apache.ibatis.annotations.Mapper;


public interface  UserService {
     String echo(String id);
}
@Mapper
class UserServerImpl implements UserService {
    public String echo(String id){
        return id;
    }
}
