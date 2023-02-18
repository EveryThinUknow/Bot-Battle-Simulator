package com.bbs.backend.service.record;

import com.alibaba.fastjson.JSONObject;

public interface RecordListService {
    JSONObject getList(Integer page);
}
