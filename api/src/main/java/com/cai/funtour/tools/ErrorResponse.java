package com.cai.funtour.tools;

import com.cai.funtour.pojo.Result;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/29 16:42
 * @description：错误类型与返回结果的映射
 */
public class ErrorResponse {
    private static Map<String, Result> errMap;

    static {
        errMap.put("org.apache.dubbo.rpc.RpcException", Result.error(502, "无服务提供者"));
        errMap.put("org.springframework.web.HttpRequestMethodNotSupportedException", Result.error(HttpURLConnection.HTTP_BAD_METHOD, "请求方式错误"));
    }

    /**
     * 通过Exception的全限定类名获取对应的Result
     * @param className
     * @return
     */
    public static Result getErrorResult(String className){
        return errMap.getOrDefault(className, Result.error(502, "不明确的错误类型"));
    }
}
