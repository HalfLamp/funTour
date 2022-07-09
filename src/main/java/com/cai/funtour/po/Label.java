package com.cai.funtour.po;

import jdk.nashorn.internal.objects.annotations.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/6 14:38
 * @description：label表实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Label {
    Integer id;
    String type;
    String label;
    String uniqueId;
    Double score;
    String isUse;
    Date updateTime;


    public static boolean getInuse(Label label){
        return label.getIsUse().equals("1");
    }
    public static boolean getNotInuse(Label label){
        return label.getIsUse().equals("0");
    }
    public static Double getDoubleScore(Label label){
        return label.getScore();
    }
}
