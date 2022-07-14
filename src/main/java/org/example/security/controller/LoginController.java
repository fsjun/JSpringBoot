package org.example.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.example.security.pojo.JwtUser;
import org.example.security.pojo.User;
import org.example.security.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    @PostMapping("/login")
    public Map login(@RequestBody User userInput) throws Exception {

        String username = userInput.getUserName();
        String password = userInput.getPassword();

        log.info("login username:{} password:{}", username, password);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(usernamePasswordToken);

        Map<String,String> res = new HashMap<>();
        JwtUser jwtUser = (JwtUser) SecurityUtils.getSubject().getPrincipal();

        res.put("token",JwtUtil.createJwtTokenByUser(jwtUser));
        res.put("result","login success or other result message");
        return res;
    }

    @GetMapping("/whoami")
    public Map whoami(){
        JwtUser jwtUser = (JwtUser) SecurityUtils.getSubject().getPrincipal();

        Map<String,String> res=new HashMap<>();
        res.put("result","you are "+jwtUser);
        res.put("token", JwtUtil.createJwtTokenByUser(jwtUser));

        return res;
    }
}
