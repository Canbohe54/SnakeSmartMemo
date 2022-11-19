package com.cmxz.snakesmartmemo.dao;

import com.cmxz.snakesmartmemo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserDao {
    public List<User> getUserInfoList();
    @Select("select * from ssm.user_info where id = #{id} and username = #{username}")
    User getUserInfo(User user);
    /**
     * 插入方法
     * @param user 封装有学生信息的Student对象(数据库自增id
     * @return 受影响的行数
     */
    void insert(User user);
    int delete(User user);
    int update(User user);

}
