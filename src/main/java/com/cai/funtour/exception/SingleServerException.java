package com.cai.funtour.exception;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/26 16:08
 * @description：单实例服务错误
 */
public class SingleServerException extends RuntimeException {
    public SingleServerException(String message) {
        super(message);
    }

    public SingleServerException() {
        super();
    }
}
