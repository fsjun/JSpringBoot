package org.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.*;
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

    @RequiresPermissions("pd")
    @RequestMapping(value = "/demo/perm", method = RequestMethod.GET)
    @ResponseBody
    public String perm(){
        log.info("perm log {}", "test");
        return "perm";
    }

    @RequiresPermissions("pderr")
    @RequestMapping(value = "/demo/pderr", method = RequestMethod.GET)
    @ResponseBody
    public String pderr(){
        log.info("pderr log {}", "test");
        return "pderr";
    }

    @RequiresRoles("user")
    @RequestMapping(value = "/demo/roles", method = RequestMethod.GET)
    @ResponseBody
    public String roles(){
        log.info("roles log {}", "test");
        return "roles";
    }

    @RequiresRoles("user1")
    @RequestMapping(value = "/demo/roleserr", method = RequestMethod.GET)
    @ResponseBody
    public String roleserr(){
        log.info("roleserr log {}", "test");
        return "roleserr";
    }
    @RequiresUser
    @RequestMapping(value = "/demo/user", method = RequestMethod.GET)
    @ResponseBody
    public String user(){
        log.info("user log {}", "test");
        return "user";
    }
    @RequiresGuest
    @RequestMapping(value = "/demo/guest", method = RequestMethod.GET)
    @ResponseBody
    public String guest(){
        log.info("guest log {}", "test");
        return "guest";
    }

    @GetMapping(value="/demo/userdb")
    public List<User> getUser() {
        return userService.queryAll();
    }
}
