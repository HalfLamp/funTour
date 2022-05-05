package com.cai.funtour.mapper;

import com.cai.funtour.po.User;
import com.cai.funtour.pojo.Result;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/5/1 20:47
 * @description：用户mapper
 */
@Repository
public interface UserMapper {
    public Map<String, Object> login(@Param("account") String account, @Param("password") String password);

    public int registry(User user);
}
