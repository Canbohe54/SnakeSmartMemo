package com.cmxz.snakesmartmemo.dao;

import com.cmxz.snakesmartmemo.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface UserDao {
    /**
     * 获取所有用户信息
     * @return 所有用户信息列表
     */
    public List<User> getUserInfoList();
    @Select("select * from ssm.user_info where id = #{id} and username = #{username}")
    User getUserInfo(User user);

    /**
     * 插入方法
     * @param user 封装有用户信息的User对象
     *
     */
    @Insert("insert into user_info(id,username) values(#{user.id},#{user.username})")
    void insert(@Param("user") User user);

    /**
     * 通过id查找是否存在该用户
     * @param id
     * @return
     */
    @Select("select * from user_info where id = #{id}")
    User getUserInfoById(@Param("id") String id);
    int delete(User user);
    int update(User user);

}
