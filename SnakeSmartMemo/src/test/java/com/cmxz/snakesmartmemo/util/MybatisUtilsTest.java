package com.cmxz.snakesmartmemo.util;

import com.cmxz.snakesmartmemo.dao.IdAndPasswordDao;
import com.cmxz.snakesmartmemo.dao.UserDao;
import com.cmxz.snakesmartmemo.pojo.Password;
import com.cmxz.snakesmartmemo.pojo.User;

import junit.framework.TestCase;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MybatisUtilsTest extends TestCase {
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

    @Test
    public void testIdAndPasswordList() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        IdAndPasswordDao idAndPwdDao = sqlSession.getMapper(IdAndPasswordDao.class);
        List<Password> idAndPwdList = idAndPwdDao.getIdAndPasswordList();

        for (Password idAndPwd : idAndPwdList) {
            System.out.println(idAndPwd);
        }

        sqlSession.close();
    }
}