package com.bbs.backend.service.rank;

import com.alibaba.fastjson.JSONObject;

public interface RankListService {
    JSONObject getList(Integer page);
}
