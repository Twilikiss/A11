package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.Repository.OcrRepository;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.result.OcrResult;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import com.elysia.demoApp.service.OcrService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.FileUtils;
import com.elysia.demoApp.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author cxb
 * @ClassName OcrServiceImpl
 * @date 24/5/2023 下午10:09
 */
@Slf4j
@Service
public class OcrServiceImpl implements OcrService {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private AccessTokenUtils accessTokenUtils;

    @Resource
    private OcrRepository ocrRepository;

    @Value("${user.file.ocr_path}")
    private String ocrPath;

    @Value("${user.file.file_path}")
    private String filePath;

    @Value("${user.file.server}")
    private String server;
    private String client_id = "ZzBycYXZDr3wk6tG4IXMDIRZ";
    private String client_secret = "lL16oHxky5X0EpANLXxH78WOO2WIklRC";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    @Override
    public Map<String, Object> image2text(MultipartFile multipartFile, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String url = FileUtils.upload(multipartFile, ocrPath, server);
        String accessToken = this.getOcrAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body =
                    RequestBody.create("url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                OcrResult ocrResult = new OcrResult();
                // 初始化参数
                ocrResult.setCreateTime(new Date());
                ocrResult.setUpdateTime(new Date());
                ocrResult.setIsDeleted(0);

                ocrResult.setInput(url);
                ocrResult.setOutput(JSON.parseObject(result));
                ocrResult.setUid(uid);
                ocrResult.setType("accurate_basic");
                ocrRepository.insert(ocrResult);
                Map<String, Object> data = new HashMap<>();
                data.put("result", result);
                data.put("url", url);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> image2textHandWriting(MultipartFile multipartFile, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String url = FileUtils.upload(multipartFile, ocrPath, server);
        String accessToken = this.getOcrAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body =
                    RequestBody.create("url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/ocr/v1/handwriting?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                OcrResult ocrResult = new OcrResult();
                // 初始化参数
                ocrResult.setCreateTime(new Date());
                ocrResult.setUpdateTime(new Date());
                ocrResult.setIsDeleted(0);

                ocrResult.setInput(url);
                ocrResult.setOutput(JSON.parseObject(result));
                ocrResult.setUid(uid);
                ocrResult.setType("handwriting");
                ocrRepository.insert(ocrResult);
                Map<String, Object> data = new HashMap<>();
                data.put("result", result);
                data.put("url", url);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> docConvert(MultipartFile multipartFile, String type, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getOcrAccessToken();
        try {
            String url = FileUtils.upload(multipartFile, filePath, server);
            // TODO 测试用，上线请注意注释
//            String url = "http://www.elysialove.xyz/wenxin/file/01.pdf";
            // base64后URLEncode
            if (url == null) {
                throw new WenXinException(ResultCodeEnum.FILE_LOAD_ERROR);
            }
            String file = "/data/wenxin/file/" + url.substring(38);
            String encode = FileUtils.getFileContentAsBase64(file, true);
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(type + "=" + encode, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/ocr/v1/doc_convert/request?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                OcrResult ocrResult = new OcrResult();
                // 初始化参数
                ocrResult.setCreateTime(new Date());
                ocrResult.setUpdateTime(new Date());
                ocrResult.setIsDeleted(0);

                ocrResult.setInput(url);
                ocrResult.setOutput(JSON.parseObject(result));
                ocrResult.setUid(uid);
                // 设置识别类型为：”图文转换“类型
                ocrResult.setType("doc_convert");
                ocrRepository.insert(ocrResult);
                Map<String, Object> data = new HashMap<>();
                data.put("result", result);
                data.put("url", url);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkDocConvert(String taskId) {
        String accessToken = this.getOcrAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create( "task_id=" + taskId, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/ocr/v1/doc_convert/get_request_result?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
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
    public Map<String, Object> RemoveHandwriting(MultipartFile multipartFile, String type, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getOcrAccessToken();
        try {
            String url = FileUtils.upload(multipartFile, filePath, server);
            // TODO 测试用，上线请注意注释
//            String url = "http://www.elysialove.xyz/wenxin/file/01.pdf";
            // base64后URLEncode
            if (url == null) {
                throw new WenXinException(ResultCodeEnum.FILE_LOAD_ERROR);
            }
            String file = "/data/wenxin/file/" + url.substring(38);
            String encode = FileUtils.getFileContentAsBase64(file, true);
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(type + "=" + encode, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/ocr/v1/remove_handwriting?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                OcrResult ocrResult = new OcrResult();
                // 初始化参数
                ocrResult.setCreateTime(new Date());
                ocrResult.setUpdateTime(new Date());
                ocrResult.setIsDeleted(0);

                ocrResult.setInput(url);
                ocrResult.setOutput(JSON.parseObject(result));
                ocrResult.setUid(uid);
                // 设置识别类型为：”图文转换“类型
                ocrResult.setType("remove_handwriting");
                ocrRepository.insert(ocrResult);
                Map<String, Object> data = new HashMap<>();
                data.put("result", result);
                data.put("url", url);
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOcrAccessToken() {
        // 判断redis是否已经有token
        boolean ocrToken = redisUtils.hasKey("ocr_token");
        if (ocrToken) {
            // redis中存在有就直接从redis取
            String accessToken = String.valueOf(redisUtils.get("ocr_token"));
            log.debug("ocr_token过期时间" + redisUtils.getExpire("ocr_token"));
            return accessToken;
        }
        String response = accessTokenUtils.getAccessTokenInCloudBd(client_id, client_secret);
        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getString("access_token");
        String expiresIn = jsonObject.getString("expires_in");
        redisUtils.set("ocr_token", accessToken, Integer.parseInt(expiresIn));
        return accessToken;
    }
}
