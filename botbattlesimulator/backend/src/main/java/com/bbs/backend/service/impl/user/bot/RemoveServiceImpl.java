package com.bbs.backend.service.impl.user.bot;

import com.bbs.backend.mapper.BotMapper;
import com.bbs.backend.pojo.Bot;
import com.bbs.backend.pojo.User;
import com.bbs.backend.service.impl.utils.UserDetailsImpl;
import com.bbs.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(bot_id);
        Map<String, String> map = new HashMap<>();

        if (bot == null) {
            map.put("receive_message", "该Bot并不存在！");
            return map;
        }

        if (!bot.getUserId().equals(user.getId()))
        {
            map.put("receive_message", "该Bot不属于您，无删除权限！");
            return map;
        }

        botMapper.deleteById(bot_id);
        map.put("receive_message", "success");

        return map;
    }
}
