package com.bbs.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbs.backend.mapper.UserMapper;
import com.bbs.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//调试用类，非项目实际运行使用
@RestController
public class UserController {
    @Autowired
    UserMapper userMapper;

    //查询所有user
    @GetMapping("user/all/")
    public List<User> getAll() //查询user中的所有数据
    {
        return userMapper.selectList(null);
    }

    //根据id查询user
    @GetMapping("user/{userId}/")
    public User getUser(@PathVariable int userId)
    {
        /// return userMapper.selectById(userId);
        ///////或者使用条件构造器QueryWrapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);//找到id为userId的id. eq：等于， gt：大于，ge：大于等于，同理 lt/le

        return userMapper.selectOne(queryWrapper);
    }

    //插入
    @GetMapping("user/add/{userId}/{username}/{password}/")
    public String addUser(@PathVariable int userId,
                          @PathVariable String username,
                          @PathVariable String password) {
        //Encode password, 直接在数据库中存encode后的password，因为spring-security验证时，按照encode的密文验证
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(userId, username, encodedPassword, "");
        userMapper.insert(user);
        return "Insert successfully!";
    }

    //删除
    @GetMapping("user/delete/{userId}/")
    public String deleteUser(@PathVariable int userId) {
        userMapper.deleteById(userId);
        return "Delete successfully!";
    }
}
