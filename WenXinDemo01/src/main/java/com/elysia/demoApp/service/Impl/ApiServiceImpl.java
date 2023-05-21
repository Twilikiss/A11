package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.service.ApiService;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cxb
 * @ClassName ApiServiceImpl
 * @date 17/5/2023 下午9:04
 */
@Service
public class ApiServiceImpl implements ApiService {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private String client_id = "H4BehRxuD1lbIlj9Snss8hCh";
    private String client_secret = "j4IEEgUTfDIbMLtoxSiqDfIArMGMIl34";

    @Override
    public String getAccessToken() {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/oauth/2.0/token?client_id=" + client_id + "&client_secret=" + client_secret + "&grant_type=client_credentials")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                String accessToken = jsonObject.getString("access_token");
                return accessToken;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getText2Image(String accessToken, String resolution, String style, Integer num, String text) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            Map<String, Object> param = new HashMap<>();
            param.put("text", text);
            param.put("resolution", resolution);
            param.put("style", style);
            param.put("num", num);
            String s = JSONObject.toJSONString(param);
            RequestBody body =
                    RequestBody.create(s, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ernievilg/v1/txt2img?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkText2Image(String accessToken, String taskId) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            Map<String, Object> param = new HashMap<>();
            param.put("taskId", taskId);
            String s = JSONObject.toJSONString(param);
            RequestBody body =
                    RequestBody.create(s, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ernievilg/v1/getImg?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
