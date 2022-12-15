package com.cmxz.snakesmartmemo.dao;

import com.cmxz.snakesmartmemo.pojo.IdAndPassword;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IdAndPasswordDao {
    /**
     * 获取Id和Password列表方法
     *
     * @return id password token列表
     */
    public List<IdAndPassword> getIdAndPasswordList();

    /**
     * 插入封装有id、password、token的Password对象
     *
     * @param IdAndPwd
     */
    //使用注解会自动roll back，不需要担心更改数据库数据
    @Insert("insert into id_and_passwords(id,password) values(#{IdAndPwd.id},#{IdAndPwd.password})")
    void insert(@Param("IdAndPwd") IdAndPassword IdAndPwd);

    /**
     * 查询对应id、password的用户信息，主要用于登录验证
     *
     * @param id
     * @param password
     * @return 对应id、password、token信息，若为null则不存在。
     */
    @Select("select * from ssm.id_and_passwords where id=#{id} and password = #{password}")
    IdAndPassword getByIdAndPwd(@Param("id") String id, @Param("password") String password);

    /**
     * 获取id对应的用户密码和token信息
     *
     * @param id 用户id
     * @return id对应的用户密码和token信息
     */
    @Select("select * from id_and_passwords where id=#{id}")
    IdAndPassword getById(String id);

    /**
     * 主要用于用户登录修改token
     *
     * @param id    用户id
     * @param token 修改后token的值
     * @return 匹配到的行数（如果想设置返回值是受影响的行数，修改数据库链接配置：增加 useAffectedRows=true 即可）
     */
    @Update("update id_and_passwords set token=#{token} where id=#{id}")
    int updateToken(@Param("id")String id, @Param("token")String token); //一定要记得@Param，不然找不到
}

