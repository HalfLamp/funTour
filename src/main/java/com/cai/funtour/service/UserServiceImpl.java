package com.cai.funtour.service;

import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.po.User;
import com.cai.funtour.pojo.Result;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/29 11:38
 * @description：
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public Result login(String account, String password) {
        return null;
    }

    @Override
    public Result register(User user) {
        return null;
    }
}
