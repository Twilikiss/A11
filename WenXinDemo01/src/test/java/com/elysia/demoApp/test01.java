package com.elysia.demoApp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.Repository.ArticleRepository;
import com.elysia.demoApp.model.entity.system.Article;
import com.elysia.demoApp.model.entity.system.TextSession;
import com.elysia.demoApp.service.AiPlatformApiService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.RedisUtils;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author cxb
 * @ClassName test01
 * @date 10/6/2023 上午11:24
 */
@SpringBootTest
public class test01 {
    @Resource
    private RedisUtils redisUtils;
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    @Resource
    private ArticleRepository articleRepository;
    @Test
    public void testRedis() {
        String key = "Elysia";
        String value01 = "12345";
        String value02 = "114514";
        redisUtils.setList(key,value01);
        redisUtils.setList(key,value02);
        boolean b = redisUtils.existValue(key, "114514");
        System.out.println(b);
    }

    @Test
    public void testAddArticle() {
        Article article = new Article();
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setIsDeleted(0);
        article.setTitle("什么是“AI”，带你走进“AI”的世界");
        article.setContent("");
        article.setThumbUp(0L);
        article.setVisits(0L);
        article.setType("homepage");
        articleRepository.insert(article);
    }
    @Test
    public void testGetArticle() {
        Article article = new Article();
        article.setType("homepage");
        Example<Article> example = Example.of(article);
        List<Article> articleList = articleRepository.findAll(example);
        System.out.println("获取成功！");
    }

    @Resource
    private AiPlatformApiService aiPlatformApiService;
    @Test
    public void test07() {
        JSONObject jsonObject = JSON.parseObject("{\n" +
                "    \"data\": {\n" +
                "        \"style\": \"油画\",\n" +
                "        \"taskId\": 8328053,\n" +
                "        \"imgUrls\": [\n" +
                "            {\n" +
                "                \"image\": \"https://wenxin.baidu.com/younger/file/ERNIE-ViLG/9eb039cab103d587ed92f52477e2f4a0ex\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"text\": \"睡莲\",\n" +
                "        \"status\": 1,\n" +
                "        \"createTime\": \"2022-11-17 16:52:10\",\n" +
                "        \"img\": \"https://wenxin.baidu.com/younger/file/ERNIE-ViLG/9eb039cab103d587ed92f52477e2f4a0ex\",\n" +
                "        \"waiting\": \"0\"\n" +
                "    },\n" +
                "    \"log_id\": 1593165135593954136\n" +
                "}");
        System.out.println(jsonObject.getJSONObject("data").getJSONArray("imgUrls").getJSONObject(0).getString("image"));
    }

    @Resource
    private AccessTokenUtils accessTokenUtils;

    /**
     * 重要对话测试
     * @ClassName test01
     * @author   cxb
     * @date  21/7/2023 上午12:12
     */
    @Test
    public void wenxinApiTest() throws IOException {
        String accessToken = accessTokenUtils.getAccessTokenWenXinWorkShop("vL1vuT0E8unb8iqrqIPSzGxX", "o0PXz8x127fYfaH0bLr3T3SaXWmSjuPf");
        Map<String, List<TextSession>> param = new HashMap<>();
        List<TextSession> list = new ArrayList<>();
        TextSession textSession = new TextSession();
        textSession.setRole("user");
        textSession.setContent("你现在是一个AI绘画描述语句生成软件，请遵循公式：Prompt（AI绘画描述语句）= 画面主体（画什么）+细节词（长什么样子）+风格词（是什么风格）+ 画面修饰词，你需要按照这个公式去引导、推荐和获取这些数据。注意你需要根据用户输入去推荐和引导。\n" +
                "1.注意理解修饰词的含义，它是用来增强和渲染画作的，例如：虚幻引擎、cg渲染、写实、精细刻画、赛博朋克、高清、辛烷渲染、黑白、像素艺术、低聚艺术、摄影风格、蒸汽朋克。在获取用户修饰词时，你需要给出列举几个常见的修饰词给用户参考。你需要主动地进行智能推荐。\n" +
                "2.在询问用户时，尽可能在10次对话以内完成，适可而止。问题需要围绕作画所需数据进行，不要涉及”性格“等抽象名词\n" +
                "3.注意：在接收的指令“直接输出prompt”时，你需要将画面主体、细节词、风格词和修饰词的对应文本拼接在一起输出，此时不要输出除prompt以外的文本，不需要回答”好的“等等对话词。\n" +
                "4.对话中尽可能模仿，不要谈及自己在角色扮演，现在请从询问用户选择什么作画主体开始");
        list.add(textSession);
        param.put("messages", list);
        String s = JSONObject.toJSONString(param);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(s, mediaType);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        if (response.body() != null) {
            System.out.println(response.body().string());
        }
    }

    @Test
    public void testPhoneNumber() {
        String filepath = "/data/elysia";
        String data =  filepath.substring(5);
        System.out.println(data);
    }
}
