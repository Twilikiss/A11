package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.Repository.WenXinRepository;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.entity.system.TextSession;
import com.elysia.demoApp.model.entity.system.WenXinHistory;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import com.elysia.demoApp.model.vo.WenXinChatVo;
import com.elysia.demoApp.model.vo.WenXinHistoryVo;
import com.elysia.demoApp.service.WenXinService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.RedisUtils;
import com.elysia.demoApp.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 文心的
 *
 * @author cxb
 * @ClassName WenXinServiceImpl
 * @date 21/7/2023 下午10:11
 */
@Service
@Slf4j
public class WenXinServiceImpl implements WenXinService {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    @Resource
    private AccessTokenUtils accessTokenUtils;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private WenXinRepository wenXinRepository;
    private String client_id = "vL1vuT0E8unb8iqrqIPSzGxX";
    private String client_secret = "o0PXz8x127fYfaH0bLr3T3SaXWmSjuPf";

    @Override
    public Map<String, Object> getChatHistory(String id, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        //TODO 注意两种情况，一种是初始状态下的对话，要创建对话的初始信息；另外一种就是用户选择继续历史对话
        if ("-1".equals(id)) {
            // 创建对话
            WenXinHistory wenXinHistory = new WenXinHistory();
            wenXinHistory.setCreateTime(new Date());
            wenXinHistory.setUpdateTime(new Date());
            wenXinHistory.setIsDeleted(0);
            wenXinHistory.setUid(uid);
            JSONObject jsonObject = new JSONObject();
            TextSession textSession = new TextSession();
            textSession.setRole("user");
            textSession.setContent("你现在是一个AI绘画描述语句生成软件，请遵循公式：Prompt（AI绘画描述语句）包含四部分：画面主体（画什么）、细节描述（长什么样子）、风格词（是什么风格）和修饰词，你需要按照这个公式去引导、推荐和获取这些数据。注意你需要根据用户输入的“画面主体”主动去推荐和引导，要主动推进对话，逐一获取Prompt（AI绘画描述语句）包含四部分。\n" +
                    "\n" +
                    "1.注意理解风格词的含义，通常用户选择其中之一，常见的风格有哑光画、油画、动漫、二次元、素描画、铅笔画、浮世绘、涂鸦、炭笔画、矢量画、工笔画，在询问用户时请适当进行推荐。\n" +
                    "2.注意理解修饰词的含义，它是用来增强和渲染画作的，例如：虚幻引擎、cg渲染、写实、精细刻画、赛博朋克、高清、辛烷渲染、黑白、像素艺术、低聚艺术、摄影风格、蒸汽朋克。在获取用户修饰词时，你需要给出列举几个常见的修饰词给用户参考。\n" +
                    "特别的，当用户选择的是二次元风格、动漫风格等类似风格时，在最后输出的prompt中加上“唯美、电影运镜、电影级光影、飘逸、复杂的细节、绝美、极致细节、精美的CG、大师作品、超详细、8k分辨率”\n" +
                    "3.在询问用户时，尽可能在10次对话以内完成。问题需要围绕公式中prompt所需四部分进行，不要涉及到提问”性格“等抽象名词\n" +
                    "4.注意：在接收的指令“直接输出prompt”时，你需要将画面主体、细节词、风格词和修饰词的对应文本拼接在一起输出，此时不要输出除prompt以外的文本，不需要回答”好的“等等对话词。\n" +
                    "5.对话中尽可能模仿，不要谈及自己在角色扮演，现在请从询问用户选择什么作画主体开始");
            List<TextSession> list = new ArrayList<>();
            list.add(textSession);
            jsonObject.put("messages", list);

            // 初始化需要进行一次对话作为开始
            String wenxinResponse = this.getWenxinResponse(jsonObject);
            JSONObject resultJSONObject = JSON.parseObject(wenxinResponse);
            String result = resultJSONObject.getString("result");
            TextSession resultSession = new TextSession();
            resultSession.setRole("assistant");
            resultSession.setContent(result);
            boolean isSaveOk = jsonObject.getJSONArray("messages").add(resultSession);
            if (!isSaveOk) {
                throw new WenXinException("保存会话失败！", 201);
            }

            wenXinHistory.setSessionJson(jsonObject);
            WenXinHistory temp = wenXinRepository.insert(wenXinHistory);
            Map<String, Object> data = new HashMap<>();
            data.put("wenxin", jsonObject);
            data.put("talkId", temp.getId());
            return data;
        } else {
            WenXinHistory history = new WenXinHistory();
            history.setId(id);
            history.setUid(uid);
            history.setIsDeleted(0);
            Example<WenXinHistory> example = Example.of(history);
            Optional<WenXinHistory> optional = wenXinRepository.findOne(example);
            if (optional.isPresent()) {
                WenXinHistory wenXinHistory = optional.get();
                JSONObject sessionJson = wenXinHistory.getSessionJson();

                Map<String, Object> data = new HashMap<>();
                data.put("wenxin", sessionJson);
                data.put("talkId", id);
                return data;
            } else {
                throw new WenXinException("未能找到对话数据", 201);
            }
        }
    }

