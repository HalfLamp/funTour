package com.cai.funtour.api.pub;

import com.cai.funtour.pojo.Result;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 16:28
 * @description：用户接口
 */
public interface User {
    Result login(String account, String password);
    Result register(User user);
}
