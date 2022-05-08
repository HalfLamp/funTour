package com.cai.funtour.service;

import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.mapper.UserMapper;
import com.cai.funtour.po.User;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.TraceId;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/29 11:38
 * @description：用户操作接口
 */
@Service
@Slf4j
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Result login(String account, String password) {
        Map<String, Object> userInfo = userMapper.login(account, password);
        if (userInfo == null) {
            return Result.error(200, "账号或密码错误");
        }
        return Result.toData("user", userInfo);
    }

    @Override
    public Result register(User user) {
        // 使用traceid算法作为uid
        String userid = TraceId.getTraceId();
        user.setUserId(userid);

        int rows = userMapper.registry(user);
        if (rows > 0) {
            return Result.toData("user", user);
        } else {
            return Result.error(111, "注册失败");
        }
    }
}
