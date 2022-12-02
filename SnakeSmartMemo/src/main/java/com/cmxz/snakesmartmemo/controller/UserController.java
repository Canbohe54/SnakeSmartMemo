package com.cmxz.snakesmartmemo.controller;

import com.cmxz.snakesmartmemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("ssm/users")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@RequestParam("id") String id, @RequestParam("user_name") String userName, @RequestParam("password") String password) {
        return userService.register(id, userName, password);
    }

    @RequestMapping(value = "echo", method = RequestMethod.GET)
    public String echo(@RequestParam("id") String id) {
        return userService.echo(id);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestParam("id") String id, @RequestParam("password") String password) {
        return userService.login(id, password);
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> share(@RequestParam("id") String id, @RequestParam("file") MultipartFile file, @RequestParam("filename") String filename) {
        return userService.upload(id, file, filename);
    }
}
