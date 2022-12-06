package com.cmxz.snakesmartmemo.dao;

import com.cmxz.snakesmartmemo.pojo.IdAndPassword;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
     * 查询对应id、password的用户信息
     *
     * @param id
     * @param password
     * @return 对应id、password、token信息，若为null则不存在。
     */
    @Select("select * from ssm.id_and_passwords where id=#{id} and password = #{password}")
    IdAndPassword getByIdAndPwd(@Param("id") String id, @Param("password") String password);

    /**
     * 该方法主要用于检测token是否到期，此处token由于时间关系，仅进行简单模拟
     *
     * @param id
     * @return
     */
    @Select("select * from id_and_passwords where id=#{id}")
    IdAndPassword getById(String id);
}

