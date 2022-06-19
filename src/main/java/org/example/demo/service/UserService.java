package org.example.demo.service;

import org.example.demo.dao.UserMapper;
import org.example.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    public User queryByName(String name) {
        return userMapper.queryByName(name);
    }
}
