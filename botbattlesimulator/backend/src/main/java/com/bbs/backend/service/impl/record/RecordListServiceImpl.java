package com.bbs.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.backend.mapper.RecordMapper;
import com.bbs.backend.mapper.UserMapper;
import com.bbs.backend.pojo.Record;
import com.bbs.backend.pojo.User;
import com.bbs.backend.service.record.RecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RecordListServiceImpl implements RecordListService {
    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer page) {
        //引用mybatis的page分页功能
        IPage<Record> recordIPage = new Page<>(page, 8); //每页十个
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();

        //分页后发送给前端
        JSONObject resp = new JSONObject();
        List<JSONObject> infos = new LinkedList<>();
        for (Record record: records)
        {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject info = new JSONObject();
            info.put("a_photo", userA.getPhoto());
            info.put("a_username", userA.getUsername());
            info.put("b_photo", userB.getPhoto());
            info.put("b_username", userB.getUsername());
            info.put("record", record);
            String res = "平";
            if ("A".equals(record.getLoser()))
            {
                res = "B胜";
            }
            else if ("B".equals(record.getLoser()))
            {
                res = "A胜";
            }
            info.put("result", res);
            info.put("play_time", record.getCreatetime().toString());

            infos.add(info);
        }
        resp.put("records", infos);
        resp.put("records_count", recordMapper.selectCount(null));

        return resp;
    }
}
