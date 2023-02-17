package com.bbs.backend.controller.battle.user.account;

import com.bbs.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    //用post实现用户名和密码到后端的传输
    @PostMapping("/user/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) { //存入字典
        String username = map.get("username");
        String password = map.get("password");

        return loginService.getToken(username, password);
    }

}
