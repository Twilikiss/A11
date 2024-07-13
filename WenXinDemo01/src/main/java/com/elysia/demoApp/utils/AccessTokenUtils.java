package com.elysia.demoApp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import com.elysia.demoApp.utils.helper.JwtHelper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author cxb
 * @ClassName AccessTokenUtils
 * @date 23/5/2023 下午10:06
 */
@Component
public class AccessTokenUtils {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    @Value("${user.jwt.jwtHead}")
    private String jwtHead;

    /**
     * 通过clientId和clientSecret获取对应的AccessToken
     *
     * @param clientId     应用id
     * @param clientSecret 应用密钥
     * @return AccessToken
     */
    public String getAccessTokenInCloudBd(String clientId, String clientSecret) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create("", mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/oauth/2.0/token?client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                response.close();
                return result;
            } else {
                throw new WenXinException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
            }
        } catch (IOException e) {
            throw new WenXinException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    public String getAccessTokenWenXinWorkShop(String API_KEY, String SECRET_KEY) {
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                    + "&client_secret=" + SECRET_KEY);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/oauth/2.0/token")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                String result = response.body().string();
                response.close();
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过access_token获取对应的uid
     *
     * @param accessToken 登录授权access_token
     * @return 对应的uid
     */
    public String getUid(String accessToken) {
        if (!StringUtils.isEmpty(accessToken) && accessToken.startsWith(jwtHead)) {
            accessToken = accessToken.substring(jwtHead.length() + 1);
            return JwtHelper.getUserUid(accessToken);
        }
        return null;
    }

    /**
     * 通过登录access_token获取对应的UserName
     *
     * @param accessToken 登录授权access_token
     * @return 对应的username
     */
    public String getUserName(String accessToken) {
        if (!StringUtils.isEmpty(accessToken) && accessToken.startsWith(jwtHead)) {
            accessToken = accessToken.substring(jwtHead.length() + 1);
            return JwtHelper.getUserName(accessToken);
        }
        return null;
    }

    /**
     * 通过refresh_token获取对应的uid
     *
     * @param refreshToken 授权刷新的refresh_token
     * @return 对应的uid
     */
    public String getUidByRe(String refreshToken) {
        if (!StringUtils.isEmpty(refreshToken)) {
            return JwtHelper.getUserUid(refreshToken);
        }
        return null;
    }

    /**
     * 通过登录refresh_token获取对应的UserName
     *
     * @param refreshToken 登录授权refresh_token
     * @return 对应的username
     */
    public String getUserNameByRe(String refreshToken) {
        if (!StringUtils.isEmpty(refreshToken)) {
            return JwtHelper.getUserName(refreshToken);
        }
        return null;
    }
}
