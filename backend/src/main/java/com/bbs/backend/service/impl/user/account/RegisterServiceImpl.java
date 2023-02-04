package com.bbs.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.backend.mapper.UserMapper;
import com.bbs.backend.pojo.User;
import com.bbs.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    //查询数据库的用户名，防止重复注册相同的用户名
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirm_password) {
        Map<String, String> map = new HashMap<>();
        if (username == null)
        {
            map.put("receive_message", "请输入用户名！");
            return map;
        }
        if (password == null || confirm_password == null)
        {
            map.put("receive_message", "请输入密码！");
            return map;
        }

        //删除空白字符
        username = username.trim();

        if (username.length() == 0)
        {
            map.put("receive_message", "用户名不能为空！");
            return map;
        }
        if (username.length() > 100)
        {
            map.put("receive_message", "用户名的长度不能超过100字符！");
            return map;
        }
        if (password.length() == 0 || confirm_password.length() == 0)
        {
            map.put("receive_message", "密码不能为空！");
            return map;
        }
        if (password.length() > 100 || confirm_password.length() > 100)
        {
            map.put("receive_message", "密码长度不能超过100字符！");
            return map;
        }
        if (!password.equals(confirm_password))
        {
            map.put("receive_message", "两次输入的密码不一致！");
            return map;
        }

        //判断注册框中填写的用户名是否已经被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);//找到username是username的实体
        List<User> users = userMapper.selectList(queryWrapper);//如果已经存在，则会select到，即users不为空
        if (!users.isEmpty())
        {
            map.put("receive_message", "该用户名已经被注册！");
            return map;
        }

        String encodedPassword =passwordEncoder.encode(password);
        //用知乎默认匿名头像代替下哈哈
        String photo = "https://pic1.zhimg.com/v2-abed1a8c04700ba7d72b45195223e0ff_l.jpg?source=1940ef5c";
        User user = new User(null, username, encodedPassword, photo);
        userMapper.insert(user); //加入数据库
        map.put("receive_message", "success");

        return map;
    }
}
