package com.cmxz.snakesmartmemo.controller;

import com.cmxz.snakesmartmemo.service.UserService;
import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
//@RunWith(SpringRunner.class)
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
//@SpringBootTest(classes = Application.class)
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest extends TestCase {
    //需要mock的服务
    @Mock
    UserService userService;

    //上面mock的数据需要注入到哪里
    @InjectMocks
    UserController userController;
/*
    为了兼容之前的版本，在运行项目时添加以下参数：
            --add-opens java.base/java.lang=ALL-UNNAMED
*/

    @Test
    public void testEcho() {
//        String msg = "1";
//        //mock服务或者类中的某个方法，当参数是什么时，返回值是什么
//        Mockito.when(userService.echo("1")).thenReturn(msg);
//
//        //执行单元测试逻辑
//        String result = userController.echo("1");
//        Assert.assertEquals("1", result);
    }
}