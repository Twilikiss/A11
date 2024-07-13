package com.elysia.demoApp.service;

import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.vo.WenXinChatVo;
import com.elysia.demoApp.model.vo.WenXinHistoryVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 文心相关的service
 * @ClassName WenXinServiceImpl
 * @author   cxb
 * @date  21/7/2023 下午10:09
 */
public interface WenXinService {
    Map<String, Object> getChatHistory(String id, HttpServletRequest request);

    Map<String, Object> getPromptWithChatByWenXin(WenXinChatVo wenXinChatVo, HttpServletRequest request);

    List<WenXinHistoryVo> getAllHistory(HttpServletRequest request);

    String chatGetPrompt(String id, HttpServletRequest request);
}
