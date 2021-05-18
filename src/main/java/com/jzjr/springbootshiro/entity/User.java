package com.jzjr.springbootshiro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;

    private String userName;

    private String passWord;

    private String salt;

    private Date createdDate;

    private Date updatedDate;
}
