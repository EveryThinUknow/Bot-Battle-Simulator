package com.bbs.backend.service.user.account;

import java.util.Map;

public interface InfoService {
    //实现接口，service包中的java类实现接口，编写具体逻辑，最后在controller包的java类中实现对service里的功能的调用
    public Map<String, String> getInfo();
}
