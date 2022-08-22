package org.example.demo.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_user_t")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String password;
    private String remark;
}
