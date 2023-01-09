package org.example.demo.dao;

import org.example.demo.pojo.User;
import org.example.demo.vo.UserInt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "select * from sys_user_t where name=?1", nativeQuery = true)
    UserInt queryByName(String name);
}
