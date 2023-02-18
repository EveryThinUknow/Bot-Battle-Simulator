package com.bbs.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.bbs.backend.service.record.RecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RecordListController {
    @Autowired
    private RecordListService recordListService;

    @GetMapping("/record/get/")
    JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));

        return recordListService.getList(page);
    }
}
