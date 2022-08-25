package com.negotiation.analysis.model.baidubce;

import cn.hutool.core.util.StrUtil;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.negotiation.common.exception.BaiduAccessTokenNotFoundException;
import com.negotiation.common.util.CommonUtil;
import org.springframework.stereotype.Component;

@Component
public class BaiduAccessToken {

    private static final String APPLICATION_NAME = "NLP_test";
    private static final String API_KEY = "DZyBVo0Dlow6DkbLOrKilSCF";
    private static final String SECRET_KEY = "s9rXubq5G4GzqS1jW7SPb9eSOBKzfzmM";
    private static final String GRANT_TYPE = "client_credentials";

    private static final String ACCESS_TOKEN = "access_token";

    // 百度api返回Json中，access_token的位置在第三位
    private static final Integer ACCESS_TOKEN_LOC = 3;

    public static String getToken() {
        final String path = "https://aip.baidubce.com/oauth/2.0/token";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);

        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json; charset=utf-8");

        // 设置query参数
        request.addQueryParameter("client_id", API_KEY);
        request.addQueryParameter("client_secret", SECRET_KEY);
        request.addQueryParameter("grant_type", GRANT_TYPE);


        ApiExplorerClient client = new ApiExplorerClient();

        ApiExplorerResponse response = new ApiExplorerResponse();
        try {
            // 返回结果格式为Json字符串
            response = client.sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 通过Parser，找出access_token并返回
        String access_token = CommonUtil.baiduJsonToMap(response).get(ACCESS_TOKEN);
        if (StrUtil.isBlank(access_token)) {
            throw new BaiduAccessTokenNotFoundException();
        }
        return access_token;
    }
}
