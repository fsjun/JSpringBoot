package org.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.example.demo.dao.UserRepository;
import org.example.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> queryAll() {
        return userRepository.findAll();
    }

    public User queryByName(String name) {
        return userRepository.queryByName(name);
    }

    public int add(User user) {
        user = userRepository.saveAndFlush(user);
        return user.getId();
    }

    public int modify(User user) {
        userRepository.saveAndFlush(user);
        return 1;
    }

    public boolean delete(int id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;
    }
}
