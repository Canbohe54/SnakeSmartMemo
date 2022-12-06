package com.cmxz.snakesmartmemo.dao;

import com.cmxz.snakesmartmemo.pojo.IdAndPassword;
import com.cmxz.snakesmartmemo.util.MybatisUtils;
import junit.framework.TestCase;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class IdAndPasswordDaoTest extends TestCase {
    @Test
    public void testIdAndPasswordList() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        IdAndPasswordDao idAndPwdDao = sqlSession.getMapper(IdAndPasswordDao.class);
        List<IdAndPassword> idAndPwdList = idAndPwdDao.getIdAndPasswordList();

        for (IdAndPassword idAndPwd : idAndPwdList) {
            System.out.println(idAndPwd);
        }

        sqlSession.close();
    }

    @Test
    public void testGetByIdAndPwd() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        IdAndPassword demo = new IdAndPassword("3","mkymky",null);
        IdAndPasswordDao idAndPwdDao = sqlSession.getMapper(IdAndPasswordDao.class);
        IdAndPassword info = idAndPwdDao.getByIdAndPwd("3", "mkymky" );

        System.out.println(info);

        sqlSession.close();
    }

    @Test
    public void testInsert(){
        //shiy
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        IdAndPassword demo = new IdAndPassword("4","mkymky4","");
        IdAndPasswordDao idAndPwdDao = sqlSession.getMapper(IdAndPasswordDao.class);

        idAndPwdDao.insert(demo);

        sqlSession.close();
    }
}