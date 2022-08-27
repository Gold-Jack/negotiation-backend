package com.negotiation.analysis.model.baidubce;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.negotiation.common.util.CommonUtil;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.common.value.qual.IntRange;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Component
public class ShortTextSimilarity {

    @ApiOperation("对两个短文本做相似度分析")
    public static Double analyze(String text_1, String text_2) {
        String path = "https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);


        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置query参数
        request.addQueryParameter("access_token", BaiduAccessToken.jackAccessToken());
        request.addQueryParameter("charset", "UTF-8");


        // 设置jsonBody参数
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("text_1", text_1);
        jsonMap.put("text_2", text_2);
        String jsonBody = new JSONObject(jsonMap).toString();
        request.setJsonBody(jsonBody);

        ApiExplorerClient client = new ApiExplorerClient();

        ApiExplorerResponse response = new ApiExplorerResponse();
        try {
            // 返回结果格式为Json字符串
             response = client.sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String scoreStr = CommonUtil.baiduJsonToMap(response).get("score");
        double similarityScore = Double.parseDouble(scoreStr);
        return similarityScore;
    }
}
