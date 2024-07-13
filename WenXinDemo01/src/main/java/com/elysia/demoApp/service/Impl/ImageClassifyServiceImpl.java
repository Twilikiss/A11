package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.Repository.ImageClassifyRepository;
import com.elysia.demoApp.model.result.ImageClassifyResult;
import com.elysia.demoApp.service.ImageClassifyService;
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
 * @ClassName ImageClassifyServiceImpl
 * @date 31/5/2023 下午6:21
 */
@Slf4j
@Service
public class ImageClassifyServiceImpl implements ImageClassifyService {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private AccessTokenUtils accessTokenUtils;
    @Resource
    private ImageClassifyRepository imageClassifyRepository;
    private String client_id = "UVy0rnTaL7xeY6rSGXqgOq2b";
    private String client_secret = "rul2vDVOHTCRpACrjmgu9tSDGxk2NBDu";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    @Value("${user.file.image_classify_path}")
    private String imageClassifyPath;
    @Value("${user.file.server}")
    private String server;

    @Override
    public Map<String, Object> imageClassifyUniversal(MultipartFile multipartFile, Integer baikeNum, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url + "&baike_num=" + baikeNum, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("universal");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public Map<String, Object> imageClassifyDishes(MultipartFile multipartFile, Integer topNum, Integer baikeNum, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url + "&top_num=" + topNum + "&filter_threshold=0.95&baike_num=" + baikeNum, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v2/dish?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("dishes");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public Map<String, Object> imageClassifyAnimal(MultipartFile multipartFile, Integer topNum, Integer baikeNum, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url + "&top_num=" + topNum + "&baike_num=" + baikeNum, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/animal?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("animal");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public Map<String, Object> imageClassifyPlant(MultipartFile multipartFile, Integer baikeNum, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url + "&baike_num=" + baikeNum, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/plant?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("plant");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public Map<String, Object> imageClassifyIngredient(MultipartFile multipartFile, Integer topNum, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url + "&top_num=" + topNum, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/classify/ingredient?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("ingredient");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public Map<String, Object> imageClassifyLandmark(MultipartFile multipartFile, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/landmark?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("landmark");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public Map<String, Object> imageClassifyCurrency(MultipartFile multipartFile, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/currency?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("currency");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public Map<String, Object> imageClassifyCar(MultipartFile multipartFile, Integer topNum, Integer baikeNum, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url + "&top_num=" + topNum + "&baike_num=" + baikeNum, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/car?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("car");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public Map<String, Object> imageClassifyLogo(MultipartFile multipartFile, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String accessToken = this.getImageClassifyAccessToken();
        String url = FileUtils.upload(multipartFile, imageClassifyPath, server);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create("url=" + url, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/image-classify/v2/logo?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
                // 初始化的各项参数
                imageClassifyResult.setCreateTime(new Date());
                imageClassifyResult.setUpdateTime(new Date());
                imageClassifyResult.setIsDeleted(0);

                imageClassifyResult.setUid(uid);
                imageClassifyResult.setInput(url);
                imageClassifyResult.setOutput(JSON.parseObject(result));
                imageClassifyResult.setType("logo");
                imageClassifyRepository.insert(imageClassifyResult);
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
    public String getGraphicMatching(String prompt, String path) {
        String accessToken = this.getImageClassifyAccessToken();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            String encode = FileUtils.getFileContentAsBase64(path, false);
            Map<String, Object> param = new HashMap<>();
            param.put("text", prompt);
            param.put("image", encode);
            String s = JSONObject.toJSONString(param);
            RequestBody body =
                    RequestBody.create(s, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/VQA/ai_painting_match?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
    //                .addHeader("Accept", "application/json")
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

    /**
     * 获取图像识别模块的access_token
     *
     * @return java.lang.String
     * @ClassName ImageClassifyServiceImpl
     * @author cxb
     * @date 31/5/2023 下午7:28
     */
    public String getImageClassifyAccessToken() {
        // 判断redis是否已经有token
        boolean ocrToken = redisUtils.hasKey("imageClassify_token");
        if (ocrToken) {
            // redis中存在有就直接从redis取
            String accessToken = String.valueOf(redisUtils.get("imageClassify_token"));
            log.debug("imageClassify_token过期时间" + redisUtils.getExpire("imageClassify_token"));
            return accessToken;
        }
        String response = accessTokenUtils.getAccessTokenInCloudBd(client_id, client_secret);
        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getString("access_token");
        String expiresIn = jsonObject.getString("expires_in");
        redisUtils.set("imageClassify_token", accessToken, Integer.parseInt(expiresIn));
        return accessToken;
    }
}
