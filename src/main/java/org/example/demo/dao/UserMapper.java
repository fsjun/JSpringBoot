package org.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.demo.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<User> queryAll();
    public User queryByName(String name);
    public int add(User user);
}
