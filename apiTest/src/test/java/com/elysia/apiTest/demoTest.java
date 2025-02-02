package com.elysia.apiTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cxb
 * @ClassName com.cxb.apiTest.demoTest
 * @date 17/5/2023 上午11:03
 */
@SpringBootTest
public class demoTest {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private String client_id = "H4BehRxuD1lbIlj9Snss8hCh";
    private String client_secret = "j4IEEgUTfDIbMLtoxSiqDfIArMGMIl34";
    private String refresh_token = "25.ed644f58957668348a5c9cbc81cb79bf.315360000.1999675461.282335-33627159";

    /**
     * 鉴权测试——获取token，便于后面调取相关服务
     */
    public String tokenTest(String client_id, String client_secret) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=" + client_id + "&client_secret=" + client_secret + "&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return response.body().string();
    }

    @Test
    public void tokenTest02() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?refresh_token=" + refresh_token + "&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());
    }

    // 对机器人进行测试，结果不错
    @Test
    public void botTest() throws IOException {
        String accessToken = "24.e90f140e579894dee920aee40baa9b0b.2592000.1687008517.282335-33706789";
        try {
            MediaType mediaType = MediaType.parse("application/json");
            Map<String, Object> param = new HashMap<>();
            param.put("version", "3.0");
            param.put("service_id", "S91207");
            param.put("log_id", "646613c27f9ca763e9e26bac");
            param.put("session_id", "GDOU114514");
            Map<String, Object> requestParam = new HashMap<>();
            requestParam.put("terminal_id", "A11");
            requestParam.put("query", "请问夹竹桃是什么？");
            param.put("request", requestParam);
            String s = JSONObject.toJSONString(param);
            RequestBody body =
                    RequestBody.create(s, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/unit/service/v3/chat?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        String phone = "19896862338";
        String apiKey = "f383fb2409144c728c1ce790a0df7962";
        String s = "";
        for (int i = 0; i < 6; i++) {
            s += (int) (Math.random() * 10);
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(" http://apis.haoservice.com/sms/sendv2?mobile=" + phone + "&content=" + "【微云信息团队】您的验证码为：" + s + ",有效时长为5分钟，若非本人操作，请忽略." + "&key=" + apiKey).get().build();
//        try {
//            // 先看看在redis中是否有对应的验证码缓存，有则删掉
//            if (redisUtils.hasKey(phone)){
//                redisUtils.del(phone);
//            }
////            boolean isOk = redisUtils.set(phone, s, 300);
//            if (!isOk) {
//                return false;
//            }
        Response response = client.newCall(request).execute();
        System.out.println((response.message()));
    }

    // 测试ocr的相关接口
    @Test
    public void OCRTest() throws IOException {
        String API_KEY = "ZzBycYXZDr3wk6tG4IXMDIRZ";
        String SECRET_KEY = "lL16oHxky5X0EpANLXxH78WOO2WIklRC";
        String token = this.tokenTest(API_KEY, SECRET_KEY);
        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Map<String, Object> param = new HashMap<>();
//        param.put(, );
        String s = JSONObject.toJSONString(param);
        RequestBody body =
                RequestBody.create(s, mediaType);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token=" + token)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
    }

    @Test
    public void testOss() {
        String nlpResult = "{\n" +
                "    \"results\": [\n" +
                "        {\n" +
                "            \"score\": 0.6082634968850668,\n" +
                "            \"word\": \"啼鸟\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"score\": 0.39173650311493324,\n" +
                "            \"word\": \"雨声\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"log_id\": 1663498283197531600\n" +
                "}";
        JSONObject jsonObject = JSON.parseObject(nlpResult);
        JSONArray results = jsonObject.getJSONArray("results");
        String words = "";
        for (int i = 0; i < results.size();i++) {
            words += results.getJSONObject(i).getString("word") + ",";
        }
        System.out.println(words);
    }

    @Test
    public void test05() throws ParseException {
        String url = "http://www.elysialove.xyz/wenxin/file/01.pdf";
        String filePath = "/data/wenxin/file/" + url.substring(38);
        System.out.println(filePath);
    }

    @Test
    public void test06() throws IOException {
//        private String client_id = "yPNA7bGZtMBaMwEfj9Az93Li";
//        private String client_secret = "soFNhbaYCLbYlXDnKddumpZAyRvApq10";
        String API_KEY = "yPNA7bGZtMBaMwEfj9Az93Li";
        String SECRET_KEY = "soFNhbaYCLbYlXDnKddumpZAyRvApq10";
//        String token = this.tokenTest(API_KEY, SECRET_KEY);
        String token = "24.96e894bf73afa7cce640d8b1c436c52e.2592000.1690376219.282335-35339304";
        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        String encode = FileUtils.getFileContentAsBase64("C:/Users/mlp52/Desktop/图集/22602766_0_final.png", false);
        Map<String, Object> param = new HashMap<>();
        param.put("text", "微信头像，二次元女生，半身像，尽量人物处于正中间，细致唯美，风格偏可爱风");
        param.put("image", encode);
        String s = JSONObject.toJSONString(param);
        RequestBody body =
                RequestBody.create(s, mediaType);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/VQA/ai_painting_match?access_token=" + token)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
//                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());
    }

    @Test
    public void No01() {

    }

}
