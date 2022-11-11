package com.cmxz.snakesmartmemo.controller;

import com.cmxz.snakesmartmemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestParam("id") String id,@RequestParam("user_name") String userName,@RequestParam("password") String password){
        String msg= "";
        return msg;
    }
    @RequestMapping(value = "login",method = RequestMethod.GET)
    @ResponseBody
    public String echo(@RequestParam("id") String id){
        String back = userService.echo(id);
        return back;
    }
}
