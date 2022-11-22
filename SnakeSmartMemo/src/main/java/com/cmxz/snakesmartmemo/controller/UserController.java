package com.cmxz.snakesmartmemo.controller;

import com.cmxz.snakesmartmemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "register",method = RequestMethod.POST)
    public String register(@RequestParam("id") String id,@RequestParam("user_name") String userName,@RequestParam("password") String password){
        String msg= userService.register(id, userName, password);
        return msg;
    }
    @RequestMapping(value = "echo",method = RequestMethod.GET)
    public String echo(@RequestParam("id") String id){
        String back = userService.echo(id);
        return back;
    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public Map<String,Object> login(@RequestParam("id")String id, @RequestParam("password")String password){
        Map<String,Object> resp = userService.login(id,password);
        return resp;
    }
}
