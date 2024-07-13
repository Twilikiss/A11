package com.elysia.demoApp.utils;

import com.elysia.demoApp.model.vo.Text2ImageBaseVo;

import java.util.List;

/**
 * @author cxb
 * @ClassName StringUtils
 * @date 10/5/2023 下午5:02
 */
public class StringUtils {
    /**
     * 判断字符串是否为空
     * @ClassName StringUtils
     * @author   cxb
     * @date  10/5/2023 下午5:04
     * @param [data]
     * @return boolean
     */
    public static boolean isEmpty(String data) {
        return data == null || data.length() == 0;
    }

    /**
     * 用于引导型关键词作画（基础版）获取prompt
     * @param keyword 关键词
     * @param otherStyle 其他风格元素
     * @return prompt
     */
    public static String getText2ImageBasePrompt(String keyword, List<String> otherStyle) {
        StringBuilder prompt = new StringBuilder(keyword);
        // 如果传入的otherStyle为空（即用户没有选择其他的风格元素）就直接回传prompt
        if (otherStyle == null || otherStyle.size() == 0) {
            return prompt.toString();
        }
        for (String style : otherStyle) {
             prompt.append("、").append(style);
        }
        return prompt.toString();
    }


    /**
     * 判断接收到的Text2ImageBaseVo参数是否满足要求
     * @param text2ImageBaseVo 所传对象
     * @return 是否满足要求（true-满足;false-不满足）
     */
    public static boolean checkText2ImageBaseVo(Text2ImageBaseVo text2ImageBaseVo) {
        String keyword = text2ImageBaseVo.getKeyword();
        String style = text2ImageBaseVo.getStyle();
        String resolution = text2ImageBaseVo.getResolution();
        return !isEmpty(keyword) && !isEmpty(style) && !isEmpty(resolution);
    }
}
