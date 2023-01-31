package com.bbs.backend.service.impl.user.account;

import com.bbs.backend.pojo.User;
import com.bbs.backend.service.impl.utils.UserDetailsImpl;
import com.bbs.backend.service.user.account.LoginService;
import com.bbs.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        //该方法会自动处理登陆失败的结果，返回异常
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        //取得后端数据库中的user
        User user = loginUser.getUser();
        //根据用户ID生成jwt
        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String, String> map = new HashMap<>();
        map.put("receive_message", "success");
        map.put("token", jwt);

        return map;
    }

}
