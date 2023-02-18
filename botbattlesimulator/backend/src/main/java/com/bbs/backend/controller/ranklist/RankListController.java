package com.bbs.backend.controller.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.bbs.backend.service.rank.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RankListController {
    @Autowired
    private RankListService rankListService;

    @GetMapping("/rank/get/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));

        return rankListService.getList(page);
    }


}
