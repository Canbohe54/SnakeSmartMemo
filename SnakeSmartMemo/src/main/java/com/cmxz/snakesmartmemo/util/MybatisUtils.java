package com.cmxz.snakesmartmemo.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
//sqlSessionFactory -->SqlSession
public class MybatisUtils {
        private static SqlSessionFactory sqlSessionFactory;

        static {
            try {
                //使用Mybatis第一步: 获取sqlSessionFactory对象
                String resource = "mybatis-config.xml";
                InputStream inputStream = Resources.getResourceAsStream(resource);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
        //sqlSession 完全包含了面向数据库执行SQL命令所需的所有方法
        public static SqlSession getSqlSession() {
            // SqlSession sqlSession = sqlSessionFactory.openSession();
            return sqlSessionFactory.openSession();
        }

}
