package com.bbs.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //无参构造函数
@AllArgsConstructor//有参构造函数
public class User {
    private Integer id;
    private String username;
    private String password;
}
