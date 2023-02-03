package com.bbs.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbs.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

//数据库接口，连接pojo/User存储的数据库bbs的user表
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
