package com.cmxz.snakesmartmemo.service;

import com.cmxz.snakesmartmemo.dao.UserDao;
import com.cmxz.snakesmartmemo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

public interface  UserService {
     String echo(String id);
     String register(String id,String userName,String password);

}
@Mapper
class UserServerImpl implements UserService {
    @Autowired
    private UserDao userDao;
    public String echo(String id){
        return id;
    }

    public String register(String id,String userName,String password){
        User newUser= new User(userName,id);
        User exist = userDao.getUserInfo(newUser);

        if(exist != null){
            return "user has been exist";
        }
        userDao.insert(newUser);
        return "register successfully";
    }
}
