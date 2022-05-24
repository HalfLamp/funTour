package com.cai.funtour.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 16:31
 * @description：用户类型
 */

@Data
@NoArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String userId;
    private String name;
    private String phone;
    private String password;
    private String trend1;
    private String trend2;
    private String trend3;
    private String isProhibit;
    private String prohibitReason;
    private String regionId;
    private Integer age;
    private String sex;
    private String icon;
    private String image;
    private String sign;
    private String email;
    private String isMember;
    private String extra;
    private String createTime;
    private String updateTime;
    private String token;
}
