package com.bbs.backend.service.impl.user.bot;

import com.bbs.backend.mapper.BotMapper;
import com.bbs.backend.pojo.Bot;
import com.bbs.backend.pojo.User;
import com.bbs.backend.service.impl.utils.UserDetailsImpl;
import com.bbs.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        //提取操作客户端的用户的信息
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String bot_name = data.get("bot_name");
        String characterization = data.get("characterization");
        String details = data.get("details");

        Map<String, String> map = new HashMap<>();
        if (bot_name == null || bot_name.length() == 0)
        {
            map.put("receive_message", "Bot的名称不能为空");
            return map;
        }

        if (bot_name.length() > 203)
        {
            map.put("receive_message", "Bot的名称过长");
        }

        if (characterization != null && characterization.length() > 203)
        {
            map.put("receive_message", "Bot的介绍过长！");
        }

        if (characterization == null && characterization.length() == 0)
        {
            characterization = "该用户未添加该Bot的信息描述";
        }

        if (details == null || details.length() == 0)
        {
            map.put("receive_message", "Bot的逻辑代码不能为空");
        }

        if (details != null && details.length() > 12023)
        {
            map.put("receive_message", "Bot的代码容量超出限制");
        }

        Date create_time = new Date();

        Bot bot = new Bot(null, user.getId(), bot_name, characterization, details, 1000,
                create_time, create_time);
        botMapper.insert(bot);

        map.put("receive_message", "success");

        return map;
    }
}
