package com.cai.funtour.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 16:29
 * @description：返回结果
 */

@Getter
@Setter
@ToString
public class Result implements Serializable {
    private Integer code;
    private String errMes;
    private Object data;

    public Result() {
    }

    public Result(Integer code, String errMes) {
        this.code = code;
        this.errMes = errMes;
    }

    public Result(Integer code, String errMes, Object data) {
        this.code = code;
        this.errMes = errMes;
        this.data = data;
    }

    public static Result ok(){
        return new Result(200,"",null);
    }

    public static Result error(String mes){
        return new Result(500,mes,null);
    }

    public static Result error(Integer code, String mes){
        return new Result(code,mes,null);
    }

    public static Result toData(Map data){
        return new Result(200,"",data);
    }

    public static Result toData(String key, Object value){
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put(key, value);
        return new Result(200,"",data);
    }

    public static Result toData(Object value){
        return new Result(200,"",value);
    }
}

