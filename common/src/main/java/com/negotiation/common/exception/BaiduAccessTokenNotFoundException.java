package com.negotiation.common.exception;

public class BaiduAccessTokenNotFoundException extends RuntimeException{

    public BaiduAccessTokenNotFoundException() {
        super("百度access_token获取失败");
    }
}
