package com.negotiation.analysis.model.baidubce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.negotiation.common.util.CommonUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Component
public class LexicalAnalysis {

//    private static final String access_token_jack = "24.40a1d8991c3a07c9ff7609dad5d74a02.2592000.1664080023.282335-26369435";

    @ApiOperation("词法分析")
    public static ApiExplorerResponse analyze(String text) {
        String path = "https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);


        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置query参数
//        String access_token = BaiduAccessToken.getToken();    // 如果使用者不是jack，请使用getToken生成accesstoken
        request.addQueryParameter("access_token", BaiduAccessToken.jackAccessToken());
        request.addQueryParameter("charset", "UTF-8");

        // 设置jsonBody参数
        Map<String, Object> map = new HashMap<>();
        map.put("text", text);
        JSON json = new JSONObject(map);
        String jsonBody = json.toJSONString();
        request.setJsonBody(jsonBody);

        ApiExplorerClient client = new ApiExplorerClient();

        ApiExplorerResponse response = new ApiExplorerResponse();
        try {
            // 返回Json字符串
            response = client.sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
