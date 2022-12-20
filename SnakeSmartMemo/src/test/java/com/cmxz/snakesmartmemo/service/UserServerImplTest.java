package com.cmxz.snakesmartmemo.service;

import com.cmxz.snakesmartmemo.dao.UserDao;
import com.cmxz.snakesmartmemo.pojo.User;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServerImplTest extends TestCase {
//    @Mock
//    private UserDao userDao;
//
//    @InjectMocks
//    private UserServerImpl userServer;
//
//    @Test
//    public void testRegister() {
//        User mockUser = new User("xzq1","4");
//        //mock服务或者类中的某个方法，当参数是什么时，返回值是什么
//        //Mockito.when(userDao.getUserInfo(mockUser)).thenReturn(mockUser);
//        Mockito.when(userDao.getUserInfo(mockUser)).thenReturn(null);
//
//        //执行单元测试逻辑
//        Map<String, Object> res = userServer.register("4","xzq1","000000");
//        System.out.println(res);
    //}
}