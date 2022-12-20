package com.cmxz.snakesmartmemo.controller;


import com.cmxz.snakesmartmemo.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("ssm")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;
    @RequestMapping(value = "share")
    public Map<String, Object> share() {
        Map<String, Object> re = new HashMap<>();
        re.put("statusMsg", "please login.");
        return re;
    }

    @RequestMapping(value = "event",method = RequestMethod.POST)
//    public Map<String, Object> event(@RequestParam("file") MultipartFile file) {
//        return visitorService.event(file);
//    }
    public Map<String, Object> event(@RequestParam("text") String text) {
        return visitorService.event(text);
    }
    @RequestMapping(value = "recognize",method = RequestMethod.POST)
    public Map<String,Object> recognize(@RequestParam("file")MultipartFile file){
        return visitorService.recognize(file);
    }
    @RequestMapping(value = "eventList", method = RequestMethod.POST)
    public Map<String, Object> eventList(@RequestParam("text") String text) {
        return visitorService.eventList(text);
    }
}
