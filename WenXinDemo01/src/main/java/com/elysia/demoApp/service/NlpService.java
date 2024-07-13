package com.elysia.demoApp.service;

import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.vo.PromptGenerationVo;

import javax.servlet.http.HttpServletRequest;

public interface NlpService {
    /**
     * 文本关键词提取
     * @param text 字数限制：最大65535字符（1个汉字=1个字符）
     * @param request 请求的request
     * @return json对象
     */
    String textKeyWord(String text, int num);

    String getTextErrorCorrection(String words);

    String createPoemByKeyword(String text, Integer index, HttpServletRequest request);

    String sentimentClassify(String input);

    String getCommentTag(String input);

    String getPromptKeyword(PromptGenerationVo promptGenerationVo);
}
