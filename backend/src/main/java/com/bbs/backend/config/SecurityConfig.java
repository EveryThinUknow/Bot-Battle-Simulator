package com.bbs.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//在controller.user.UserController.java中存储encode后的用户名密码（security默认判断哈希后的密码的字符串）
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //调用security中的“BCryptPasswordEncoder()”加密方法，对注册的密码进行加密存储
        //当login时，matches()查看输入的密码转译成字符串后，和config中存储的密码密文是否匹配 是否相同
        return new BCryptPasswordEncoder();
    }
}