package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.result.Image2TextResult;
import com.elysia.demoApp.model.result.ImageClassifyResult;
import com.elysia.demoApp.model.vo.PromptGenerationVo;
import com.elysia.demoApp.service.NlpService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author cxb
 * @ClassName NlpServiceImpl
 * @date 30/5/2023 下午6:47
 */
@Service
@Slf4j
public class NlpServiceImpl implements NlpService {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private AccessTokenUtils accessTokenUtils;
    private String client_id = "yPNA7bGZtMBaMwEfj9Az93Li";
    private String client_secret = "soFNhbaYCLbYlXDnKddumpZAyRvApq10";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    @Override
    public String textKeyWord(String text, int num) {
        String accessToken = this.getNlpAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("{\"text\":[\"" + text + "\"],\"num\":" + num + "}", mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/txt_keywords_extraction?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTextErrorCorrection(String words) {
        String accessToken = this.getNlpAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create("{\"text\":\"" + words + "\"}", mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/text_correction?charset=UTF-8&access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createPoemByKeyword(String text, Integer index, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getNlpAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create("{\"text\":\"" + text + "\",\"index\":" + index + "}", mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/poem?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                Image2TextResult image2TextResult = new Image2TextResult();
                // 初始化参数
                image2TextResult.setCreateTime(new Date());
                image2TextResult.setUpdateTime(new Date());
                image2TextResult.setIsDeleted(0);

                // 这里image2Text的输入在上传图像进行提取时进行存储
                image2TextResult.setUid(uid);
                image2TextResult.setOutput(JSON.parseObject(result));
                image2TextResult.setType("poem");
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sentimentClassify(String input) {
        String accessToken = this.getNlpAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create("{\"text\":\"" + input + "\"}", mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?charset=&access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getCommentTag(String input) {
        String accessToken = this.getNlpAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create("{\"text\":\"" + input + "\",\"type\":8}", mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/comment_tag?charset=UTF-8&access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPromptKeyword(PromptGenerationVo promptGenerationVo) {
        String accessToken = this.getNlpAccessToken();
        String text = promptGenerationVo.getText();
        Integer maxGenLen = promptGenerationVo.getMaxGenLen();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            Map<String, Object> param = new HashMap<>();
            param.put("text", text);
            param.put("max_gen_len", maxGenLen);
            String s = JSONObject.toJSONString(param);
            RequestBody body =
                    RequestBody.create(s, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/text_gen/prompt_generation?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String getNlpAccessToken() {
        // 判断redis是否已经有token
        boolean ocrToken = redisUtils.hasKey("nlp_token");
        if (ocrToken) {
            // redis中存在有就直接从redis取
            String accessToken = String.valueOf(redisUtils.get("nlp_token"));
            log.debug("nlp_token过期时间" + redisUtils.getExpire("nlp_token"));
            return accessToken;
        }
        String response = accessTokenUtils.getAccessTokenInCloudBd(client_id, client_secret);
        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getString("access_token");
        String expiresIn = jsonObject.getString("expires_in");
        redisUtils.set("nlp_token", accessToken, Integer.parseInt(expiresIn));
        return accessToken;
    }
}
