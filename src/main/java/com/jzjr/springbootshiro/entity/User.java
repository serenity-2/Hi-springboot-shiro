package com.jzjr.springbootshiro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;

    private String userName;

    private String passWord;

    private String salt;

    private String phoneNumber;

    private String WxOpenId;

    private List<Role>roles;

    private Date loginDate;

    private Date createdDate;

    private Date updatedDate;
}
