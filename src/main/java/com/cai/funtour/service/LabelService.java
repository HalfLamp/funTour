package com.cai.funtour.service;

import com.cai.funtour.mapper.LabelMapper;
import com.cai.funtour.po.Label;
import com.cai.funtour.tools.LabelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/6 10:16
 * @description：标签业务处理
 */
@Slf4j
@Service
public class LabelService {
    @Autowired
    LabelMapper labelMapper;

    // 根据uniqueId获取标签
    public List<Label> getLabel(String uniqueId, LabelType type) {
        List<Label> result = null;
        switch (type) {
            case USER:
                result = labelMapper.getUserLabels(uniqueId);
                log.info("根据uniqueId: {}获取用户标签列表：{}", uniqueId, result);
                break;
            case SIGHT:
                result = labelMapper.getSightLabels(uniqueId);
                log.info("根据uniqueId: {}获取景点标签列表：{}", uniqueId, result);
                break;
        }
        return result;
    }

    // 更新用户标签权重
    public int updateScore(Label label) {
        int rows = labelMapper.updateUserScore(label);
        if (rows == 1) {
            log.info("新增用户标签：{}", label);
        } else if (rows == 2) {
            log.info("修改用户标签{}权重为:{}", label.getId(), label.getScore());
        } else {
            log.info("权重一致，无需修改，id:{}", label.getId());
        }
        return rows;
    }

    public int deleteUserInuseLabel(String userId) {
        return labelMapper.deleteUserInuseLabel(userId);
    }

    // 更新用户未使用标签的权重
    @Transactional(rollbackFor = Exception.class)
    public int updateUserLabelWeight(List<Label> sightLabels, Double weight, String userId) {
        for (Label sightLabel : sightLabels) {
            Double score = sightLabel.getScore();
            Label label = new Label();
            label.setIsUse("0");
            label.setScore(weight);
            label.setUniqueId(userId);
            label.setType(LabelMapper.USER_LABEL);
            label.setLabel(sightLabel.getLabel());

            updateScore(label);
        }
        return 1;
    }

    // 更新用户在使用中的标签权重
    @Transactional(rollbackFor = Exception.class)
    public int updateWeight(String userId, List<Label> inuseList) {
        int delRows = deleteUserInuseLabel(userId);
        int updateRows = 0;
        // 计算总值
        double total = inuseList.stream().mapToDouble(Label::getDoubleScore).sum();
        for (Label label : inuseList) {
            // 计算所占权重
            label.setScore(Label.getDoubleScore(label) / total);
            label.setIsUse("1");
            updateRows += updateScore(label);
        }
        return delRows + updateRows;
    }
}

