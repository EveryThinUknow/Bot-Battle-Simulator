package com.bbs.backend.controller.battle;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
@Controller标识的类，代表控制器类(控制层/表现层)。
*/
@RestController //@controller和@ResponseBody 的结合,该类所有API返回的数据会以Json字符串的形式返回
@RequestMapping("/battle/") //@RequestMapping(),用于映射请求
public class BotInfoController {
    @RequestMapping("getBotInfo/")
    public String getBotInfo()
    {
        return "connect successfully";
    }

}
