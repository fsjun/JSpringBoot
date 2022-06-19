package org.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.demo.pojo.User;
import org.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class DemoController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/demo/simple", method = RequestMethod.GET)
    @ResponseBody
    public String simple(){
        log.info("demo log {}", "test");
        return "demo";
    }

    @GetMapping(value="/demo/user")
    public List<User> getUser() {
        return userService.queryAll();
    }
}
