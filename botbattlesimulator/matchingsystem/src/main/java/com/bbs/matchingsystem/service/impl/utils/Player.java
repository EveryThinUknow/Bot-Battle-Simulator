package com.bbs.matchingsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer userId; //你的ID
    private Integer rating; //你的分数
    private Integer botId; //对战选择的bot
    private Integer matchingTime; //匹配耗时
}
