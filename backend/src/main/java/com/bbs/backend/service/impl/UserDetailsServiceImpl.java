package com.bbs.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.backend.mapper.UserMapper;
import com.bbs.backend.pojo.User;
import com.bbs.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//spring security 逻辑实现
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired //读取数据库
    private UserMapper userMapper;

    @Override //Override重写,
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username); //找到username为字符串username的数据库表格中的实体
        User user = userMapper.selectOne(queryWrapper);//让user等于该实体
        if (user == null) //如果不存在该数据
        {
            throw new RuntimeException("User doesn't exist!");
        }
        return new UserDetailsImpl(user); //返回对应于数据库中user表里username = username的实体
    }
}
