package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.Repository.ImageProcessRepository;
import com.elysia.demoApp.model.result.ImageProcessResult;
import com.elysia.demoApp.service.ImageProcessService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.FileUtils;
import com.elysia.demoApp.utils.RedisUtils;
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
 * @ClassName ImageProcessServiceImpl
 * @date 12/7/2023 下午10:38
 */
@Service
@Slf4j
public class ImageProcessServiceImpl implements ImageProcessService {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private AccessTokenUtils accessTokenUtils;
    private String client_id = "itkVgrSKINOvYYzQOsNoy35u";
    private String client_secret = "5d79vWYbUuuwxGZEcWhlztfZKSuqF1HZ";
    @Value("${user.file.image_process}")
    private String imageProcessPath;
    @Value("${user.file.server}")
    private String server;
    @Resource
    private ImageProcessRepository imageProcessRepository;

    @Override
    public Map<String, Object> selfieAnime(MultipartFile multipartFile, String type, Integer maskId, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageProcessAccessToken();
        String url = FileUtils.upload(multipartFile, imageProcessPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String param = "";
            if ("anime".equals(type)) {
                param = "type=" + type + "&url=" + url;
            } else if ("anime_mask".equals(type) && maskId != 0) {
                param = "type=" + type + "&mask_id=" + maskId + "&url=" + url;
            } else {
                param = "type=anime&url=" + url;
            }
            RequestBody body = RequestBody.create(param, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-process/v1/selfie_anime?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                JSONObject resultJSONObject = JSON.parseObject(result);
                String imageBase64 = resultJSONObject.getString("image");
                // 将相关的数据保存到mongodb
                ImageProcessResult imageProcessResult = new ImageProcessResult();
                imageProcessResult.setCreateTime(new Date());
                imageProcessResult.setUpdateTime(new Date());
                imageProcessResult.setIsDeleted(0);

                imageProcessResult.setUid(uid);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("url", url);
                paramMap.put("type", type);
                paramMap.put("maskId", maskId);
                JSONObject jsonObject = new JSONObject(paramMap);
                imageProcessResult.setInput(jsonObject);

                imageProcessResult.setBase64(imageBase64);
                imageProcessResult.setProcessType("selfie_anime");

                imageProcessRepository.insert(imageProcessResult);

                Map<String, Object> data = new HashMap<>();
                data.put("source_image", url);
                String resultUrl = FileUtils.Base642Url(imageBase64);
                data.put("resulting_image", resultUrl);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> styleTrans(MultipartFile multipartFile, String option, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageProcessAccessToken();
        String url = FileUtils.upload(multipartFile, imageProcessPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("option=" + option + "&url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-process/v1/style_trans?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                JSONObject resultJSONObject = JSON.parseObject(result);
                String imageBase64 = resultJSONObject.getString("image");
                // 将相关的数据保存到mongodb
                ImageProcessResult imageProcessResult = new ImageProcessResult();
                imageProcessResult.setCreateTime(new Date());
                imageProcessResult.setUpdateTime(new Date());
                imageProcessResult.setIsDeleted(0);

                imageProcessResult.setUid(uid);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("url", url);
                paramMap.put("option", option);
                JSONObject jsonObject = new JSONObject(paramMap);
                imageProcessResult.setInput(jsonObject);

                imageProcessResult.setBase64(imageBase64);
                imageProcessResult.setProcessType("style_trans");
                imageProcessRepository.insert(imageProcessResult);

                Map<String, Object> data = new HashMap<>();
                data.put("source_image", url);
                String resultUrl = FileUtils.Base642Url(imageBase64);
                data.put("resulting_image", resultUrl);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> colourize(MultipartFile multipartFile, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageProcessAccessToken();
        String url = FileUtils.upload(multipartFile, imageProcessPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-process/v1/colourize?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                JSONObject resultJSONObject = JSON.parseObject(result);
                String imageBase64 = resultJSONObject.getString("image");
                // 将相关的数据保存到mongodb
                ImageProcessResult imageProcessResult = new ImageProcessResult();
                imageProcessResult.setCreateTime(new Date());
                imageProcessResult.setUpdateTime(new Date());
                imageProcessResult.setIsDeleted(0);

                imageProcessResult.setUid(uid);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("url", url);
                JSONObject jsonObject = new JSONObject(paramMap);
                imageProcessResult.setInput(jsonObject);

                imageProcessResult.setBase64(imageBase64);
                imageProcessResult.setProcessType("colourize");
                imageProcessRepository.insert(imageProcessResult);

                Map<String, Object> data = new HashMap<>();
                data.put("source_image", url);
                String resultUrl = FileUtils.Base642Url(imageBase64);
                data.put("resulting_image", resultUrl);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> imageDefinitionEnhance(MultipartFile multipartFile, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageProcessAccessToken();
        String url = FileUtils.upload(multipartFile, imageProcessPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-process/v1/image_definition_enhance?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                JSONObject resultJSONObject = JSON.parseObject(result);
                String imageBase64 = resultJSONObject.getString("image");
                // 将相关的数据保存到mongodb
                ImageProcessResult imageProcessResult = new ImageProcessResult();
                imageProcessResult.setCreateTime(new Date());
                imageProcessResult.setUpdateTime(new Date());
                imageProcessResult.setIsDeleted(0);

                imageProcessResult.setUid(uid);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("url", url);
                JSONObject jsonObject = new JSONObject(paramMap);
                imageProcessResult.setInput(jsonObject);

                imageProcessResult.setBase64(imageBase64);
                imageProcessResult.setProcessType("image_definition_enhance");
                imageProcessRepository.insert(imageProcessResult);

                Map<String, Object> data = new HashMap<>();
                data.put("source_image", url);
                String resultUrl = FileUtils.Base642Url(imageBase64);
                data.put("resulting_image", resultUrl);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> imageQualityEnhance(MultipartFile multipartFile, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageProcessAccessToken();
        String url = FileUtils.upload(multipartFile, imageProcessPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-process/v1/image_quality_enhance?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                JSONObject resultJSONObject = JSON.parseObject(result);
                String imageBase64 = resultJSONObject.getString("image");
                // 将相关的数据保存到mongodb
                ImageProcessResult imageProcessResult = new ImageProcessResult();
                imageProcessResult.setCreateTime(new Date());
                imageProcessResult.setUpdateTime(new Date());
                imageProcessResult.setIsDeleted(0);

                imageProcessResult.setUid(uid);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("url", url);
                JSONObject jsonObject = new JSONObject(paramMap);
                imageProcessResult.setInput(jsonObject);

                imageProcessResult.setBase64(imageBase64);
                imageProcessResult.setProcessType("image_quality_enhance");
                imageProcessRepository.insert(imageProcessResult);

                Map<String, Object> data = new HashMap<>();
                data.put("source_image", url);
                String resultUrl = FileUtils.Base642Url(imageBase64);
                data.put("resulting_image", resultUrl);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String getImageProcessAccessToken() {
        // 判断redis是否已经有token
        boolean aiPlayground = redisUtils.hasKey("image_process");
        if (aiPlayground) {
            // redis中存在有就直接从redis取
            String accessToken = String.valueOf(redisUtils.get("image_process"));
            log.debug("image_process_token过期时间" + redisUtils.getExpire("image_process"));
            return accessToken;
        }
        String response = accessTokenUtils.getAccessTokenInCloudBd(client_id, client_secret);
        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getString("access_token");
        String expiresIn = jsonObject.getString("expires_in");
        redisUtils.set("image_process", accessToken, Integer.parseInt(expiresIn));
        return accessToken;
    }
}
