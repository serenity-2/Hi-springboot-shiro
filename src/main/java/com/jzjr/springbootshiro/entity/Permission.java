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
public class Permission {
    private String id;

    private String name;

    private String url;

    private Date created_date;

    private Date updated_date;
}
