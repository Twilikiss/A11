package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.Repository.Text2ImageRepository;
import com.elysia.demoApp.model.result.Text2ImageResult;
import com.elysia.demoApp.model.vo.Text2ImageAdvancedSaveVo;
import com.elysia.demoApp.model.vo.Text2ImageAdvancedVo;
import com.elysia.demoApp.service.AiPlatformApiService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.FileUtils;
import com.elysia.demoApp.utils.RedisUtils;
import com.elysia.demoApp.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cxb
 * @ClassName ApiServiceImpl
 * @date 17/5/2023 下午9:04
 */
@Service
@Slf4j
public class AiPlatformApiServiceImpl implements AiPlatformApiService {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private AccessTokenUtils accessTokenUtils;
    @Resource
    private Text2ImageRepository text2ImageRepository;
    @Value("${user.file.reference_path}")
    private String referencePath;
    @Value("${user.file.server}")
    private String server;
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private String client_id = "3MUOWMnqIeI2HbY0G0lWxkNq";
    private String client_secret = "9AZid4GnQzTb93zFmTW8FkEm1jlGLYXt";


    @Override
    public Map<String, Object> getText2ImageBase(String resolution, String style, Integer num, String text, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getAiPlayGroundAccessToken();
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
                String result = response.body().string();
                // 将相关的数据保存到mongodb
                Text2ImageResult text2ImageResult = new Text2ImageResult();
                // 初始化参数
                text2ImageResult.setCreateTime(new Date());
                text2ImageResult.setUpdateTime(new Date());
                text2ImageResult.setIsDeleted(0);

                text2ImageResult.setUid(uid);

                Map<String, Object> input = new HashMap<>();
                input.put("text", text);
                input.put("resolution", resolution);
                input.put("style", style);
                input.put("num", num);
                JSONObject inputJSONObject = new JSONObject(input);

                text2ImageResult.setInput(inputJSONObject);
                text2ImageResult.setOutput(JSON.parseObject(result));
                text2ImageResult.setType("base");
                Text2ImageResult temp = text2ImageRepository.insert(text2ImageResult);
                Map<String, Object> data = new HashMap<>();
                data.put("result_data",JSON.parseObject(result));
                data.put("id", temp.getId());
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkText2Image(String taskId) {
        String accessToken = this.getAiPlayGroundAccessToken();
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

    @Override
    public Map<String, Object> getText2ImageAdvanced(Text2ImageAdvancedVo text2ImageAdvancedVo, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getAiPlayGroundAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            Map<String, Object> param = new HashMap<>();
            param.put("prompt", text2ImageAdvancedVo.getPrompt());
            param.put("version", text2ImageAdvancedVo.getVersion());
            param.put("width", text2ImageAdvancedVo.getWidth());
            param.put("height", text2ImageAdvancedVo.getHeight());
            param.put("image_num", text2ImageAdvancedVo.getImageNum());
            if (!StringUtils.isEmpty(text2ImageAdvancedVo.getImageUrl())) {
                param.put("url", text2ImageAdvancedVo.getImageUrl());
                param.put("change_degree", text2ImageAdvancedVo.getChangeDegree());
            }
            String s = JSONObject.toJSONString(param);
            RequestBody body =
                    RequestBody.create(s, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ernievilg/v1/txt2imgv2?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                JSONObject resultJsonObject = JSON.parseObject(result);
                Map<String, Object> finalResult = new HashMap<>();
                finalResult.put("results", resultJsonObject);
                // 如果有参考图的画就一并返回回去，没有的话就为""
                finalResult.put("image_url", text2ImageAdvancedVo.getImageUrl());
                return finalResult;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String updateReferenceImage(MultipartFile file, HttpServletRequest req) {
        String referenceUrl = FileUtils.upload(file, referencePath, server);
        return referenceUrl;
    }

    @Override
    public String checkText2ImageAdvanced(String taskId) {
        String accessToken = this.getAiPlayGroundAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            Map<String, Object> param = new HashMap<>();
            param.put("task_id", taskId);
            String s = JSONObject.toJSONString(param);
            RequestBody body =
                    RequestBody.create(s, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ernievilg/v1/getImgv2?access_token=" + accessToken)
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
    public Map<String, Object> saveDataByUrl(Text2ImageAdvancedSaveVo text2ImageAdvancedSaveVo, String url, HttpServletRequest req) {
        String serverUrl = FileUtils.downloadImageByUrl(url, "/data/wenxin/text2image/Advanced");
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        // 将相关的数据保存到mongodb
        Text2ImageResult text2ImageResult = new Text2ImageResult();
        // 初始化参数
        text2ImageResult.setCreateTime(new Date());
        text2ImageResult.setUpdateTime(new Date());
        text2ImageResult.setIsDeleted(0);

        text2ImageResult.setUid(uid);
        JSONObject inputJSONObject = (JSONObject) JSONObject.toJSON(text2ImageAdvancedSaveVo);
        text2ImageResult.setInput(inputJSONObject);

        Map<String, Object> output = new HashMap<>();
        output.put("ai_painting_result", serverUrl);
        JSONObject outputJSONObject = new JSONObject(output);
        //TODO 验证是否可以正确存储
        text2ImageResult.setOutput(outputJSONObject);
        text2ImageResult.setType("advanced");
        Text2ImageResult temp = text2ImageRepository.insert(text2ImageResult);
        Map<String, Object> result = new HashMap<>();
        result.put("serverUrl", serverUrl);
        result.put("id", temp.getId());
        return result;
    }

    /**
     * @return java.lang.String
     * @ClassName ApiServiceImpl
     * @author cxb
     * @date 23/5/2023 下午10:24
     */
    public String getAiPlayGroundAccessToken() {
        // 判断redis是否已经有token
        boolean aiPlayground = redisUtils.hasKey("ai_playground");
        if (aiPlayground) {
            // redis中存在有就直接从redis取
            String accessToken = String.valueOf(redisUtils.get("ai_playground"));
            log.debug("ai_playground_token过期时间" + redisUtils.getExpire("ai_playground"));
            return accessToken;
        }
        String response = accessTokenUtils.getAccessTokenInCloudBd(client_id, client_secret);
        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getString("access_token");
        String expiresIn = jsonObject.getString("expires_in");
        redisUtils.set("ai_playground", accessToken, Integer.parseInt(expiresIn));
        return accessToken;
    }
}
