package com.negotiation.analysis.model.baidubce;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.nio.charset.Charset;

@Component
public class LexicalAnalysis {

    public static ApiExplorerResponse forward() {
        String path = "https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);


        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置query参数
        String access_token = BaiduAccessToken.getToken();
        request.addQueryParameter("access_token", access_token);
        request.addQueryParameter("charset", "UTF-8");

        // 设置jsonBody参数
        String jsonBody = "{\n  \"text\": \"百度是一家高科技公司\"\n}";
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
