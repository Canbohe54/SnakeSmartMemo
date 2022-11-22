package com.cmxz.snakesmartmemo.service;

import com.cmxz.snakesmartmemo.bean.exceptions.UserNotFoundException;
import com.cmxz.snakesmartmemo.dao.IdAndPasswordDao;
import com.cmxz.snakesmartmemo.dao.UserDao;
import com.cmxz.snakesmartmemo.pojo.IdAndPassword;
import com.cmxz.snakesmartmemo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public interface  UserService {
     String echo(String id);
     String register(String id,String userName,String password);
     Map<String,Object> login(String id,String password);

}
@Service
@Mapper
class UserServerImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IdAndPasswordDao idAndPasswordDao;
    public String echo(String id){
        return id;
    }

    /**
     * 注册
     * @param id 唯一id（自动生成）
     * @param userName 用户名
     * @param password 密码存储需要加密
     * @return 返回注册是否成功
     */
    public String register(String id,String userName,String password){
        //新建User对象，并查找是否已存在
        User newUser= new User(userName,id);
        User uExist = userDao.getUserInfoById(id);

        IdAndPassword newIdAndPassword = new IdAndPassword(id,password,null);
//        IdAndPassword ipExist = idAndPasswordDao.getByIdAndPwd(newIdAndPassword);
        if(uExist != null){
            return "user has been exist";
        }

        //将信息插入user_info，id_and_passwords表
        userDao.insert(newUser);
        idAndPasswordDao.insert(newIdAndPassword);
        return "register successfully";
    }

    public Map<String,Object> login(String id, String password){
        Map<String,Object> response = new HashMap<>();


        try {
            User exist = userDao.getUserInfoById(id);
            if(exist==null){
                throw new UserNotFoundException();
            }
            response.put("statusMsg","success");
            response.put("userInfo",exist);
            return response;
        }catch (UserNotFoundException e){
            response.put("statusMsg","UserNotFoundException");
            response.put("userInfo","");
            return response;
        }
    }
}
