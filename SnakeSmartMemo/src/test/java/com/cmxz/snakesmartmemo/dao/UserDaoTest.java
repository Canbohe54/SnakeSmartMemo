package com.cmxz.snakesmartmemo.dao;

import com.cmxz.snakesmartmemo.pojo.User;
import com.cmxz.snakesmartmemo.util.MybatisUtils;

import junit.framework.TestCase;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest extends TestCase {

    @Test
    public void testGetUserInfoList() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserDao userDao = sqlSession.getMapper(UserDao.class);
        List<User> userList = userDao.getUserInfoList();

        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }

    public void testGetUserInfo() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User demo = new User("xzq","0");
        User user = userDao.getUserInfo(demo);

        System.out.println(user);

        sqlSession.close();
    }
    @Test
    public void testInsert(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User demo = new User("xzq1","4");

        userDao.insert(demo);
        sqlSession.close();
    }
}