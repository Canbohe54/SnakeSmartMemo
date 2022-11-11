package com.cmxz.snakesmartmemo.dao;

import com.cmxz.snakesmartmemo.pojo.Password;

import java.util.List;

public interface IdAndPasswordDao {
    /**
     * 获取Id和Password列表方法
     *
     * @return id password 列表
     */
    public List<Password> getIdAndPasswordList();
}