    @Override
    public Map<String, Object> getPromptWithChatByWenXin(WenXinChatVo wenXinChatVo, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        String talkId = wenXinChatVo.getTalkId();
        String content = wenXinChatVo.getContent();

        JSONObject chatHistory = (JSONObject) this.getChatHistory(talkId, req).get("wenxin");
        TextSession textSession = new TextSession();
        textSession.setRole("user");
        textSession.setContent(content);
        boolean isAddOk = chatHistory.getJSONArray("messages").add(textSession);
        if (!isAddOk) {
            throw new WenXinException("创建会话失败！", 201);
        }

        String wenxinResponse = this.getWenxinResponse(chatHistory);

        if (!StringUtils.isEmpty(wenxinResponse)) {
            JSONObject resultJSONObject = JSON.parseObject(wenxinResponse);
            String result = resultJSONObject.getString("result");
            TextSession resultSession = new TextSession();
            resultSession.setRole("assistant");
            resultSession.setContent(result);
            boolean isSaveOk = chatHistory.getJSONArray("messages").add(resultSession);
            if (!isSaveOk) {
                throw new WenXinException("保存会话失败！", 201);
            }
            // 保存对话信息
            Optional<WenXinHistory> optional = wenXinRepository.findById(talkId);
            if (!optional.isPresent()) {
                throw new WenXinException(ResultCodeEnum.DATA_ERROR);
            }
            WenXinHistory wenXinHistory = optional.get();
            wenXinHistory.setSessionJson(chatHistory);
            // 记录最后一次对话的时间
            wenXinHistory.setUpdateTime(new Date());
            wenXinRepository.save(wenXinHistory);
            Map<String, Object> data = new HashMap<>();
            data.put("wenxin", resultJSONObject);
            data.put("talkId", talkId);
            return data;
        } else {
            return null;
        }
    }

    @Override
    public List<WenXinHistoryVo> getAllHistory(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        WenXinHistory history = new WenXinHistory();
        history.setUid(uid);
        history.setIsDeleted(0);
        Example<WenXinHistory> example = Example.of(history);
        List<WenXinHistory> histories = wenXinRepository.findAll(example);
        List<WenXinHistoryVo> list = new ArrayList<>();
        for (WenXinHistory wenXinHistory : histories) {
            String id = wenXinHistory.getId();
            Date createTime = wenXinHistory.getCreateTime();
            Date updateTime = wenXinHistory.getUpdateTime();
            JSONArray messages = wenXinHistory.getSessionJson().getJSONArray("messages");
            TextSession textSession = (TextSession) messages.get(messages.size() - 1);
            String content = textSession.getContent();
            WenXinHistoryVo historyVo = new WenXinHistoryVo();
            historyVo.setTalkId(id);
            historyVo.setLastWord(content);
            historyVo.setCreateTime(createTime);
            historyVo.setUpdateTime(updateTime);
            list.add(historyVo);
        }
        return list;
    }

    @Override
    public String chatGetPrompt(String taskId, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);

        JSONObject chatHistory = (JSONObject) this.getChatHistory(taskId, req).get("wenxin");
        TextSession textSession = new TextSession();
        textSession.setRole("user");

        // 这里我们要输入指定的语句来获取我们所需要的prompt数据
        textSession.setContent(
                "作为prompt生成软件，现在请根据上面的对话直接输出prompt，你需要按照画面主体、细节词、风格词和修饰词合并在一起输出，注意不要分点输出，直接输出一句话。注意接下来不要输出除prompt以外的文字，在输出prompt文本前面不需要输出“好的”等对话和礼貌用语。\n" +
                        "输出格式例子：\n" +
                        "作画主体：一位可爱的少女，有着粉色的头发，穿着可爱的洛丽塔裙，端着一个茶杯。风格词：二次元。细节词：精细刻画、电影运镜、唯美、电影级光影。");

        boolean isAddOk = chatHistory.getJSONArray("messages").add(textSession);
        if (!isAddOk) {
            throw new WenXinException("创建会话失败！", 201);
        }

        String wenxinResponse = this.getWenxinResponse(chatHistory);

        if (!StringUtils.isEmpty(wenxinResponse)) {
            JSONObject resultJSONObject = JSON.parseObject(wenxinResponse);
            String result = resultJSONObject.getString("result");
            TextSession resultSession = new TextSession();
            resultSession.setRole("assistant");
            resultSession.setContent(result);
            boolean isSaveOk = chatHistory.getJSONArray("messages").add(resultSession);
            if (!isSaveOk) {
                throw new WenXinException("保存会话失败！", 201);
            }
            // 保存对话信息
            Optional<WenXinHistory> optional = wenXinRepository.findById(taskId);
            if (!optional.isPresent()) {
                throw new WenXinException(ResultCodeEnum.DATA_ERROR);
            }
            WenXinHistory wenXinHistory = optional.get();
            wenXinHistory.setSessionJson(chatHistory);
            // 记录最后一次对话的时间
            wenXinHistory.setUpdateTime(new Date());
            wenXinRepository.save(wenXinHistory);

            // 保存好数据后，返回所需的相关数据
            return result;
        } else {
            return null;
        }
    }

    private String getWenxinResponse(JSONObject chatHistory) {
        String wenXinAccessToken = getWenXinAccessToken();
        String s = chatHistory.toString();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(s, mediaType);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + wenXinAccessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
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

    /**
     * @return java.lang.String
     * @ClassName ApiServiceImpl
     * @author cxb
     * @date 23/5/2023 下午10:24
     */
    public String getWenXinAccessToken() {
        // 判断redis是否已经有token
        boolean wenxin = redisUtils.hasKey("wenxin_token");
        if (wenxin) {
            // redis中存在有就直接从redis取
            String accessToken = String.valueOf(redisUtils.get("wenxin_token"));
            log.debug("wenxin_token过期时间" + redisUtils.getExpire("wenxin_token"));
            return accessToken;
        }
        String response = accessTokenUtils.getAccessTokenWenXinWorkShop(client_id, client_secret);
        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getString("access_token");
        String expiresIn = jsonObject.getString("expires_in");
        redisUtils.set("wenxin_token", accessToken, Integer.parseInt(expiresIn));
        return accessToken;
    }
}
