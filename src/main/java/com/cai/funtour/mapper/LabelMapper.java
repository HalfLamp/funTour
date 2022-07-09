package com.cai.funtour.mapper;

import com.cai.funtour.po.Label;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/6 10:15
 * @description：标签mapper
 */
@Component
public interface LabelMapper {
    String USER_LABEL = "用户";
    String SIGHT_LABEL = "景点";
    String USER_LABEL_MAPPER = "'用户'";
    String SIGHT_LABEL_MAPPER = "'景点'";

    // 获取景点标签列表
    List<Label> getSightLabels(String sightId);
    // 获取用户标签列表
    List<Label> getUserLabels(String userId);
    // 新增用户标签
    int insertUserLabels(@Param("userId") String userId, @Param("labelId")String labelId);
    // 更新用户标签
    int updateUserLabel(@Param("userId") String userId, @Param("labelId") String labelId);
    // 更新用户标签权重
    int updateUserScore(Label label);
    // 删除用户所有正在使用的标签
    int deleteUserInuseLabel(String userId);
}
